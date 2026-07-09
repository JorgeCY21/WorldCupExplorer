package com.example.worldcupexplorer.di

import android.content.Context
import androidx.room.Room
import com.example.worldcupexplorer.data.local.db.FootballDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private const val DATABASE_NAME = "football_explorer.db"

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideFootballDatabase(
        @ApplicationContext context: Context
    ): FootballDatabase {
        return Room.databaseBuilder(context, FootballDatabase::class.java, DATABASE_NAME)
            .setQueryExecutor(Executors.newSingleThreadExecutor())
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}