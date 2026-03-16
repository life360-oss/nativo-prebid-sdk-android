package com.nativo.prebidsdk.utils

import android.os.SystemClock
import org.prebid.mobile.api.rendering.BannerView
import org.prebid.mobile.api.rendering.listeners.BannerViewListener
import java.util.concurrent.atomic.AtomicLong

object NativoUtils {
    fun debounceAction(intervalMs: Long, action: () -> Unit): () -> Unit {
        val lastCall = AtomicLong(0L)
        return {
            val now = SystemClock.elapsedRealtime()
            val previous = lastCall.get()
            if (now - previous >= intervalMs) {
                lastCall.set(now)
                action()
            }
        }
    }

    /**
     * Checks if the listener has implemented the onNativoAdLoaded method
     * by checking if it's overridden from the default interface implementation.
     */
    @JvmStatic
    fun hasImplementedNativoCallback(listener: BannerViewListener?): Boolean {
        if (listener == null) {
            return false
        }

        return try {
            val method = listener.javaClass.getMethod("onNativoAdLoaded", BannerView::class.java)
            // Check if the method is declared in the listener's own class (not just inherited from interface)
            // This means they've overridden it from the default implementation
            method.declaringClass != BannerViewListener::class.java
        } catch (e: NoSuchMethodException) {
            false
        }
    }
}
