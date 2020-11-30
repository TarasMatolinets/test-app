package com.demo.utils

import androidx.lifecycle.Observer

open class Event<out T>(private val content: T) {

    private var handled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContent(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }

}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onUnhandledEvent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onUnhandledEvent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContent()?.let {
            onUnhandledEvent(it)
        }
    }
}

