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

package com.rhexgomez.plainsky.util

import com.rhexgomez.plainsky.exception.PlainskyFileNotFoundException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Iterate the [JSONArray] containing anonymous objects with the
 * corresponding index. The anonymous can be any of the
 * following : [Integer],[Double],[String],
 * [JSONArray] or [JSONObject]
 */
inline fun JSONArray.iterateWithIndex(func: (Any, Int) -> Unit) {
    for (index in 0 until length()) {
        func(get(index), index)
    }
}

/**
 * Iterate the [JSONArray] containing anonymous objects. The anonymous
 * can be any of the following : [Integer],[Double],[String],
 * [JSONArray] or [JSONObject]
 */
inline fun JSONArray.iterateObject(func: (Any) -> Unit) {
    iterateWithIndex { anonymousObject, _ ->
        func(anonymousObject)
    }
}

/**
 * Clear the entire content of [JSONArray].
 */
fun JSONArray.clearAll() {
    for (index in 0 until length()) {
        remove(0)
    }
}

/**
 * @return true if the [JSONArray] is empty otherwise false.
 */
fun JSONArray.isEmpty() = length() == 0

/**
 * Create a copy of the [JSONObject].
 */
fun JSONObject.shallowCopy() = JSONObject(this, JSONObject.getNames(this))

fun JSONObject.getWithError(key: String): Any =
        try {
            get(key)
        } catch (json: JSONException) {
            PlainskyFileNotFoundException()
        }

fun JSONArray.createArrayOf(key: String): JSONArray {
    val newArray = JSONArray()
    iterateObject {
        (it as? JSONObject)?.run {
            newArray.put(getWithError(key))
        }
    }
    return newArray
}

fun JSONArray.searchObject(key: String, value: String): JSONObject? {
    iterateObject {
        (it as? JSONObject)?.run {
            if (getWithError(key).guaranteeString() == value) return this
        }
    }
    return null
}
