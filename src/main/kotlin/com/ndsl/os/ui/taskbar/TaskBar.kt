package com.ndsl.os.ui.taskbar

import com.ndsl.graphics.display.Display
import com.ndsl.graphics.display.drawable.IDrawable
import com.ndsl.graphics.display.mouse.MouseInputHandler
import com.ndsl.graphics.pos.Rect
import com.ndsl.os.main.os
import com.ndsl.os.util.ThreadSafer
import com.ndsl.os.util.copy
import com.ndsl.os.util.fillRect
import java.awt.Color
import java.awt.Graphics
import java.util.*

class TaskBar {
    private val list: MutableList<TaskBarObject> = mutableListOf()
    private var showing = true

    val objects = ThreadSafer(list)
    val isShowing = ThreadSafer(showing)


    fun add(obj: TaskBarObject) {
        objects.ignore().add(obj)
    }

    /**
     * When the display update,this will be called.
     * this plays the role to hook TaskBarObject#onClick
     * @see TaskBarObject.onClick
     */
    fun update(r:Rect) {
        if (os.display.vDisplay.use().mouseInputHandler.isClicking) {
            if (r.contain(os.display.vDisplay.use().mouseInputHandler.getNow_mouse_pos())) {
                objects.ignore().forEach {
                    if (it.bounds.copy().shift(-r.left_up.x, -r.left_up.y)
                            .contain(os.display.vDisplay.use().mouseInputHandler.getNow_mouse_pos())
                    ) {
                        it.onClick(os.display.vDisplay.use().mouseInputHandler)
                    }
                }
            }
        }
    }

    /**
     * The method to call all (IDrawable)TaskBarObject#onDraw
     * @see TaskBarObject.onDraw
     */
    fun draw(p0: Graphics, p1: Rect) {
        objects.ignore().forEach {
            it.onDraw(
                p0, Rect(
                    p1.left_up.x + it.bounds.left_up.x,
                    p1.left_up.y + it.bounds.left_up.y,
                    p1.left_up.x + it.bounds.width,
                    p1.left_up.y + it.bounds.height
                )
            )
        }
    }
}

abstract class TaskBarObject : IDrawable {
    /**
     * @return The location where this object is in the taskbar.
     * @note TaskBar left up is (0,0)
     */
    abstract var bounds: Rect

    /**
     * @call on clicked in the taskbar
     */
    abstract fun onClick(e: MouseInputHandler)
    override fun isShowing(p0: Display?): Boolean = os.taskBar.ignore().isShowing.ignore()
    override fun getShowingRect(): Rect = bounds
}


class Clock : TaskBarObject() {
    override var bounds: Rect = Rect(
        os.display.vDisplay.use().width - 50,
        os.display.vDisplay.use().height - 40,
        os.display.vDisplay.use().width,
        os.display.vDisplay.use().height
    )
    val clock:Calendar = Calendar.getInstance()

    override fun onClick(e: MouseInputHandler) {
        os.vLogExecutor.ignore().info("Clock Clicked!")
    }

    override fun onDraw(p0: Graphics?, p1: Rect?) {
        if (p0 == null || p1 == null) return
        p0.color = Color.GREEN
        p0.drawString(getString(), p1.left_up.x, p1.left_up.y + 25)
    }

    fun getString() = "${clock.get(Calendar.HOUR_OF_DAY)}:${clock.get(Calendar.MINUTE)}"

    override fun getID(): String = "taskbar.clock"
}