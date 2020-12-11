package com.ndsl.os.main

import com.ndsl.os.graphics.OSDisplay
import com.ndsl.os.ui.taskbar.Clock
import com.ndsl.os.ui.taskbar.TaskBar
import com.ndsl.os.util.OrderExecutor
import com.ndsl.os.util.OrderManager
import com.ndsl.os.util.ThreadSafer

class OS {
    private val logger:OrderManager = OrderManager(System.out)
    val vLogger = ThreadSafer(logger)
    private val logExecutor : OrderExecutor = OrderExecutor("System",vLogger.use())
    val vLogExecutor = ThreadSafer(logExecutor)
    var display: OSDisplay = OSDisplay()
    private val bar = TaskBar()
    val taskBar = ThreadSafer(bar)
    private var isLooping: Boolean = true
    val vLooping = ThreadSafer(isLooping)

    fun run(args:Array<String>){
        init(args)
        drawThread()
        main()
    }

    private fun drawThread() {
        Thread{
            display.start()
        }.run()
    }

    fun init(args:Array<String>){
        logExecutor.debug("Hello!")
        taskBar.ignore().add(Clock())
    }

    fun main(){
        while(vLooping.ignore()){
            Thread.sleep(0,1)
        }
    }
}