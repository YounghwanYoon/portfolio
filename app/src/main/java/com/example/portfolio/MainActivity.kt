package com.example.portfolio

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.portfolio.feature_permission.presentation.GPSTracker
import com.example.portfolio.feature_permission.presentation.LocationForegroundService
import com.example.portfolio.feature_permission.presentation.LocationStateVM
import com.example.portfolio.feature_permission.presentation.PermissionTracker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val permissionVM: LocationStateVM by viewModels()
    private val permListener:PermissionTracker ? = null
    private var permLauncher:ActivityResultLauncher<Array<String>>? = null
    private val permList = arrayListOf<String>()
    private lateinit var gpsTracker:GPSTracker

    private var singlePermissionContract: ActivityResultContracts.RequestPermission? = null
    private var multiPermissionContract: ActivityResultContracts.RequestMultiplePermissions? = null

    private val networkVM:NetworkStatusViewModel by viewModels()
    private var navController: NavController? = null
    private var navHostFragment:NavHostFragment? = null

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun permissionHandler(){
        //check permission
        fun checkPermission(perm:String):Boolean{
            return ContextCompat.checkSelfPermission(this.applicationContext, perm ) == PackageManager.PERMISSION_GRANTED
        }

        //Handle Case Base On Permission State
        LocationStateVM.PERMISSIONS.forEach{
            when(checkPermission(it)){
                true -> {
                    Timber.tag("MainActivity").d("permissionHandler: Permission is Granted")
                }
                false -> {
                    Timber.tag("MainActivity").d("permissionHandler: Permission is not yet granted")

                    shouldShowRequestPermissionRationale(it)
                    permList.add(it)
                }
            }
        }

        //request Permission
        if(permList.isNotEmpty()){
            singlePermissionContract = ActivityResultContracts.RequestPermission()
            multiPermissionContract = ActivityResultContracts.RequestMultiplePermissions()
            //permLauncher =
            val permArray = permList.toTypedArray()
            requestPermissions(permArray, 123)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).d("onCreate: MainActivity ")
        setContentView(R.layout.activity_main)
        subscribeNetworkVM()
        subscribePermissionVM()
        setupNavigation()
        //supportFragmentManager.beginTransaction().add(R.id.newFaceContainer, MainFragment()).commit()
    }

    private fun subscribeNetworkVM(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                networkVM.networkMonitor.isConnected.collectLatest {
                    when(it){
                        true -> {
                            Timber.tag(TAG).d("subscribeVM: Internet is available")
                        }
                        false -> {
                            makeDialog(this@MainActivity, "Internet is not available")
                            Timber.tag(TAG).d("subscribeVM: internet is not available")
                        }
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LocationStateVM.PERMISSION_CODE->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Timber.tag("MainActivity").d("onRequestPermissionsResult: User Granted Permission")
                    permissionVM.setPermState(
                        LocationStateVM.PermissionState.Granted
                    )
                }else{
                    //User denied permission
                    //Need to explain to user and ask user to redirect to setting or stop program
                    Timber.tag("MainActivity").d("onRequestPermissionsResult: User Denied Permission")
                    permissionVM.setPermState(
                        LocationStateVM.PermissionState.Denied("User refused to accept")
                    )
                }
                return
            }
            else ->{
                Timber.tag("MainActivity").d("Other Permission Code")
                //Not permission request result
                //Ignore
            }
        }

        //PermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun subscribePermissionVM(){
        lifecycleScope.launch{
            repeatOnLifecycle(state = Lifecycle.State.RESUMED){
                permissionVM.permState.collect{
                    when(it){
                        is LocationStateVM.PermissionState.Denied -> {
                            Timber.tag("MainActivity").d("subscribePermissionVM: Denied")
                            TODO()
                        }
                        LocationStateVM.PermissionState.Granted -> {
                            Timber.tag("MainActivity").d("subscribePermissionVM: Granted")
                            gpsTracker = object: GPSTracker{

                                override fun onLocationReceived(lattitude: Double, longitude: Double){
                                    Timber.tag("MainActivity")
                                        .d("onGPSChanged: lat =$lattitude, long= $longitude")                                }

                                override fun onFailedToReceivedLocation() {
                                    Timber.tag("MainActivity").d("onFailedToReceivedLocation: ")
                                }
                            }
                            LocationForegroundService.startService(
                                context = applicationContext,
                                message = "starting service",
                                gpsTracker = gpsTracker
                            )
                        }
                        LocationStateVM.PermissionState.Requesting -> {
                            Timber.tag("MainActivity").d("subscribePermissionVM: Requesting")
                            permissionHandler()
                        }
                    }
                }
            }
        }
    }

    private fun setupNavigation(){
        //attempting to retrieve the NavController in onCreate() of an Activity via Navigation.
        // findNavController(Activity, @IdRes int) will fail.
        // You should retrieve the NavController directly from the NavHostFragment instead.
        if(navHostFragment == null)
            navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        if(navController == null)
            navController = navHostFragment!!.navController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //inflate the men; this adds SellingItem to the action bar if it is present
        //menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun makeDialog(context: Context, rational:String, listener: (() -> Unit)? = null){
        AlertDialog.Builder(context)
            .setMessage(rational)
/*            .setPositiveButton("Enable", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context.startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }
            })*/
            .setNegativeButton("Cancel", null)
            .show()
    }
}