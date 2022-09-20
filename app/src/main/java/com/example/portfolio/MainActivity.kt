package com.example.portfolio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private var navHostFragment:NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportFragmentManager.beginTransaction().add(R.id.newFaceContainer, MainFragment()).commit()
    }
    override fun onResume() {
        setupNavigation()
        super.onResume()
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



}