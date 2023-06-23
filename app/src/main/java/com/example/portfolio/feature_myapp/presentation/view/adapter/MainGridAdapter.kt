package com.example.portfolio.feature_myapp.presentation.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.portfolio.R
import com.example.portfolio.databinding.GridViewItemsBinding
import com.example.portfolio.feature_myapp.domain.model.Title_Image

class MainGridAdapter(
    private val context: Context,
    private val titlesImages:ArrayList<Title_Image>
    ): BaseAdapter() {


    var inflater:LayoutInflater? = null
    var itemBinding: GridViewItemsBinding? = null

    override fun getCount(): Int {
        return titlesImages.size
    }

    override fun getItem(position: Int): Any? {
        return titlesImages[position]
    }

    override fun getItemId(position: Int): Long {
        return titlesImages[position].id
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if(inflater == null)
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        itemBinding = GridViewItemsBinding.inflate(inflater!!, parent, false)
        //val itemView = inflater!!.inflate(R.layout.grid_view_items, parent, false)

        handleView(position)
        return itemBinding!!.root
    }
    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if(inflater == null)
            //inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater = LayoutInflater.from(context)

        if(itemBinding == null)
            itemBinding = GridViewItemsBinding.inflate(inflater!!, parent, false)

        handleView(position)

        return itemBinding!!.root
    }*/

    private fun handleView(position:Int){
        itemBinding?.apply{
           var title = titlesImages[position].title
            var image: Int? = titlesImages[position].image

            if(titlesImages.isEmpty())
                title = "empty"

            if(image == null)
                image = R.drawable.ic_baseline_warning_24

            this.gridItemTitle.text = title
            this.gridItemImage.setImageResource(image)
            /*Glide.with(context)
                .load(image)
                .error(R.drawable.droid_background)
                .into(this.gridItemImage)*/
        }
    }


}







/*
class MainGridAdapter(
    val context: Context,
    val titles:Array<String>,
    val images:IntArray ): BaseAdapter() {

    var inflater:LayoutInflater? = null
    var itemBinding: GridViewItemsBinding? = null

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if(inflater == null)
            //inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater = LayoutInflater.from(context)

        if(itemBinding == null)
            itemBinding = GridViewItemsBinding.inflate(inflater!!, parent, false)

        handleView(position)

        return itemBinding!!.root
    }

    private fun handleView(position:Int){
        itemBinding?.apply{
           var title = titles[position]
            var image: Int? = images[position]

            if(titles[position].isEmpty())
                title = "empty"

            if(image == null)
                image = R.drawable.ic_face_48dp

            this.gridItemTitle.text = title
            this.gridItemImage.setImageResource(image)
            */
/*Glide.with(context)
                .load(image)
                .error(R.drawable.droid_background)
                .into(this.gridItemImage)*//*

        }
    }


}*/
