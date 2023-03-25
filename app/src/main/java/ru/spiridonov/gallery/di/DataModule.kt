package ru.spiridonov.gallery.di

import dagger.Binds
import dagger.Module
import ru.spiridonov.gallery.data.repository.AlbumRepositoryImpl
import ru.spiridonov.gallery.data.repository.MediaRepositoryImpl
import ru.spiridonov.gallery.data.repository.UserRepositoryImpl
import ru.spiridonov.gallery.domain.repository.AlbumRepository
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.domain.repository.UserRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @ApplicationScope
    fun bindAlbumRepository(impl: AlbumRepositoryImpl): AlbumRepository

    @Binds
    @ApplicationScope
    fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository


    /* companion object {
         @Provides
         @ApplicationScope
         fun provideCurrListDao(
             application: Application
         ): CurrListDao {
             return AppDatabase.getInstance(application).currListDao()
         }
     }*/
}