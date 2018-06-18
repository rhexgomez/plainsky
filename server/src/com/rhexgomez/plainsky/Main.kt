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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rhexgomez.plainsky

import com.rhexgomez.plainsky.source.JsonQueryImpl
import java.net.ServerSocket

private const val DEFAULT_PORT = 2235

fun main(args: Array<String>) {

    println("The server is currently running at port : 2235!")

    IO.serverIO.execute {

        val jsonString = JsonQueryImpl(args[0])

        ServerSocket(DEFAULT_PORT).use {
            while (true) {
                IO.requestIO.execute(RequestHandler(it.accept(), jsonString))
            }
        }
    }

}