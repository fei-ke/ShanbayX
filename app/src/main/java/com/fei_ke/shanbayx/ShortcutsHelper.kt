package com.fei_ke.shanbayx

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.FrameLayout

class ShortcutsHelper(private val activity: Activity) : View.OnClickListener {
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

    private var curMode: String = ""
    private var btnRevocation: Button
    private var btnSound: Button
    private var btnPositive: Button
    private var btnNegative: Button

    private val viewCache: HashMap<String, View> by lazy { HashMap<String, View>() }

    init {
        val contentView: FrameLayout = activity.window.decorView.findViewById(Window.ID_ANDROID_CONTENT)
        val shortcutsLayout = ShortcutsLayout(activity.createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY))
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM or Gravity.END
        contentView.addView(shortcutsLayout, params)

        btnSound = shortcutsLayout.findViewById(R.id.x_sound)
        btnPositive = shortcutsLayout.findViewById(R.id.x_positive)
        btnRevocation = shortcutsLayout.findViewById(R.id.x_revocation)
        btnNegative = shortcutsLayout.findViewById(R.id.x_negative)

        btnSound.setOnClickListener(this)
        btnPositive.setOnClickListener(this)
        btnRevocation.setOnClickListener(this)
        btnNegative.setOnClickListener(this)

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
        curMode = mode
        when (mode) {
            MODE_SELF_PERCEIVED, MODE_RECALL -> {
                btnPositive.text = "认识"
                btnNegative.text = "不认识"
            }
            MODE_SUMMAR -> {
                btnPositive.text = "下一组"
                btnNegative.text = "不认识"
            }
            MODE_EXPLORE -> {
                btnPositive.text = "下一个"
                btnNegative.text = "不认识"
            }
            MODE_LISTEN -> {
                btnPositive.text = "听懂了"
                btnNegative.text = "没听懂"
            }
        }
    }

    override fun onClick(v: View) {
        when (v) {
            btnSound -> {
                when (curMode) {
                    MODE_LISTEN -> performOriginalClick(activity, "btn_word_sound_play")
                    else -> performOriginalClick(activity, "btn_sound_in_word")
                }
            }
            btnRevocation -> performOriginalClick(activity, "head_title")
            btnNegative -> performOriginalClick(activity, "unknown")
            btnPositive -> {
                when (curMode) {
                    MODE_SELF_PERCEIVED, MODE_LISTEN -> performOriginalClick(activity, "known")
                    MODE_EXPLORE -> performOriginalClick(activity, "next_button")
                    MODE_SUMMAR -> performOriginalClick(activity, "button_next_group")
                }
            }
        }
    }


    private fun performOriginalClick(activity: Activity, idName: String) {
        if (viewCache[idName] == null) {
            val id = activity.resources.getIdentifier(idName, "id", activity.packageName)
            viewCache[idName] = activity.findViewById<View>(id)
        }
        viewCache[idName]?.performClick()
    }
}
