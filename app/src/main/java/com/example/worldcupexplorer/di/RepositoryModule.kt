package com.example.worldcupexplorer.di

import com.example.worldcupexplorer.data.repository.FootballRepositoryImpl
import com.example.worldcupexplorer.domain.repository.FootballRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFootballRepository(
        footballRepositoryImpl: FootballRepositoryImpl
    ): FootballRepository
}
