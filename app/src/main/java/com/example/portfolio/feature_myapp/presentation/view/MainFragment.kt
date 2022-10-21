package com.example.portfolio.feature_myapp.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.portfolio.R
import com.example.portfolio.databinding.*
import com.example.portfolio.feature_myapp.domain.model.Title_Image
import com.example.portfolio.feature_myapp.presentation.view.adapter.MainGridAdapter
import com.example.portfolio.feature_secondactivity.presentation.SecondActivity


class MainFragment : Fragment(R.layout.fragment_main){
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainGridAdapter
    //private lateinit var gridAdapter:MainPageGridAdapter
    private lateinit var titleImageData:ArrayList<Title_Image>
//    val api_key = com.example.portfolio.BuildConfig.API_KEY

    private lateinit var navController: NavController

    private fun setUpNavController(){
        navController = findNavController()
        //navController = findNavController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setUpNavController()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setGridAdapter()
        //setAdapter()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
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

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    private fun setData(){
        titleImageData = arrayListOf()
        titleImageData.add(Title_Image( "Weather Api", R.drawable.ic_face_48dp, 1L))
        titleImageData.add(Title_Image( "Shopping Api", R.drawable.ic_face_48dp,2L))
        titleImageData.add(Title_Image( "Media Player", R.drawable.ic_face_48dp,3L))
        titleImageData.add(Title_Image( "DemoApp4", R.drawable.ic_face_48dp,4L))
        titleImageData.add(Title_Image( "DemoApp5", R.drawable.ic_face_48dp,5L))
        titleImageData.add(Title_Image( "Resume", R.drawable.ic_face_48dp,6L))
    }
    private fun setGridAdapter(){
        setData()

        binding.apply{
          adapter = MainGridAdapter(this.root.context, titleImageData)
          this.mainGridview.adapter = adapter
          this.mainGridview.setOnItemClickListener{parent, view, position, id ->
              Toast.makeText(this.root.context,"You Clicked this + $position", Toast.LENGTH_SHORT).show()
              setUpNavigation(position)
          }
        }
    }

    private fun setUpNavigation(position:Int){
        val action: NavDirections
        when(position){
            0 ->{
                action= MainFragmentDirections.actionMainFragmentToWeatherFragment()
                navController.navigate(action)
            }

            4 ->{
                val intent:Intent = Intent(requireContext(), SecondActivity::class.java)
                startActivity(intent)
//                finish()
            }

        }
    }

    /*private fun setAdapter(){
        setData()
        binding.also{
            gridAdapter = MainPageGridAdapter(it.root.context, titleImageData)
            it.mainGridview.adapter = gridAdapter
            it.mainGridview.setOnItemClickListener{parent, view, position, id ->
                Toast.makeText(it.root.context,"You Clicked this + $position", Toast.LENGTH_SHORT).show()
            }
        }
        gridAdapter.notifyDataSetChanged()
    }*/

    companion object {
        private const val TAG = "MainFragment"
    }
}