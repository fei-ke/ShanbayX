package com.fei_ke.shanbayx

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class BackendService : AccessibilityService() {
    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }
}
