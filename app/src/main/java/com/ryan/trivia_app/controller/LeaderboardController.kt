/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the controller for LeaderboardFragment.
 */

package com.ryan.trivia_app.controller

import android.app.Activity
import android.preference.PreferenceManager
import com.ryan.trivia_app.R
import com.ryan.trivia_app.databinding.FragmentLeaderboardBinding
import org.json.JSONArray
import org.json.JSONException

/**
 * Controller for LeaderboardFragment.
 *
 * @property activity the activity LeaderboardFragment came from.
 * @property binding the binding that allows access to XML components.
 */
class LeaderboardController(
    private val activity: Activity,
    private val binding: FragmentLeaderboardBinding
) {
    /** Binds leaderboard stored in persistent storage to the View. */
    fun bindLeaderboard() {
        // Leaderboard in persistent storage
        val prefLeaderboard = PreferenceManager.getDefaultSharedPreferences(activity)
        val jsonLeaderboard = try {
            JSONArray(
                prefLeaderboard.getString(
                    "jsonLeaderboard", "empty"
                )
            )
        } catch (e: JSONException) {
            null
        }
        // Leaderboard of nulls
        val leaderboard = arrayOfNulls<String>(10)
        // Populate leaderboard with leaderboard in persistent memory
        if (jsonLeaderboard != null) {
            for (iterator in leaderboard.indices) {
                try {
                    leaderboard[iterator] = jsonLeaderboard.getString(iterator)
                } catch (e: JSONException) {
                    leaderboard[iterator] = null
                }
            }

            // Update text
            binding.txtLeaderboard.text = activity.getString(
                R.string.leaderboard_text,
                leaderboard[0], leaderboard[1], leaderboard[2],
                leaderboard[3], leaderboard[4], leaderboard[5],
                leaderboard[6], leaderboard[7], leaderboard[8], leaderboard[9]
            )
        } else {
            "Play a game to start off the leaderboard!".also { binding.txtLeaderboard.text = it }
        }
    }
}
