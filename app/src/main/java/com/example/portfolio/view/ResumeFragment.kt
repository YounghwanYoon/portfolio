package com.example.portfolio.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.portfolio.databinding.FragmentResumeBinding
import kotlinx.android.synthetic.main.activity_main.*


class ResumeFragment : Fragment(), View.OnClickListener {
    private var _viewBinding:FragmentResumeBinding? = null
    private val viewBinding get()= _viewBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _viewBinding = FragmentResumeBinding.inflate(inflater, container, false)
        return _viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupListeners()
    }

    private fun setupListeners(){
        viewBinding?.toNextBtn?.setOnClickListener(this)
    }

    private fun setupNavigation(){
        navController = findNavController()
    }

    //clear backstack of previous fragment
    private fun clearBackStack(){
        val supportFragmentManager = activity?.supportFragmentManager

        repeat(supportFragmentManager!!.backStackEntryCount){
            supportFragmentManager.popBackStack()
        }
    }


    override fun onClick(view: View?) {
        when(view){
            viewBinding?.toNextBtn -> {
                clearBackStack()
                val action = ResumeFragmentDirections.actionResumeFragmentToHomeFragment()
                navController.navigate(action)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        private const val TAG = "ResumeFragment"
    }
}
