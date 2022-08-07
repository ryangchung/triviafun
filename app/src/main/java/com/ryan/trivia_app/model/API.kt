/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * API class that contains functions related to API requests and parsing.
 */

package com.ryan.trivia_app.model

import java.net.URL

/** API-related class. */
class API {
    /**
     * Get request to a URL.
     *
     * @param url to make a GET request
     * @return JSON as String or null
     */
    fun request(url: String): String? = try {
        URL(url).readText()
    } catch (e: Exception) {
        null
    }
}
