package com.ndsl.os.util

class First(private val func : () -> Unit){
    var isFirst = true
    fun invoke(){
        if (isFirst){
            func.invoke()
            isFirst = false
        }
    }

    fun invoke(func : () -> Unit){
        if (isFirst){
            func.invoke()
            isFirst = false
        }
    }
}