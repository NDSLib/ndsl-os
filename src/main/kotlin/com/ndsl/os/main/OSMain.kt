package com.ndsl.os.main

/**
 * OS Static Instance
 * @note Not Thread Safe!
 */
var os:OS = OS()
fun main(args:Array<String>){
    println("OS-Start-UP")
    os.run(args)
}