package org.techtown.androidsimhwastudy

import android.app.Application
import org.techtown.androidsimhwastudy.util.TimberDebugTree
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(TimberDebugTree())
    }
}
