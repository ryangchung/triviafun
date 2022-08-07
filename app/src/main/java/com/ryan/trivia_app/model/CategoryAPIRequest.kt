/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Category Request class.
 */

package com.ryan.trivia_app.model

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.ryan.trivia_app.controller.CategoriesController
import com.ryan.trivia_app.controller.Transfer
import java.net.URL
import java.util.concurrent.Executors
import org.json.JSONObject

/**
 * Class that returns a parsed API response.
 *
 * @property url
 */
class CategoryAPIRequest(private val url: String) {

    /**
     * Get request to a URL.
     *
     * @return JSON as String or null
     */
    fun apiRequest(fragmentManager: FragmentManager, buttons: Array<Button>, activity: Activity) {
        Executors.newSingleThreadExecutor().execute {
            val categories = try {
                parseCategories(URL(url).readText())
            } catch (e: Exception) {
                null
            }

            Handler(Looper.getMainLooper()).post {
                // Controller
                val categoriesController = CategoriesController(fragmentManager, buttons, categories)

                if (categories != null) {
                    // Binds text to buttons
                    categoriesController.bindToView()
                    categoriesController.setOnClickListeners()
                } else {
                    /*
                       Goes back to MainActivity and shows a Toast
                       in case there are internet connection issues.
                    */
                    Transfer().transferForIssue(activity)
                    activity.overridePendingTransition(0, 0)
                }
            }
        }
    }

    /**
     * Parses categories API response.
     *
     * @param json as String.
     * @return Categories.
     */
    private fun parseCategories(json: String): ArrayList<Category> {
        // All categories from API
        val allCategories = ArrayList<Category>()
        // Used categories from API
        val usedCategories = ArrayList<Category>()
        // JSONArray of categories
        val jsonArray = JSONObject(json).getJSONArray("trivia_categories")
        // Parse loop of JSONObjects inside of JSONArray
        for (iterator in 0 until jsonArray.length()) {
            // Name of category
            val name = (jsonArray[iterator] as JSONObject).getString("name")
            // Adds id and name
            if (categoryIsValid((jsonArray[iterator] as JSONObject).getInt("id"))) {
                allCategories.add(
                    Category(
                        (jsonArray[iterator] as JSONObject).getInt("id"),
                        // Filters unnecessary substrings
                        when {
                            name.startsWith("Entertainment: Japanese ") -> name.drop(24)
                            name.startsWith("Entertainment: ") -> name.drop(15)
                            else -> name
                        }
                    )
                )
            }
        }

        // Random unique numbers
        val randomList = (0 until allCategories.size).shuffled().take(4)
        for (randNum: Int in randomList) { usedCategories.add(allCategories[randNum]) }

        return usedCategories
    }

    /**
     * Checks if a category has enough questions (50) for a full game
     *
     * @param id the category ID
     * @return returns if it's valid, or null if there is an internet error
     */
    private fun categoryIsValid(id: Int): Boolean {
        val json = API().request(
            "https://opentdb.com/api.php?amount=50&category=$id&type=multiple"
        )
        // Need 50 questions for a full game
        return if (json != null) {
            JSONObject(json).getInt("response_code") == 0
        } else {
            false
        }
    }
}
