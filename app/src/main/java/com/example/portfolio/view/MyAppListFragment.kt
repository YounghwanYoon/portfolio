package com.example.portfolio.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.portfolio.R
import com.example.portfolio.databinding.FragmentMyAppListBinding
import com.example.portfolio.model.AppData
import com.example.portfolio.view.adapter.MyAppRecyclerviewAdapter

class MyAppListFragment : Fragment(), View.OnClickListener {
    private var _viewbinding:FragmentMyAppListBinding? = null
    private val viewbinding get() = _viewbinding
    private lateinit var adapter:MyAppRecyclerviewAdapter
    private lateinit var navController:NavController

    private var testData = listOf<AppData>(
        AppData(null,"First Title", "Blahblahahah", "www."),
        AppData(null,"Second Title", "Blahblahahah", "www."),
        AppData(null,"Third Title", "Blahblahahah", "www."),
        AppData(null,"Fourth Title", "Blahblahahah", "www."),
        AppData(null,"Fifth Title", "Blahblahahah", "www."),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewbinding = FragmentMyAppListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_my_app_list, container, false)
        val view = viewbinding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupNavigation()
    }

    private fun setupRecyclerView(){
        val recyclerView = viewbinding?.myappsRecyclerview
        adapter = MyAppRecyclerviewAdapter(testData)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        //adapter.updateData(testData)
    }

    private fun setupNavigation(){
        navController = findNavController()
        _viewbinding?.toNextBtn?.setOnClickListener(this)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _viewbinding = null
    }

    companion object {
        private const val TAG = "MyAppListFragment"
    }

    override fun onClick(view: View?) {

        when(view){
            _viewbinding?.toNextBtn ->{
                val action:NavDirections = MyAppListFragmentDirections.actionMyAppListFragmentToResumeFragment()
                navController.navigate(action)

            }
        }
    }
}