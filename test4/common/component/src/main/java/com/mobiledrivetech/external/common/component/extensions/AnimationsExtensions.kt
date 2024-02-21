package com.mobiledrivetech.external.common.component.extensions

import android.view.animation.Animation

/**
 * Add an action which will be invoked when an animation is started.
 *
 * @return the [Animation] with listener
 */
inline fun Animation.onAnimationStart(
    crossinline action: (animation: Animation) -> Unit
) = onAnimationListener(onAnimationStart = action)

/**
 * Add an action which will be invoked when an animation is finished.
 *
 * @return the [Animation] with listener
 */
inline fun Animation.onAnimationEnd(
    crossinline action: (animation: Animation) -> Unit
) = onAnimationListener(onAnimationEnd = action)

/**
 * Add an action which will be invoked when an animation try to repeat.
 *
 * @return the [Animation] with listener
 */
inline fun Animation.onAnimationRepeat(
    crossinline action: (animation: Animation) -> Unit
) = onAnimationListener(onAnimationRepeat = action)

/**
 * Add an animation listener to this animation using the provided actions
 *
 * @return the [Animation] with listener
 */
inline fun Animation.onAnimationListener(
    crossinline onAnimationStart: (animation: Animation) -> Unit = {},
    crossinline onAnimationEnd: (animation: Animation) -> Unit = {},
    crossinline onAnimationRepeat: (animation: Animation) -> Unit = {}
): Animation {
    val animationListener = object : Animation.AnimationListener {
        /**
         *
         * Notifies the start of the animation.
         *
         * @param animation The started animation.
         */
        override fun onAnimationStart(animation: Animation) {
            onAnimationStart.invoke(animation)
        }

        /**
         *
         * Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.
         *
         * @param animation The animation which reached its end.
         */
        override fun onAnimationEnd(animation: Animation) {
            onAnimationEnd.invoke(animation)
        }

        /**
         *
         * Notifies the repetition of the animation.
         *
         * @param animation The animation which was repeated.
         */
        override fun onAnimationRepeat(animation: Animation) {
            onAnimationRepeat.invoke(animation)
        }
    }
    setAnimationListener(animationListener)

    return this
}
