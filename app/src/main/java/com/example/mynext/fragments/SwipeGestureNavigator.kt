package com.example.mynext.fragments

import android.view.MotionEvent
import androidx.navigation.NavController

class SwipeGestureNavigator(val navControllerReference: NavController) {
    private var startX : Float = 0f
    private val MIN_SWIPE = 300  //MIN_SWIPE is hard coded to 300 but should be pixel independent

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