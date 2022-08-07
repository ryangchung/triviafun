/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the controller for CategoriesFragment.
 */

package com.ryan.trivia_app.controller

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.ryan.trivia_app.R
import com.ryan.trivia_app.model.Category
import com.ryan.trivia_app.view.TriviaFragment

/**
 * Controller for CategoriesFragment.
 *
 * @property fragmentManager fragmentManager to transfer fragments.
 * @property buttons array of buttons.
 * @property categories array of categories.
 */
class CategoriesController(
    private val fragmentManager: FragmentManager,
    private val buttons: Array<Button>,
    private val categories: ArrayList<Category>?
) {

    /** Binds the categories to the View. */
    fun bindToView() {
        if (categories != null) {
            // Binds text to buttons
            for (iterator in buttons.indices) {
                buttons[iterator].text = categories[iterator].name
            }
        }
    }

    /** Sets the button onClickListener. */
    fun setOnClickListeners() {
        if (categories != null) {
            /*
                Make sure the app doesn't crash if the user
                spams the button and the transfer process has started.
            */
            var transferred = false
            // setOnClickListener for each of the buttons
            for (iterator in buttons.indices) {
                buttons[iterator].setOnClickListener {
                    // Transitions to TriviaFragment with the chosen category being passed
                    if (!transferred) {
                        toGame(it as Button, categories[iterator])
                    }
                    transferred = true
                }
            }
        }
    }

    /**
     * Initiates a transition and replaces the fragment by TriviaFragment.
     *
     * @param button the button that was clicked by the user.
     * @param category the category to be sent to TriviaFragment.
     */
    private fun toGame(button: Button, category: Category) {
        // Sets chosen button to green
        button.setBackgroundResource(R.drawable.game_button_green_pressed)

        // Executes this code 1 second after the button was set to green
        Handler(Looper.getMainLooper()).postDelayed({
            // Adds the chosen category to the bundle to be sent to TriviaFragment
            val args = Bundle()
            args.putParcelable("category", category)

            val triviaFragment = TriviaFragment()
            triviaFragment.arguments = args

            // Replaces this fragment with TriviaFragment
            Transfer().transferToFragment(
                fragmentManager, R.id.fragmentPlaceholder, triviaFragment
            )
        }, 1000)
    }
}
