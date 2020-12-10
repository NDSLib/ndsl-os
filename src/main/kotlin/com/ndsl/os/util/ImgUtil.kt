package com.ndsl.os.util

import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage;

class ImgUtil {
    companion object{
        fun fillGen(color:Color,width:Int,height:Int):Image{
            val array : IntArray = IntArray(width*height)
            for (index in array.indices){
                array[index] = 255 shl 24 or color.red shl 16 or color.green shl 8 or color.blue
            }
            val img = BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)
            img.setRGB(0,0,width,height,array,0,0)
            return img
        }
    }
}