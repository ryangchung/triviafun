/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Fragment that is attached to MainActivity when btnSettings is clicked in MenuFragment.
 * Displays settings and credits.
 */

package com.ryan.trivia_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryan.trivia_app.controller.SettingsController
import com.ryan.trivia_app.databinding.FragmentSettingsBinding

/** SettingsFragment class, where the settings and credits are located inside MainActivity. */
class SettingsFragment : Fragment() {
    /** Binding to access XML components. */
    private var _binding: FragmentSettingsBinding? = null

    /** Binding getter. */
    private val binding get() = _binding!!

    /**
     * After the view is created with binding, create onClickListeners.
     *
     * @param view the view with the inflated layout.
     * @param savedInstanceState Bundle holding instanceState.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Controller
        val settingsController = SettingsController(requireContext(), binding)
        // Binds button state to View
        settingsController.bindToFXButton()
        // Sets onClickListener
        settingsController.setOnBtnFXClickListener()

        // Opens up the user's default browser, displays the API used for this app
        settingsController.openBrowserOnClick(
            binding.btnAPI, requireActivity(), "https://opentdb.com/"
        )

        // Opens up the user's default browser, used API's license
        settingsController.openBrowserOnClick(
            binding.btnLicense, requireActivity(), "https://creativecommons.org/licenses/by-sa/4.0/"
        )

        // Transfers back to MenuFragment
        settingsController.onBackPressListener(parentFragmentManager)
    }

    /**
     * When the view is created.
     *
     * @param inflater XML reader.
     * @param container contains other views.
     * @param savedInstanceState Bundle holding instanceState.
     * @return the view for binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }
}
