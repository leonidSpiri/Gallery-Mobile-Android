package ru.spiridonov.gallery.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.presentation.MainActivity
import ru.spiridonov.gallery.presentation.ui.dashboard.DashboardFragment
import ru.spiridonov.gallery.presentation.ui.home.HomeFragment
import ru.spiridonov.gallery.presentation.ui.profile.ProfileFragment


@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class
    ]
)
interface ApplicationComponent {

    fun inject(application: GalleryApp)

    fun inject(activity: MainActivity)

    fun inject(fragment: DashboardFragment)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: ProfileFragment)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}