package com.ahmed_apps.dictionary_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 全局应用程序类，使用Dagger Hilt进行依赖注入。
 *
 * @author Ahmed Guedmioui
 */
@HiltAndroidApp
class App: Application()
