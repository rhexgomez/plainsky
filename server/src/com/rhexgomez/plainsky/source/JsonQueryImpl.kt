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

package com.rhexgomez.plainsky.source

import com.rhexgomez.plainsky.util.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class JsonQueryImpl(uri: String) : JsonQuery {

    val rawJson = JSONTokener(uri.toFileText()).nextValue()

    init {
        when (rawJson) {
            is JSONArray -> {
                transformJsonArray(rawJson)
            }
            is JSONObject -> {
                transformJsonObject(rawJson)
            }
        }
    }

    private fun transformJsonArray(jsonArray: JSONArray) {
        val list = ArrayList<Any>()
        // Within the array find all object that has __count key so that we can replicate a copy.
        jsonArray.iterateObject { anyObject ->
            if (anyObject is JSONObject && anyObject.has(COUNT_KEY)) {
                for (x in 1..anyObject.getInt(COUNT_KEY)) {
                    list.add(anyObject.shallowCopy().apply { remove(COUNT_KEY) })
                }
            } else {
                list.add(anyObject)
            }
        }
        jsonArray.clearAll()
        list.forEach { jsonArray.put(it) }
    }

    private fun transformJsonObject(jsonObject: JSONObject) {
        if (jsonObject.has(COUNT_KEY)) jsonObject.remove(COUNT_KEY)
        jsonObject.keys().forEach {
            it?.let {
                (jsonObject[it.toString()] as? JSONArray)?.let {
                    transformJsonArray(it)
                }
            }
        }
    }

    override fun query(path: String): String? {
        var currentJson: Any? = rawJson
        if (path == "/") return currentJson?.toString()
        path.substring(1).split("/").iterator().let { path ->
            while (path.hasNext()) {
                currentJson = currentJson?.run { move(this, path) }
            }
        }
        return currentJson?.toString()
    }

    private fun move(currentJson: Any, path: Iterator<String>): Any {
        if (currentJson is JSONObject) {
            return currentJson.getWithError(path.next())
        } else if (currentJson is JSONArray) {
            val property = path.next()
            return if (path.hasNext()) {
                currentJson.searchObject(property, value = path.next())
            } else {
                currentJson.createArrayOf(property)
            }
        }
        throw UnsupportedOperationException("Unsupported json value!")
    }

    companion object {
        // A special key that indicates the number of copies of an object within an array.
        private const val COUNT_KEY = "__count"
    }

}