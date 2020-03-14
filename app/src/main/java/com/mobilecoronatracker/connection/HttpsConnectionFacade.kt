package com.mobilecoronatracker.connection

import java.io.InputStream

interface HttpsConnectionFacade {
    fun connect(url: String)
    fun getInput(): String?
    fun getInputStream(): InputStream?
    fun disconnect()
}