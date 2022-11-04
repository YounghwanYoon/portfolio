package com.example.portfolio.feature_myapp.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.portfolio.R
import com.example.portfolio.feature_myapp.presentation.viewmodel.HomeFragment_VM


class HomeFragment : Fragment()/*View.OnClickListener */{
    private lateinit var navController:NavController
    private val vm:HomeFragment_VM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
    }


    private fun setupNavController(){
        navController = findNavController(this)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)
/*

        setOnClickListeners(view)
*/

        return view
    }
/*
    private fun setOnClickListeners(view:View){
        view.demo_apps_btn.setOnClickListener(this)
        view.to_next_btn.setOnClickListener(this)
        view.to_resume_btn.setOnClickListener(this)
        view.to_demo_btn.setOnClickListener(this)
    }*/

    private fun clearBackStack(){
        /*
        val supportFragmentManager = activity?.supportFragmentManager

        repeat(supportFragmentManager!!.backStackEntryCount){
            supportFragmentManager.popBackStack()
        }

         */
        navController.popBackStack(R.id.resumeFragment, true)
        //or

    }

    override fun onResume() {
        super.onResume()
        //clearBackStack()

    }
/*
    override fun onClick(view: View?) {
        val action:NavDirections
        when(view){
            demo_apps_btn -> {

            }
            to_resume_btn->{
                action = HomeFragmentDirections.actionHomeFragmentToResumeFragment()
                navController.navigate(action)
            }
            *//*
            hometitle_edittext -> {
                val tempText = hometitle_edittext.text
                vm.saveState(HomeFragment_VM.SAVED_EDIT_TEXT, tempText)
            }

             *//*
            to_next_btn -> {
                action = HomeFragmentDirections.actionHomeFragmentToMyAppListFragment()
                navController.navigate(action)
            }

            demo_apps_btn -> {
                action = HomeFragmentDirections.actionHomeFragmentToDemoApps()
                navController.navigate(action)
            }
            to_demo_btn -> {
                action = HomeFragmentDirections.actionHomeFragmentToCarouselDisplayFragment()

                navController.navigate(action)
            }

        }
    }*/
}