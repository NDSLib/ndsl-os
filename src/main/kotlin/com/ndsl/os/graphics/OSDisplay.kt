package com.ndsl.os.graphics

import com.ndsl.graphics.display.BorderLessDisplay
import com.ndsl.graphics.display.Display
import com.ndsl.graphics.display.drawable.IDrawable
import com.ndsl.graphics.display.drawable.base.Drawable
import com.ndsl.graphics.display.util.ExitAttitude
import com.ndsl.graphics.pos.Rect
import com.ndsl.os.graphics.window.OSWindow
import com.ndsl.os.graphics.window.WindowDrawer
import com.ndsl.os.main.os
import com.ndsl.os.ui.taskbar.TaskBar
import com.ndsl.os.util.*
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import kotlin.system.exitProcess

class OSDisplay {
    private val display = BorderLessDisplay("NDSL-OS", 3, Rect(0, 0, 1920, 1080))
    private var isLooping: Boolean = true
    private lateinit var manager:WindowManager
    val vDisplay = ThreadSafer(display)
    val vLooping = ThreadSafer(isLooping)
    lateinit var windowManager:ThreadSafer<WindowManager>

    fun start() {
        manager = WindowManager()
        windowManager = ThreadSafer(manager)
        vDisplay.ignore().exitManager.add {
            vLooping.set(false)
            os.vLooping.set(false)
        }
        vDisplay.ignore().addDrawable(Drawable(windowManager.ignore()))
        update()
    }

    fun update() {
        while (vLooping.ignore()) {
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

class WindowManager : IDrawable {
    private val windows: MutableList<OSWindow> = mutableListOf()
    private val back_drawer = BackGroundDrawer(ImgUtil.fillGen(Color(25, 100, 255), 1920, 1080))
    private val task_drawer = TaskBarDrawer(
        os.taskBar, Rect(
            0,
            os.display.vDisplay.ignore().height - 40,
            os.display.vDisplay.ignore().width,
            os.display.vDisplay.ignore().height
        )
    )

    val vWindows = ThreadSafer(windows)
    val backGround = ThreadSafer(back_drawer)
    val taskBar = ThreadSafer(task_drawer)

    override fun onDraw(p0: Graphics?, p1: Rect?) {
        if (p0 == null || p1 == null) {
            return
        } else {
            back_drawer.draw(p0, p1)
            task_drawer.onDraw(p0, p1)
            vWindows.ignore { i ->
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
        val onFirst: First = First { }
        override fun draw(g: Graphics, r: Rect) {
            onFirst.invoke {
//                img.zoom()
            }
            g.drawImage(img, 0, 0, r.width, r.height, null)
        }
    }

    class TaskBarDrawer(var bar: ThreadSafer<TaskBar>, var r: Rect) : IDrawable {

        override fun onDraw(p0: Graphics?, p1: Rect?) {
            if (p0 == null || p1 == null) return
            p0.color = Color.GRAY
            p0.fillRect(r)
            bar.use().update(r)
            bar.use().draw(p0, p1)
        }

        override fun getShowingRect(): Rect = r
        override fun isShowing(p0: Display?): Boolean = os.taskBar.use().isShowing.use()
        override fun getID(): String = "os.taskBarDrawer"
    }
}