package com.ndsl.os.main

import com.ndsl.os.graphics.OSDisplay
import com.ndsl.os.util.OrderExecutor
import com.ndsl.os.util.OrderManager
import com.ndsl.os.util.ThreadSafer

class OS {
    private val logger:OrderManager = OrderManager(System.out)
    val vLogger = ThreadSafer(logger)
    private val logExecutor : OrderExecutor = OrderExecutor("System",vLogger.use())
    val vLogExecutor = ThreadSafer(logExecutor)
    lateinit var display: OSDisplay

    fun run(args:Array<String>){
        init(args)
        drawThread()
    }

    private fun drawThread() {
        Thread{
            display = OSDisplay()
            display.start()
        }.run()
    }

    fun init(args:Array<String>){
        logExecutor.debug("Hello!")
    }
}