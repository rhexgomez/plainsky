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

import java.lang.StringBuilder
import java.util.stream.Stream

fun Stream<String>.concatinate(): String {
    val builder = StringBuilder()
    forEach { builder.append(it) }
    return builder.toString()
}