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

package com.rhexgomez.plainsky.converter

import java.io.Closeable
import java.io.InputStream

class HttpReader(private val inputStream: InputStream) : Closeable {

    override fun close() {
        inputStream.close()
    }

    private lateinit var _type: String
    private lateinit var _path: String
    val type: String
        get() = _type
    val path: String
        get() = _path

    init {
        inputStream.bufferedReader().readLine().split(" ").let {
            _type = it[0]
            _path = it[1]
        }
    }
}