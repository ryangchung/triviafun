/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Fragment that is attached to GameActivity during the game loop.
 * The game is played in this fragment.
 */

package com.ryan.trivia_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.databinding.FragmentTriviaBinding
import com.ryan.trivia_app.model.Category
import com.ryan.trivia_app.model.TriviaAPIRequest

/** TriviaFragment class, this is the in-game loop. */
class TriviaFragment : Fragment() {
    /** Binding to access XML components. */
    private var _binding: FragmentTriviaBinding? = null

    /** Binding getter. */
    private val binding get() = _binding!!

    /**
     * After the view is created with binding, create onClickListeners
     *
     * @param view the view with the inflated layout
     * @param savedInstanceState Bundle holding instanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Array of buttons
        val buttons = arrayOf(
            binding.btnAnswer1, binding.btnAnswer2,
            binding.btnAnswer3, binding.btnAnswer4
        )

        val category = requireArguments().getParcelable<Category>("category")
        if (category != null) {
            TriviaAPIRequest(
                "https://opentdb.com/api.php?amount=50&category=" + category.id + "&type=multiple"
            ).apiRequest(requireArguments(), buttons, requireActivity(), binding, requireContext())
        } else {
            Transfer().transferForIssue(requireActivity())
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
        _binding = FragmentTriviaBinding.inflate(layoutInflater)
        return binding.root
    }
}
