package com.example.portfolio.feature_myapp.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.portfolio.R
import com.example.portfolio.databinding.GridViewItemsBinding
import com.example.portfolio.feature_myapp.domain.model.Title_Image

class MainPageGridAdapter(context: Context, initData:ArrayList<Title_Image>):ArrayAdapter<Title_Image>(context, 0) {
    private var data:ArrayList<Title_Image> = initData
    private var itemBinding: GridViewItemsBinding? = null
    private var inflater:LayoutInflater? = null
    private var v: View? = null

    fun updateData(newData:ArrayList<Title_Image>):Boolean{
        if(newData.isEmpty())
            return false
        this.data = newData

        notifyDataSetChanged()
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if(v == null)
            v = LayoutInflater.from(context).inflate(R.layout.grid_view_items, parent, false)

        if(inflater == null)
            //inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater = LayoutInflater.from(context)

        if(itemBinding == null)
            itemBinding = GridViewItemsBinding.inflate(inflater!!, parent, false)
        //handleView(position)
        //return itemBinding!!.root

        val titleView = v!!.findViewById<TextView>(R.id.grid_item_title)
        val imageView = v!!.findViewById<ImageView>(R.id.grid_item_image)
        v.apply{
            titleView.text = data[0].title
            imageView.setImageResource(data[0].image)
        }

        return LayoutInflater.from(context).inflate(R.layout.grid_view_items, parent, false)

    }
    private fun handleView(position:Int):Boolean{
        if(data.isEmpty()){
            itemBinding!!.apply{
                this.gridItemTitle.text = "There is no data"
                Glide.with(context)
                    .load(R.drawable.ic_baseline_android_24)
                    .error(R.drawable.ic_baseline_android_24)
                    .into(this.gridItemImage)
            }
            return false
        }

        data[position].also {
            itemBinding!!.apply{
                this.gridItemTitle.text = it.title
                Glide.with(context)
                    .load(R.drawable.ic_face_48dp)
                    .error(R.drawable.ic_baseline_warning_24)
                    .into(this.gridItemImage)

                this.gridItemImage.setImageResource(it.image)
            }
        }

        return true
    }


}