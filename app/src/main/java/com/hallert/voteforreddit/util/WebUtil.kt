package com.hallert.voteforreddit.util

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class WebUtil {
    companion object {
        fun isOnline(): Boolean {
            return try {
                val timeoutMs = 1500
                val sock = Socket()
                val sockaddr = InetSocketAddress("8.8.8.8", 53)
                sock.connect(sockaddr, timeoutMs)
                sock.close()
                true
            } catch (e: IOException) {
                false
            }
        }
    }
}