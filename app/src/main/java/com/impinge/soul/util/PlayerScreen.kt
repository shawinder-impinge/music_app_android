package com.impinge.soul.util

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

class PlayerScreen(context: Context, attributeSet: AttributeSet? = null)
: MotionLayout(context, attributeSet) {


    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            transitionToEnd()
            return false
        }
    })
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //This ensures the Mini Player is maximised on single tap
        gestureDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                var hasTouchStarted = false
                return super.onTouchEvent(event)
            }
        }
        val hasTouchStarted = false
        if (!hasTouchStarted) {
            //This Checks if the touch is on the Player or the transaprent background
//            this.getHitRect(viewRect)
//            hasTouchStarted =
//                hasTouchStarted    viewRect.contains(event.x.toInt(),vent.y.toInt())
        }
        return hasTouchStarted && super.onTouchEvent(event)
    }
}