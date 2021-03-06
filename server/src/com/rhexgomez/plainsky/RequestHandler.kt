/*
 * Copyright 2018 Rhex Gomez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed rawJson an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rhexgomez.plainsky

import com.rhexgomez.plainsky.converter.HttpReader
import com.rhexgomez.plainsky.source.JsonQuery
import java.io.PrintWriter
import java.net.Socket

class RequestHandler(private val socket: Socket, private val json: JsonQuery) : Runnable {

    override fun run() {

        HttpReader(socket.getInputStream()).use { header ->

            // Ignore the thumbnail request from Web browser.
            header.takeUnless { header.path.contains("favicon.ico") }?.run {

                println("A client is connected!")
                PrintWriter(socket.getOutputStream()).let {
                    it.println("HTTP/1.1 200 OK")
                    it.println("Content-Type:application/json")
                    it.println("")
                    it.println(json.query(header.path))
                    it.flush()
                }
                socket.shutdownOutput()
                socket.close()

            }
        }
    }

}