package com.atizik.scoupexample

import android.app.Application
import ru.atizik.scoup.SingleEvent
import ru.atizik.scoup.di.bindInstance
import ru.atizik.scoup.di.module
import ru.atizik.scoup.fragments.ScopeCounter
import ru.atizik.scoup.fragments.appScope
import ru.atizik.scoup.viewmodel.ErrorHandler
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.smoothie.module.SmoothieApplicationModule
import java.util.*

class ScoupApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        toothpickInit()
    }

    private fun toothpickInit() {
        val toothpickConfig = if (BuildConfig.DEBUG) {
            Configuration.forDevelopment()
        } else
            Configuration.forProduction()
        Toothpick.setConfiguration(toothpickConfig)

        val mod = module {
            bindInstance<ErrorHandler> {
                object : ErrorHandler {
                    override val errorsEvent: SingleEvent<ArrayDeque<Throwable>> = SingleEvent()
                }
            }

        }
        val appScope = Toothpick.openScope(appScope)
        appScope.installModules(
            SmoothieApplicationModule(this),
            mod
            // Application level modules
        )
    }
}