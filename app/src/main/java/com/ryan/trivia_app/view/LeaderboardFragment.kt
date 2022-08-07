/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Fragment that is attached to MainActivity when btnLeaderboard is clicked in MenuFragment.
 * Displays the local leaderboard.
 */

package com.ryan.trivia_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryan.trivia_app.R
import com.ryan.trivia_app.controller.LeaderboardController
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.databinding.FragmentLeaderboardBinding

class LeaderboardFragment : Fragment() {
    /** Binding to access XML components. */
    private var _binding: FragmentLeaderboardBinding? = null

    /** Binding getter. */
    private val binding get() = _binding!!

    /**
     * When the View is created.
     *
     * @param view Fragment View.
     * @param savedInstanceState saved instance state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Controller
        val leaderboardController = LeaderboardController(requireActivity(), binding)
        // Binds leaderboard to View
        leaderboardController.bindLeaderboard()

        // Transfers back to MenuFragment
        binding.btnBack.setOnClickListener {
            Transfer().transferToFragment(
                parentFragmentManager, R.id.fragmentPlaceholder, MenuFragment()
            )
        }
    }

    /**
     * When the view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view for binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLeaderboardBinding.inflate(layoutInflater)
        return binding.root
    }
}
