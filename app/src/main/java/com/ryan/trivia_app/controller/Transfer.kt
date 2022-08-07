/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Class containing functions related to transferring to different fragments and activities.
 */

package com.ryan.trivia_app.controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ryan.trivia_app.view.MainActivity

/** Transfer class, deals with all the transfers within the app. */
class Transfer {
    /**
     * Replaces the placeholder FrameLayout with a Fragment.
     *
     * @param fragmentManager the activities' fragmentManager.
     * @param start placeholder FrameLayout.
     * @param end fragment to be attached.
     * @param backStack if the fragment should be placed in the backstack hierarchy.
     */
    fun transferToFragment(
        fragmentManager: FragmentManager,
        start: Int,
        end: Fragment,
        backStack: Pair<Boolean, String>? = null
    ) {
        val transition = fragmentManager.beginTransaction().replace(start, end)
        if (backStack?.first == true) {
            transition.addToBackStack(backStack.second)
        }
        transition.commit()
    }

    /**
     * Transfer to the user's main browser.
     *
     * @param activity the activity holding the fragment.
     * @param link the link to load in the user's browser.
     */
    fun transferToBrowser(activity: Activity, link: String) =
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    /**
     * Transfer to MainActivity if there are any internet or parsing issues.
     *
     * @param activity the activity holding the fragment.
     */
    fun transferForIssue(activity: Activity) =
        activity.startActivity(
            Intent(activity, MainActivity::class.java).putExtra(
                "error",
                "Something went wrong. Please check your internet connection" +
                    " and try again shortly."
            )
        )
}
