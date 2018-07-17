package com.fei_ke.shanbayx

import android.app.Activity
import android.view.View
import android.widget.TextView

class Binder(private val activity: Activity,
             private val shortcutView: View,
             private val ids: IntArray) {

    init {
        shortcutView.setOnClickListener {
            curBindView?.performClick()
        }
    }

    private var curBindView: View? = null


    fun tryBind() {
        var visibleView: View? = null
        for (i in ids) {
            val view = activity.findViewById<View>(i)
            if (view?.isShown == true) {
                visibleView = view
                break
            }
        }
        curBindView = visibleView

        shortcutView.isEnabled = curBindView != null

        if (curBindView is TextView && shortcutView is TextView) {
            shortcutView.text = (curBindView as TextView).text
        }
    }
}