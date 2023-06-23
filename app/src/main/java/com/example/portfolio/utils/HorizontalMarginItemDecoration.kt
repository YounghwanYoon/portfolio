package com.example.portfolio.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//Adapted from : https://stackoverflow.com/questions/10098040/android-viewpager-show-preview-of-page-on-left-and-right
/**
 * Adds margin to the left and right sides of the RecyclerView item.
 * This prevent current recyclerview item to take entire space in the screen.
 * Adapted from https://stackoverflow.com/a/27664023/4034572
 * @param horizontalMarginInDp the margin resource, in dp.
 */
class HorizontalMarginItemDecoration(context: Context, horizontalMargin: Int) : RecyclerView.ItemDecoration(){

    private val horizontalMargin:Int = context.resources.getDimension(horizontalMargin).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = horizontalMargin
        outRect.left = horizontalMargin
    }
}