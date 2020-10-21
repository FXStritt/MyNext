package com.example.mynext.fragments

import android.view.MotionEvent
import androidx.navigation.NavController

class SwipeGestureNavigator(private val navControllerReference: NavController) {

    private companion object {
        private const val MIN_SWIPE = 200  //MIN_SWIPE is hard coded but should be pixel independent
    }

    private var startX : Float = 0f

    fun navigateUpIfSwipeRight(motionEvent: MotionEvent) {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.x
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            val swipeDistance = motionEvent.x - startX
            if (swipeDistance > MIN_SWIPE) {
                navControllerReference.navigateUp()
            }
        }
    }
}