package com.example.portfolio.feature_shopping.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

class ShoppingFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply{
            setContent {
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ColorBox(modifier:Modifier = Modifier){
    val color = remember{
        mutableStateOf(Color.Yellow)
    }
    Box(
        modifier = Modifier
            .background(color.value)
    ){
        Text(text ="Hello World2")
        Text(text="wtf")
    }
}

@Composable
fun CardBox(modifier:Modifier = Modifier){


}


@Preview
@Composable
fun PreviewColorBox(){
    ColorBox()
}