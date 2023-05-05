package ru.spiridonov.gallery.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.presentation.MainActivity
import ru.spiridonov.gallery.presentation.activity.account.LoginActivity
import ru.spiridonov.gallery.presentation.activity.add_media.AddMediaActivity
import ru.spiridonov.gallery.presentation.fragments.dashboard.DashboardFragment
import ru.spiridonov.gallery.presentation.activity.fullscreen.FullscreenActivity
import ru.spiridonov.gallery.presentation.fragments.home.HomeFragment
import ru.spiridonov.gallery.presentation.fragments.profile.ProfileFragment


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

    fun inject(activity: LoginActivity)

    fun inject(activity: FullscreenActivity)

    fun inject(activity: AddMediaActivity)

    fun inject(fragment: DashboardFragment)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: ProfileFragment)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}