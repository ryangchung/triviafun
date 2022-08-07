/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the controller for TriviaFragment.
 */

package com.ryan.trivia_app.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import com.ryan.trivia_app.R
import com.ryan.trivia_app.databinding.FragmentTriviaBinding
import com.ryan.trivia_app.model.Question
import com.ryan.trivia_app.view.MainActivity
import kotlin.concurrent.thread
import org.json.JSONArray
import org.json.JSONException

/**
 * Controller for TriviaFragment.
 *
 * @property binding the binding that allows access to XML components.
 * @property activity the activity related to TriviaFragment.
 * @property context the context related to TriviaFragment.
 */
class TriviaController(
    private val binding: FragmentTriviaBinding,
    private val activity: Activity,
    private val context: Context
) {
    /**
     * Returns if the user is correct.
     *
     * @param buttonText text on the clicked button.
     * @param rightAnswer the right answer.
     * @return if the user is correct.
     */
    fun userIsCorrect(buttonText: String, rightAnswer: String): Boolean = buttonText == rightAnswer

    /**
     * Plays a given sound effect.
     *
     * @param fx if the setting is enabled.
     * @param sound the sound effect.
     * @param context the context related to TriviaFragment.
     */
    fun playSoundEffect(fx: Boolean, sound: Int, context: Context) {
        if (fx) {
            thread {
                val soundEffect =
                    MediaPlayer.create(context, sound)
                soundEffect.setVolume(1.5f, 1.5f)
                soundEffect.start()
                soundEffect.release()
            }
        }
    }

    /**
     * Binds the question to the view.
     *
     * @param question the current question to bind.
     * @param questionCount how many questions the user has answered.
     */
    fun showQuestion(
        question: Question,
        questionCount: Int
    ) {
        // Shuffles the answers
        val answers = arrayOf(
            question.rightAnswer,
            question.wrongAnswer1,
            question.wrongAnswer2,
            question.wrongAnswer3
        )
        answers.shuffle()

        // Array of buttons
        val buttons = arrayOf(
            binding.btnAnswer1, binding.btnAnswer2, binding.btnAnswer3, binding.btnAnswer4
        )
        // Binds the answers to the buttons' text
        for (iterator in buttons.indices) {
            buttons[iterator].text = answers[iterator]
        }

        // Updates the question indicator
        "Question ${questionCount + 1}".also { binding.txtQuestionCount.text = it }
        // Updates the question
        binding.txtQuestion.text = question.question
    }

    /**
     * Shows the right and wrong answers visually to the user.
     *
     * @param button the button that the user pressed.
     * @param rightAnswer the right answer.
     * @param userRight is the user right.
     */
    fun showAnswers(
        button: Button,
        rightAnswer: String,
        userRight: Boolean,
    ) {
        // Array of buttons
        val buttons = arrayOf(
            binding.btnAnswer1, binding.btnAnswer2, binding.btnAnswer3, binding.btnAnswer4
        )

        // If the user is right, make the button green
        if (userRight) {
            button.setBackgroundResource(R.drawable.game_button_green_pressed)
        } else {
            // If the user is wrong, make the button red
            button.setBackgroundResource(R.drawable.game_button_red)

            // Finds the right answer within the buttons and delete
            for (buttonElement: Button in buttons) {
                if (buttonElement.text == rightAnswer) {
                    buttonElement.setBackgroundResource(R.drawable.game_button_green_unpressed)
                }
            }
        }
    }

    /**
     * Binds the new question to the fragment.
     *
     * @param buttons array of buttons.
     * @param question the new question to bind.
     * @param questionCount how many questions the user has answered.
     */
    fun newQuestion(
        buttons: Array<Button>,
        question: Question,
        questionCount: Int
    ) {
        // 1 second delay until next question is shown
        Handler(Looper.getMainLooper()).postDelayed({
            // Set back default drawable
            binding.btnAnswer1.setBackgroundResource(R.drawable.default_button)
            binding.btnAnswer2.setBackgroundResource(R.drawable.default_button)
            binding.btnAnswer3.setBackgroundResource(R.drawable.default_button)
            binding.btnAnswer4.setBackgroundResource(R.drawable.default_button)
            // Show new question
            showQuestion(question, questionCount)
            // Return ability to click buttons
            buttons.forEach { button -> button.isEnabled = true }
        }, 1000)
    }

    /**
     * Show the end of game screen to the user.
     *
     * @param win whether the user won or not.
     * @param score the number of right answers.
     */
    fun showEndOfGame(win: Boolean, score: Int) {
        // 1 second delay until end of game is shown
        Handler(Looper.getMainLooper()).postDelayed({
            // Makes end of game screen visible
            binding.endOfGame.visibility = View.VISIBLE
            // Sets the header to if the user won or not
            binding.txtWinOrLose.text = if (win) "You won!" else "Game Over"
            // Display score
            binding.txtScore.text = activity.getString(R.string.score, score)
            // Update leaderboard in persistent storage
            updateLeaderboard(score)

            // Button to go back to MainActivity
            binding.btnMainMenu.setOnClickListener {
                activity.startActivity(Intent(context, MainActivity::class.java))
                activity.finish()
            }
        }, 1000)
    }

    /**
     * Updates the life indicator.
     *
     * @param lives the amount of lives left.
     */
    fun showLives(lives: Int) = when (lives) {
        2 -> binding.life1.setBackgroundResource(R.drawable.life_circle_lost)
        1 -> binding.life2.setBackgroundResource(R.drawable.life_circle_lost)
        else -> binding.life3.setBackgroundResource(R.drawable.life_circle_lost)
    }

    /**
     * Updates the leaderboard inside of persistent storage.
     *
     * @param score  the number of right answers.
     */
    private fun updateLeaderboard(score: Int) {
        // Leaderboard in persistent storage
        val prefLeaderboard = PreferenceManager.getDefaultSharedPreferences(context)
        // JSONArray from the String in persistent storage
        val jsonLeaderboard = try {
            JSONArray(prefLeaderboard.getString("jsonLeaderboard", "empty"))
        } catch (e: JSONException) {
            null
        }

        // Creates an ArrayList from the JSONArray
        val leaderboard = ArrayList<Int>()
        if (jsonLeaderboard != null) {
            for (iterator in 0 until jsonLeaderboard.length()) {
                leaderboard.add(jsonLeaderboard.getInt(iterator))
            }
        }

        // Sorts and removes any extra entries so the size of the leaderboard doesn't exceed 10
        leaderboard.add(score)
        leaderboard.sortDescending()
        if (leaderboard.size > 10) {
            leaderboard.remove(11)
        }

        // Applies it to persistent storage
        val edit = prefLeaderboard.edit()
        edit.putString("jsonLeaderboard", JSONArray(leaderboard).toString())
        edit.apply()
    }
}
