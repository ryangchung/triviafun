/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the controller for SettingsFragment.
 */
package com.ryan.trivia_app.controller

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.ryan.trivia_app.R
import com.ryan.trivia_app.databinding.FragmentSettingsBinding
import com.ryan.trivia_app.view.MenuFragment

/**
 * Controller for SettingsFragment.
 *
 * @property context context of the Fragment.
 * @property binding the binding that allows access to XML components.
 */
class SettingsController(
    private val context: Context,
    private val binding: FragmentSettingsBinding
) {
    // Grabs the settings from persistent storage
    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    // Grabs the FX sound effect setting
    private var fx = settings.getBoolean("fx", true)

    /** Binds color and text to the FX button toggle. */
    fun bindToFXButton() {
        binding.btnFX.text = if (fx) "ON" else "OFF"
        binding.btnFX.setBackgroundResource(
            if (fx) R.drawable.fx_button_green else R.drawable.fx_button_red
        )
    }

    /** Sets onClickListener for the FX button. */
    fun setOnBtnFXClickListener() =
        binding.btnFX.setOnClickListener {
            // fx becomes the opposite of its initial state. Ex: True -> False
            fx = !fx
            // Reflect the changes on the button
            bindToFXButton()

            // Updates the persistent storage to remember the user's choice
            val edit = settings.edit()
            edit.putBoolean("fx", fx)
            edit.apply()
        }

    /**
     * Opens browser on click.
     *
     * @param button the button.
     * @param activity the activity associated with the fragment.
     * @param url url to map the button to.
     */
    fun openBrowserOnClick(button: Button, activity: Activity, url: String) =
        button.setOnClickListener { Transfer().transferToBrowser(activity, url) }

    /**
     * Listens for a btnBack press.
     *
     * @param fragmentManager the fragmentManage associated with SettingsFragment.
     */
    fun onBackPressListener(fragmentManager: FragmentManager) {
        binding.btnBack.setOnClickListener {
            Transfer().transferToFragment(
                fragmentManager, R.id.fragmentPlaceholder, MenuFragment()
            )
        }
    }
}
