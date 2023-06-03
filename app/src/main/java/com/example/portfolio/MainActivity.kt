package com.example.portfolio

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.portfolio.utils.Constants
import com.example.portfolio.utils.NetworkStatus
import com.example.portfolio.utils.PermissionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val networkVM:NetworkStatusViewModel by viewModels()

    private var navController: NavController? = null
    private var navHostFragment:NavHostFragment? = null

    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(R.layout.activity_main)
        subscribeVM()
        //supportFragmentManager.beginTransaction().add(R.id.newFaceContainer, MainFragment()).commit()
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        setupNavigation()
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart: ")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    private fun subscribeVM(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                networkVM.networkMonitor.isConnected.collectLatest {
                    when(it){
                        true -> {
                            Log.d(TAG, "subscribeVM: Internet is available")
                        }
                        false -> {
                            makeDialog(this@MainActivity, "Internet is not available")
                            Log.d(TAG, "subscribeVM: internet is not available")
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
        PermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        //inflate the men; this adds items to the action bar if it is present
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