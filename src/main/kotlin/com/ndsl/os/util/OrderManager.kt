package com.ndsl.os.util

import java.io.PrintStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * This class is managing where/what the OS is doing.
 */
class OrderManager(out: PrintStream) {
    var executors: MutableList<OrderExecutor> = mutableListOf()
    var timeStamp: Boolean = false

    fun register(executor: OrderExecutor) {
        executors.add(executor)
    }

    fun out(string: String, executor: OrderExecutor) {
        if (timeStamp)
            println("[${executor.name}](${LocalDate.now().format(DateTimeFormatter.ISO_TIME)})$string")
        else
            println("[${executor.name}]$string")
    }
}

class OrderExecutor(val name:String, val manager: OrderManager) {
    fun out(s: String) {
        manager.out(s, this)
    }

    fun info(s: String) {
        manager.out("[INFO]$s", this)
    }

    fun error(s: String) {
        manager.out("[ERROR]$s", this)
    }

    fun debug(s: String) {
        manager.out("[DEBUG]$s", this)
    }
}