package com.example.portfolio.feature_myapp.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.databinding.MyappIndividualBinding
import com.example.portfolio.feature_myapp.data.AppData
import com.example.portfolio.utils.GlideApp

class MyAppRecyclerviewAdapter(val data:List<AppData>):
    RecyclerView.Adapter<MyAppRecyclerviewAdapter.ViewHolder>() {
    private lateinit var binding:MyappIndividualBinding
    private var _data:List<AppData>
    init{
        _data = data
    }

    fun updateData(newData:List<AppData>){
        this._data = newData
        notifyDataSetChanged()
        
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAppRecyclerviewAdapter.ViewHolder {

        binding = MyappIndividualBinding.inflate(LayoutInflater.from(parent.context), parent, false)


/*
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myapp_individual, parent, false)

        return ViewHolder(view)

 */
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyAppRecyclerviewAdapter.ViewHolder, position: Int) {
        with(holder){
            with(_data[position]){
                binding.myappTitle.text = this.title
                binding.appDescription.text = this.description
                binding.urlTxt.text = this.url

                GlideApp.with(holder.itemView.context)
                    .load("")
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imageView)
            }
        }

    }

    override fun getItemCount(): Int {
        return _data.size
    }


    class ViewHolder(binding: MyappIndividualBinding):RecyclerView.ViewHolder(binding.root)
}