package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.RealEstateDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePropertyDao(database: RealEstateDataBase): PropertyDao = database.propertyDao

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ):RealEstateDataBase = Room.databaseBuilder(
        context,
        RealEstateDataBase::class.java,
        "real_estate_db"
    ).build()
}