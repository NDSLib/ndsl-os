package com.ndsl.os.graphics

import com.ndsl.graphics.display.Display
import com.ndsl.graphics.display.drawable.IDrawable
import com.ndsl.graphics.display.drawable.base.Drawable
import com.ndsl.graphics.pos.Rect
import com.ndsl.os.graphics.window.OSWindow
import com.ndsl.os.graphics.window.WindowDrawer
import com.ndsl.os.main.os
import com.ndsl.os.util.First
import com.ndsl.os.util.ImgUtil
import com.ndsl.os.util.ThreadSafer
import java.awt.Color
import java.awt.Graphics
import java.awt.Image

class OSDisplay {
    private val display = Display("NDSL-OS", 3, Rect(0, 0, 1920, 1080))
    val vDisplay = ThreadSafer(display)
    private var isLooping: Boolean = true
    val vLooping = ThreadSafer(isLooping)
    private val manager = WindowManager()
    val windowManager = ThreadSafer(manager)

    fun start(){
        vDisplay.ignore().addDrawable(Drawable(windowManager.ignore()))
        update()
    }

    fun update() {
        while (vLooping.use()) {
            vDisplay.ignore {
                    if (it.limiter.onUpdate())
                            it.update()
            }
        }
    }

    fun end() {
        vLooping.set(false)
    }
}

class WindowManager:IDrawable{
    private val windows:MutableList<OSWindow> = mutableListOf()
    val vWindows = ThreadSafer(windows)
    private val back_drawer = BackGroundDrawer(ImgUtil.fillGen(Color(0,0,0),1920,1080))
    val backGround = ThreadSafer(back_drawer)

    override fun onDraw(p0: Graphics?, p1: Rect?) {
        if(p0==null || p1 == null){
            return
        }else{
            back_drawer.draw(p0,p1)

            vWindows.ignore { i->
                i.forEach {
                    it.onDraw(p0)
                }
            }
        }
    }

    override fun getShowingRect(): Rect = os.display.vDisplay.ignore().displayShowingRect
    override fun isShowing(p0: Display?): Boolean = true
    override fun getID(): String = "OS.WindowManager"



    class BackGroundDrawer(val img: Image) : WindowDrawer() {
        val onFirst:First = First {  }
        override fun draw(g: Graphics, r: Rect) {
            onFirst.invoke {
//                img.zoom()
            }
            g.drawImage(img,0,0,r.width,r.height,null)
        }
    }
}