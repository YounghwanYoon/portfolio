package com.example.portfolio.feature_main_fragment.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.databinding.FragmentMainRecyclerviewItemBinding
import com.example.portfolio.feature_main_fragment.presentation.helper.RecyclerViewClickListner
import com.example.portfolio.feature_myapp.domain.model.Title_Image

class MainRecyclerView(val list:ArrayList<Title_Image>, val itemListener:RecyclerViewClickListner): RecyclerView.Adapter<MainRecyclerView.MainViewHolder>() {
    lateinit var binding:FragmentMainRecyclerviewItemBinding

    inner class MainViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun onBind(data:Title_Image, position:Int){
            binding.mainRecyclerviewImg.setImageResource(data.image)
            binding.mainRecyclerviewTxt.text = data.title

            view.setOnClickListener {
                itemListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = FragmentMainRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}