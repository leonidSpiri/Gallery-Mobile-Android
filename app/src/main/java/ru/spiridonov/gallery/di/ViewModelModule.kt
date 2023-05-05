package ru.spiridonov.gallery.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.spiridonov.gallery.presentation.activity.account.AccountViewModel
import ru.spiridonov.gallery.presentation.activity.add_media.AddMediaViewModel
import ru.spiridonov.gallery.presentation.viewmodels.MainViewModel
import ru.spiridonov.gallery.presentation.fragments.dashboard.DashboardViewModel
import ru.spiridonov.gallery.presentation.activity.fullscreen.FullScreenViewModel
import ru.spiridonov.gallery.presentation.fragments.home.HomeViewModel
import ru.spiridonov.gallery.presentation.fragments.profile.ProfileViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullScreenViewModel::class)
    fun bindFullScreenViewModel(viewModel: FullScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddMediaViewModel::class)
    fun bindAddMediaViewModel(viewModel: AddMediaViewModel): ViewModel
}