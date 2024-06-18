package com.dicoding.storyappaaz.animation

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View

fun applyFadeInAnimations(context: Context, views: List<View>) {
    val animationDuration = 500L
    val delayBetweenFields = 250L

    views.forEachIndexed { index, view ->
        val fadeInAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        fadeInAnimator.duration = animationDuration
        fadeInAnimator.startDelay = delayBetweenFields * index

        view.visibility = View.INVISIBLE
        view.postDelayed({
            view.visibility = View.VISIBLE
            fadeInAnimator.start()
        }, delayBetweenFields * index)
    }
}