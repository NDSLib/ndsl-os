package com.ndsl.os.util

import com.ndsl.graphics.pos.Rect
import java.awt.Graphics

fun Rect.copy():Rect{
    return Rect(this.left_up,this.right_down)
}

fun Graphics.drawRect(r:Rect){
    drawRect(r.left_up.x,r.left_up.y,r.width,r.height)
}

fun Graphics.fillRect(r:Rect){
    fillRect(r.left_up.x,r.left_up.y,r.width,r.height)
}