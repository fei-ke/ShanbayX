package com.fei_ke.shanbayx

import android.view.View
import android.widget.TextView

class Binder(private val shortcutView: View, private val views: Array<View>) {
    private var curBindView: View? = null

    init {
        shortcutView.setOnClickListener {
            curBindView?.performClick()
        }
    }

    fun tryBind() {
        curBindView = views.find(View::isShown)
        shortcutView.isEnabled = curBindView != null

        if (curBindView is TextView && shortcutView is TextView) {
            shortcutView.text = (curBindView as TextView).text
        }
    }
}
