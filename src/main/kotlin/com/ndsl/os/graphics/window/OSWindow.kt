package com.ndsl.os.graphics.window

import com.ndsl.graphics.pos.Rect
import com.ndsl.os.util.ThreadSafer
import java.awt.Graphics


abstract class OSWindow(r:Rect,d:WindowDrawer){
    private val showing = true
    val isShowing = ThreadSafer(showing)
    val rect = ThreadSafer(r)
    val drawer = ThreadSafer(d)
    fun onDraw(g:Graphics){
        drawer.use { it.draw(g,rect.use()) }
    }
}

abstract class WindowDrawer {
    abstract fun draw(g:Graphics,r:Rect)
}
