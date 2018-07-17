package com.fei_ke.shanbayx

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.FrameLayout

class ShortcutsHelper(private val activity: Activity) {
    companion object {
        const val MODE_PREVIEW = "预习模式"
        const val MODE_SELF_PERCEIVED = "自评模式"
        const val MODE_RECALL = "回忆模式"
        const val MODE_EXPLORE = "探索模式"
        const val MODE_SPELL = "拼写模式"
        const val MODE_SUMMAR = "小结模式"
        const val MODE_TEST = "测试模式"
        const val MODE_NORMAL = "扇贝单词"
        const val MODE_LISTEN = "听词模式"
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

        soundBinder = Binder(activity, btnSound,
                intArrayOf(
                        resolveId("btn_word_sound_play"),
                        resolveId("btn_sound_in_word")
                ))
        negativeBinder = Binder(activity, btnNegative,
                intArrayOf(
                        resolveId("head_title"),
                        resolveId("unknown"),
                        resolveId("control_widget_btn_white_unknown")
                ))
        positiveBinder = Binder(activity, btnPositive,
                intArrayOf(
                        resolveId("next_button"),
                        resolveId("button_next_group"),
                        resolveId("known"),
                        resolveId("detail"),
                        resolveId("control_widget_btn_white_known"),
                        resolveId("control_widget_btn_green")
                ))

        shortcutsLayout.findViewById<View>(R.id.handle).setOnTouchListener(object : View.OnTouchListener {
            private var lastX = 0f
            private var lastY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val diffX = event.rawX - lastX
                        val diffY = event.rawY - lastY

                        shortcutsLayout.offsetLeftAndRight(diffX.toInt())
                        shortcutsLayout.offsetTopAndBottom(diffY.toInt())

                        lastX = event.rawX
                        lastY = event.rawY
                    }
                }
                return true
            }
        })
    }

    fun onModeChanged(mode: String) {
        Log.i("ShortcutsHelper:mode", mode)

        activity.window.decorView.postDelayed({
            soundBinder.tryBind()
            negativeBinder.tryBind()
            positiveBinder.tryBind()
        }, 100)

    }


    private fun resolveId(idName: String) =
            activity.resources.getIdentifier(idName, "id", activity.packageName)
}
