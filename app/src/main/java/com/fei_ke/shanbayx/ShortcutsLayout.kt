package com.fei_ke.shanbayx

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class ShortcutsLayout : FrameLayout {
    private var center: View

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        View.inflate(context, R.layout.layout_shortcuts, this)
        center = findViewById(R.id.x_positive)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = (context.resources.displayMetrics.density * 200).toInt()
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val cX = (r - l) / 2
        val cY = (b - t) / 2
        val left = cX - center.measuredWidth / 2
        val top = cY - center.measuredHeight / 2
        center.layout(left, top, left + center.measuredHeight, top + center.measuredHeight)

        val radius = Math.hypot(center.measuredWidth.toDouble(), center.measuredHeight.toDouble()) / 1.5
        var offset = -Math.PI / 4
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child == center) continue

            val x = cX + radius * Math.cos(offset)
            val y = cY + radius * Math.sin(offset)

            val childLeft = (x - child.measuredWidth / 2).toInt()
            val childTop = (y - child.measuredHeight / 2).toInt()

            child.layout(childLeft, childTop, childLeft + child.measuredWidth, childTop + child.measuredHeight)

            offset -= Math.PI / 4
        }
    }
}
