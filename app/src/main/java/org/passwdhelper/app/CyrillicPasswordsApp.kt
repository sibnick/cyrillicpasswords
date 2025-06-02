package org.passwdhelper.app

import android.app.Application

class CyrillicPasswordsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // WorkManager will be initialized automatically by AndroidX
        // No need for manual initialization which can cause crashes
    }
}
