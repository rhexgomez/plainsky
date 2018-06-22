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

package com.rhexgomez.plainsky.exception

import org.json.JSONObject

open class PlainskyException(val errorCode: Int, message: String? = null) : Exception() {

    override fun toString(): String =
            JSONObject().apply {
                put(ERROR, JSONObject().apply {
                    put(CODE, errorCode.toString())
                    message?.run {
                        put(MESSAGE, this)
                    }
                })
            }.toString()

    companion object {
        private const val ERROR = "error"
        private const val CODE = "code"
        private const val MESSAGE = "message"
    }

}