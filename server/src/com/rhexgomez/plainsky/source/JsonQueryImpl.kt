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

package com.rhexgomez.plainsky.source

import com.rhexgomez.plainsky.util.forEachObject
import com.rhexgomez.plainsky.util.guaranteeString
import com.rhexgomez.plainsky.util.toFileText
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class JsonQueryImpl(uri: String) : JsonQuery {

    val on: Any? = JSONTokener(uri.toFileText()).nextValue()

    override fun query(path: String): String? {
        var currentJson: Any? = on

        // Skip the query process immediately
        if (path == "/") return currentJson?.toString()

        path.substring(1).split("/").iterator().let {
            while (it.hasNext()) {
                currentJson?.run {
                    when (this) {
                        is JSONObject -> currentJson = get(it.next())
                        is JSONArray -> {

                            // Treat the next path as query filter for the array of Objects.
                            // It will only obtain the first item that matches our condition,
                            // sometimes it is null.
                            if (it.hasNext()) {
                                val property = it.next()
                                val value = it.next()

                                run breaker@{

                                    forEachObject { jsonObject ->

                                        if (jsonObject.get(property).guaranteeString() == value) {
                                            currentJson = jsonObject
                                            return@breaker
                                        }
                                    }
                                    currentJson = null
                                }
                            }
                        }
                    }
                }
            }
        }

        return currentJson?.toString()
    }
}