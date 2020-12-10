package com.ndsl.os.util

import com.ndsl.os.main.os

class ThreadSafer<T>(private var t: T) {
    private var locked: Boolean = false

    /**
     * Use this when you access across threads
     */
    fun use(func: (ts: T) -> Unit) {
        while (locked){
            Thread.sleep(0, 1)
            os.vLogExecutor.use().debug("Waiting")
        }
        locked = true
        func.invoke(t)
        locked = false
    }

    fun use(): T {
        var copy: T? = null
        use {
            copy = it
        }
        return copy!!
    }

    fun set(t:T) {
        use{
            this.t = t
        }
    }

    /**
     * Use this when you use from the same(or main) thread
     */
    fun ignore(func: (ts: T) -> Unit) {
        func.invoke(t)
    }

    fun ignore(): T = t
}