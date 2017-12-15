package frc.team4330.robot.server

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class ServerTest {
    internal var thread: Thread? = null
    //	public static void main(String[] args) throws IOException {
    //		start();
    //	}

    @Throws(IOException::class)
    fun start() {
        val portNumber = 9003
        val listener = ServerSocket(portNumber)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (true) {
                        val socket = listener.accept()
                        println("server on")
                        try {
                            val out = PrintWriter(socket.getOutputStream(), true)
                            val `in` = BufferedReader(
                                    InputStreamReader(socket.getInputStream()))

                            var input: String = `in`.readLine()
                            var output: String?
                            println("we halfway there")
                            val proto = ServerProtocol()

                            while (!input.isNullOrBlank()) {
                                output = proto.processDataRequests(input)
                                println("input is " + input)
                                println("output is " + output!!)

                                out.println(output)

                            }
                        } finally {
                            socket.close()
                        }
                    }
                } catch (e: Exception) {
                } finally {
                    try {
                        listener.close()
                    } catch (e: IOException) {
                    }

                }
            }
        }

        thread!!.start()
    }

    @Throws(IOException::class)
    fun stop() {
        if (thread != null)
            thread!!.interrupt()
    }
}
