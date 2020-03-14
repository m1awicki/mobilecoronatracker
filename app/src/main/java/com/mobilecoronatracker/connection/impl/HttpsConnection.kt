package com.mobilecoronatracker.connection.impl

import com.mobilecoronatracker.connection.HttpsConnectionFacade
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpsConnection : HttpsConnectionFacade {
    private var connection: HttpsURLConnection? = null

    override fun connect(url: String) {
        try {
            val _url = URL(url)
            connection?.disconnect()
            connection = _url.openConnection() as HttpsURLConnection
            connection?.requestMethod = "GET"
            connection?.setRequestProperty("Accept", "application/json")
            connection?.setRequestProperty("User-Agent", "")
            //"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            if (connection?.getResponseCode() != 200) {
                throw RuntimeException("Failed : HTTP error code : "
                        + connection?.responseCode)
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: ProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getInput(): String? {
        connection?.apply {
            val builder = StringBuilder()
            val br = BufferedReader(InputStreamReader((inputStream)))
            var output = br.readLine()
            while (output != null) {
                builder.append(output)
                output = br.readLine()
            }
            return builder.toString()
        }
        return null
    }

    override fun getInputStream(): InputStream? = connection?.inputStream

    override fun disconnect() {
        connection?.disconnect()
    }
}