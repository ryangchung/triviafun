/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Category Request class.
 */

package com.ryan.trivia_app.model

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.Html
import android.widget.Button
import com.ryan.trivia_app.R
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.controller.TriviaController
import com.ryan.trivia_app.controller.onLimitedClick
import com.ryan.trivia_app.databinding.FragmentTriviaBinding
import java.net.URL
import java.util.concurrent.Executors
import org.json.JSONObject

/**
 * Class that returns a parsed API response.
 *
 * @property url
 */
class TriviaAPIRequest(private val url: String) {

    /**
     * Get request to a URL.
     *
     * @return JSON as String or null
     */
    fun apiRequest(
        arguments: Bundle,
        buttons: Array<Button>,
        activity: Activity,
        binding: FragmentTriviaBinding,
        context: Context
    ) {
        Executors.newSingleThreadExecutor().execute {
            val questionsArray = try {
                parseQuestions(URL(url).readText())
            } catch (e: Exception) {
                null
            }

            Handler(Looper.getMainLooper()).post {
                val triviaController = TriviaController(binding, activity, context)
                // Gets the passed data from CategoriesFragment
                val category = arguments.getParcelable<Category>("category")
                // Sets the category as the top bar
                binding.txtCategory.text = category!!.name

                // FX setting in persistent storage
                val settings = PreferenceManager.getDefaultSharedPreferences(context)
                val fx = settings.getBoolean("fx", true)

                // Makes sure the API call was successful
                if (questionsArray != null) {
                    // Game attributes
                    // Numbers of questions that have passed
                    var questionCount = 0
                    // Lives remaining
                    var lives = 3

                    // Current question
                    var question = Question("", "", "", "", "")
                    try {
                        question = questionsArray[questionCount]
                    } catch (e: IndexOutOfBoundsException) {
                        Transfer().transferForIssue(activity)
                    }

                    // Show first question
                    triviaController.showQuestion(questionsArray[questionCount], questionCount)

                    // Binds onClickListeners to each of the buttons
                    buttons.forEach { it ->
                        // onClickListeners with a time limit to prevent spam
                        it.onLimitedClick {
                            it as Button

                            // If user won
                            if (questionCount == 49) {
                                /* Add 1 to questionCount as we want the amount
                                   of questions answered, not the index of the array */
                                triviaController.showEndOfGame(true, ++questionCount)
                            } else {
                                // Checks if the user was right
                                val isCorrect = it.text == question.rightAnswer

                                // Disables button presses of other buttons
                                buttons.forEach { button -> button.isEnabled = false }
                                // Shows visually the right and wrong answers
                                triviaController.showAnswers(
                                    it, question.rightAnswer,
                                    triviaController.userIsCorrect(it.text as String, question.rightAnswer)
                                )

                                // Reassigns the next question
                                question = questionsArray[++questionCount]
                                // If user was wrong
                                if (isCorrect) {
                                    // Play right answer sound effect
                                    triviaController.playSoundEffect(fx, R.raw.right, context)

                                    // Show new question
                                    triviaController.newQuestion(buttons, question, questionCount)
                                } else {
                                    // Play wrong answer sound effect
                                    triviaController.playSoundEffect(fx, R.raw.wrong, context)

                                    // Removes a life
                                    --lives
                                    // Updates life indicator
                                    triviaController.showLives(lives)

                                    // Checks whether to end the game or show the next question
                                    if (lives == 0) {
                                        // Minus 3 to questionCount to remove 3 wrong answers
                                        triviaController.showEndOfGame(false, questionCount - 3)
                                    } else {
                                        // Shows next question
                                        triviaController.newQuestion(buttons, question, questionCount)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    /*
                        Goes back to MainActivity and shows a Toast
                        in case there are internet connection issues.
                    */
                    Transfer().transferForIssue(activity)
                }
            }
        }
    }

    /**
     * Parses the question API call response.
     *
     * @param json the returned JSON.
     * @return a parsed ArrayList of all questions.
     */
    private fun parseQuestions(json: String): ArrayList<Question> {
        // Questions to be sent back
        val questionsArray = ArrayList<Question>()
        // Entry point of the JSON data
        val results = JSONObject(json).getJSONArray("results")
        // For every JSONObject inside results
        for (iterator in 0 until results.length()) {
            // Get array of incorrect answers
            val incorrectAnswers = arrayOfNulls<String>(3)
            for (iterator2 in incorrectAnswers.indices) {
                incorrectAnswers[iterator2] = (results[iterator] as JSONObject)
                    .getJSONArray("incorrect_answers")
                    .getString(iterator2)
            }
            // Adds all relevant information to questionsArray
            val jsonObject = results[iterator] as JSONObject
            questionsArray.add(
                Question(
                    htmlToString(jsonObject.getString("question")),
                    htmlToString(jsonObject.getString("correct_answer")),
                    htmlToString(incorrectAnswers[0]!!),
                    htmlToString(incorrectAnswers[1]!!),
                    htmlToString(incorrectAnswers[2]!!),
                )
            )
        }
        return questionsArray
    }

    /**
     * Converts HTML entities.
     *
     * @param text String to decode.
     * @return decoded String with special characters.
     */
    @Suppress("DEPRECATION")
    private fun htmlToString(text: String): String = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(text).toString()
    }
}
