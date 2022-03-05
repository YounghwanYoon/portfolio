package com.example.portfolio.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.portfolio.R

class CarouselAdapter: RecyclerView.Adapter<CarouselAdapter.MyViewHolder>() {
    private var items = mutableListOf<Int>()
    private var localData: List<MyAppLocal> = listOf()

    fun setData(newItems:MutableList<Int>){
        items = newItems
        notifyDataSetChanged()
    }

    fun setData(newItems:List<MyAppLocal>):Boolean{
        if(newItems.isEmpty())
            return false

        localData = newItems
        notifyDataSetChanged()
        return true
    }

    inner class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
//        fun bind(item:Int){
        @SuppressLint("ResourceAsColor")
        fun bind(data: MyAppLocal){
            //for testing, use only color reference
            //in real app, better update it to displaying picture with Glide class

            val imageHolder = view.findViewById<LinearLayout>(R.id.image_holder)
            var color = data.picture_color

            when(color){
                R.color.red ->imageHolder.setBackgroundColor(color)
                R.color.blue -> imageHolder.setBackgroundColor(color)
                R.color.yellow -> imageHolder.setBackgroundColor(color)
                else -> imageHolder.setBackgroundColor(R.color.black)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_carousel_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.bind(items[position])
        holder.bind(localData[position])
    }

    override fun getItemCount(): Int {
        return localData.size
    }

}