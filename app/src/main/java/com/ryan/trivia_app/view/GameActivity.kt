/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the game activity for the app.
 */

package com.ryan.trivia_app.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryan.trivia_app.R
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.controller.UIController
import com.ryan.trivia_app.databinding.ActivityGameBinding

/** GameActivity class. All game content will run on top of this activity. */
class GameActivity : AppCompatActivity() {
    /**
     * When the activity is created on screen.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGameBinding.inflate(layoutInflater)
        setTheme(R.style.app)
        setContentView(binding.root)

        // Replaces placeholder FrameLayout with CategoriesFragment
        Transfer().transferToFragment(
            supportFragmentManager, R.id.fragmentPlaceholder, CategoriesFragment()
        )
    }

    /** When the app is refocused. Deprecated but works as intended. */
    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()

        // Makes the app fullscreen (No system bars)
        UIController().fullScreen(window)
    }

    /** On backpress, go back to MainActivity. */
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
