package com.fei_ke.shanbayx

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.FrameLayout

class ShortcutsHelper(private val activity: Activity) {
    companion object {
        fun View.findViewByIdName(idName: String): View? {
            val id = context.resources.getIdentifier(idName, "id", context.packageName)
            return findViewById(id)
        }
    }

    private val soundBinder: Binder
    private val negativeBinder: Binder
    private val positiveBinder: Binder

    init {
        val contentView: FrameLayout = activity.window.decorView.findViewById(Window.ID_ANDROID_CONTENT)
        val shortcutsLayout = ShortcutsLayout(activity.createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY))
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM or Gravity.END
        contentView.addView(shortcutsLayout, params)

        val btnSound = shortcutsLayout.findViewById<View>(R.id.x_sound)
        val btnPositive = shortcutsLayout.findViewById<View>(R.id.x_positive)
        val btnNegative = shortcutsLayout.findViewById<View>(R.id.x_negative)

        soundBinder = Binder(btnSound, listOfNotNull(
                contentView.findViewByIdName("btn_word_sound_play"),
                contentView.findViewByIdName("view_explore")?.findViewByIdName("btn_sound_in_word"),
                contentView.findViewByIdName("view_recognition")?.findViewByIdName("btn_sound_in_word")
        ).toTypedArray())

        negativeBinder = Binder(btnNegative, listOfNotNull(
                contentView.findViewByIdName("head_title"),
                contentView.findViewByIdName("view_recognition")?.findViewByIdName("unknown"),
                contentView.findViewByIdName("view_listen")?.findViewByIdName("unknown")
        ).toTypedArray())

        positiveBinder = Binder(btnPositive, listOfNotNull(
                contentView.findViewByIdName("next_button"),
                contentView.findViewByIdName("button_next_group"),
                contentView.findViewByIdName("view_recognition")?.findViewByIdName("known"),
                contentView.findViewByIdName("view_recognition")?.findViewByIdName("detail_button"),
                contentView.findViewByIdName("view_listen")?.findViewByIdName("known"),
                contentView.findViewByIdName("view_listen")?.findViewByIdName("detail_button"),
                contentView.findViewByIdName("detail")
        ).toTypedArray())

        shortcutsLayout.findViewById<View>(R.id.handle).setOnTouchListener(object : View.OnTouchListener {
            private var lastX = 0f
            private var lastY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY

                        val params = shortcutsLayout.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.NO_GRAVITY
                        params.leftMargin = shortcutsLayout.left
                        params.topMargin = shortcutsLayout.top
                        shortcutsLayout.layoutParams = params
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val diffX = event.rawX - lastX
                        val diffY = event.rawY - lastY

                        val params = shortcutsLayout.layoutParams as FrameLayout.LayoutParams
                        params.leftMargin += diffX.toInt()
                        params.topMargin += diffY.toInt()
                        shortcutsLayout.layoutParams = params

                        lastX = event.rawX
                        lastY = event.rawY
                    }
                }
                return true
            }
        })
    }

    fun onModeChanged(mode: String) {
        activity.window.decorView.postDelayed({
            soundBinder.tryBind()
            negativeBinder.tryBind()
            positiveBinder.tryBind()
        }, 100)

    }
}
