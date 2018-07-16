package com.fei_ke.shanbayx

import android.app.Activity
import android.os.Bundle
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import kotlin.Int

class XposedMod : IXposedHookLoadPackage {
    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName != "com.shanbay.words") return

        val classReviewActivity = XposedHelpers.findClass("com.shanbay.words.learning.main.ReviewActivity", lpparam.classLoader)
        XposedHelpers.findAndHookMethod(classReviewActivity, "onCreate", Bundle::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val activity = param.thisObject as Activity
                val shortcutsHelper = ShortcutsHelper(activity)

                XposedHelpers.setAdditionalInstanceField(activity, "helper", shortcutsHelper)
            }
        })
        XposedHelpers.findAndHookMethod(Activity::class.java, "onTitleChanged", CharSequence::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (classReviewActivity.isInstance(param.thisObject)) {
                            val curMode = param.args[0].toString()
                            (XposedHelpers.getAdditionalInstanceField(param.thisObject, "helper") as ShortcutsHelper?)
                                    ?.onModeChanged(curMode)
                        }
                    }
                })
        XposedHelpers.findAndHookMethod(classReviewActivity, "onDestroy", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                XposedHelpers.setAdditionalInstanceField(param.thisObject, "helper", null)
            }
        })
    }


}
