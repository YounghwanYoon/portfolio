package com.example.portfolio.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.demoapp.utils.DataState
import com.example.portfolio.view.adapter.CarouselAdapter
import com.example.portfolio.utils.HorizontalMarginItemDecoration
import com.example.portfolio.R
import com.example.portfolio.databinding.FragmentCarouselDisplayBinding
import com.example.portfolio.viewmodel.MainStateEvent
import com.example.portfolio.viewmodel.MyAppViewModel
import kotlinx.coroutines.launch
import java.lang.Math.abs


class CarouselDisplayFragment : Fragment(R.layout.fragment_carousel_display) {

    private val carouselAdapter = CarouselAdapter()
    private val viewModel by activityViewModels<MyAppViewModel>()
    //private lateinit var viewModel: MyAppViewModel
    private lateinit var binding:FragmentCarouselDisplayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCarouselDisplayBinding.inflate(inflater, container, false)
        setViewPager(binding.root)

        //var view = inflater.inflate(R.layout.fragment_carousel__dispaly_, container, false)
        //setViewPager(view)
        return binding.root
    }

    private fun setViewPager(view:View){
        val viewpager = view.findViewById<ViewPager2>(R.id.carousel_holder_viewpager)
        viewpager.adapter = carouselAdapter

        viewpager.clipToPadding = false
        viewpager.setPadding(10,0,10,0)

        viewpager.offscreenPageLimit = 1

        //Next preview size and distance for current item's horizontal margin.
        val nextVisiblePx = resources.getDimension(R.dimen.viewpager_next_visible_px)
        val currentItemHorizotalMarginPx = resources.getDimension(R.dimen.viewpager_current_horizontal_margin)

        //animation X-axie movement//transformer
        val pageTranslationX = nextVisiblePx + currentItemHorizotalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page:View , position:Float ->
            page.translationX = -pageTranslationX * position
            //next line scales - > next item's height.
            page.scaleY = 1-(0.25f * abs(position))
            //fading effect
            page.alpha = 0.25f + (1-abs(position))

        }
        viewpager.setPageTransformer(pageTransformer)

        // giving margin to recyclerview, so it can display pre & post item.
        val itemDeco = HorizontalMarginItemDecoration(requireContext(), R.dimen.viewpager_current_horizontal_margin)
        viewpager.addItemDecoration(itemDeco)
    }

    private fun getFakeData():MutableList<Int>{
        return mutableListOf(R.color.red, R.color.blue, R.color.yellow)

    }

    private fun subscribeObserver(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.dataUIState.collect { dataState ->
                when(dataState){
                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        displayErrorMessage(dataState.exception)
                    }

                    is DataState.Success -> {
                        displayProgressBar(false)
                        updateCarouselData(dataState.myApps)
                    }
                }
            }
        }
    }



    private fun displayProgressBar(isDisplayed:Boolean){
        binding.progressbarCarousel.visibility = if(isDisplayed) View.VISIBLE else View.GONE

    }

    private fun displayErrorMessage(exception:Exception){
        Toast.makeText(context,"Internet Error: ${exception.message}", Toast.LENGTH_SHORT).show()
    }

    private fun updateCarouselData(listLocalData: List<MyAppLocal>){
        carouselAdapter.setData(listLocalData)
        //carouselAdapter.setData(getFakeData())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //viewModel = ViewModelProvider(this)[MyAppViewModel::class.java]
        //updateCarouselData()
        viewModel.setStateEvent(MainStateEvent.GetMyAppEvents)
        subscribeObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private val TAG:String = this.javaClass.name
    }
}