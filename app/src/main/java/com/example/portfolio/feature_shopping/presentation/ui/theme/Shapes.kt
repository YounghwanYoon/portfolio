package com.example.portfolio.feature_shopping.presentation.ui.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp


val Shapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(6.dp),
)

val TestShape = GenericShape{ size, layoutDirection ->
    //Starting point (top left is starting)
    moveTo(x= 0f, y= 0f)
    //it will draw a line
    //To bottom right
    lineTo(x= size.width, y= size.height)
    //To Bottom left
    lineTo(x=0f, y= size.height)
//GenericShape will implicitly execute
// close()which will draw a line from last painter coordinate to the first defined
}

val MugShape = GenericShape{ size, layoutDirection ->

    val startingHandle= size.width*0.95f
    val handleHeight = size.height*0.20f
    val handleYStarting = size.height * .10f
    //Starting (of cup) point (top left is starting)
    moveTo(x= 0f, y= 0f)
    //top right before end
    lineTo(x= startingHandle, y=0f)

    //drawing a handle
    //starting point of handle
    lineTo(x= startingHandle, y=handleYStarting)
    //to right end
    lineTo(x= size.width, y=handleYStarting)
    //to move down
    lineTo(x= size.width, y=handleHeight)
    //to move left
    lineTo(x= startingHandle, y= handleHeight)
    //backTo handle starting point
    //End of handle coordinate
    lineTo(x= startingHandle, y=handleYStarting)

    //Continue Cup Shape
    //Bottom right end *0.9f
    lineTo(x=startingHandle, y= size.height)
    //move to left (coordinate is bottom left most)
    lineTo (x=0f ,y=size.height)
    //back to starting (of mug) point
    //which will be executed by GenericShape by calling close()
    //done
}