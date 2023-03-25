package ru.spiridonov.gallery

import android.app.Application
import android.content.Context
import ru.spiridonov.gallery.di.DaggerApplicationComponent

class GalleryApp : Application()/*, Configuration.Provider*/ {

    //@Inject
    //lateinit var workerFactory: CurrWorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
        appContext = applicationContext
    }

    /*override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(
                workerFactory
            )
            .build()
    }*/

    companion object {
        lateinit var appContext: Context
    }
}