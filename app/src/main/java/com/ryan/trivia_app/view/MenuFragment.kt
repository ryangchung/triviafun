/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Fragment that is attached to MainActivity by default, and the user can decide to start a game,
 * change settings and look at the leaderboard.
 */

package com.ryan.trivia_app.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryan.trivia_app.R
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.databinding.FragmentMenuBinding

/** MenuFragment class, the main menu inside MainActivity. */
class MenuFragment : Fragment() {
    /** Binding to access XML components. */
    private var _binding: FragmentMenuBinding? = null

    /** Binding getter. */
    private val binding get() = _binding!!

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
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * After the view is created with binding, create onClickListeners.
     *
     * @param view the view with the inflated layout.
     * @param savedInstanceState Bundle holding instanceState.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // onClickListener to start the game
        binding.btnStart.setOnClickListener {
            startActivity(Intent(activity, GameActivity::class.java))
        }

        // onClickListener to transfer to the LeaderboardFragment
        binding.btnLeaderboard.setOnClickListener {
            Transfer().transferToFragment(
                parentFragmentManager, R.id.fragmentPlaceholder,
                LeaderboardFragment(), Pair(true, "leaderboard")
            )
        }

        // onClickListener to transfer to the SettingsFragment
        binding.btnSettings.setOnClickListener {
            Transfer().transferToFragment(
                parentFragmentManager, R.id.fragmentPlaceholder,
                SettingsFragment(), Pair(true, "settings")
            )
        }
    }
}
