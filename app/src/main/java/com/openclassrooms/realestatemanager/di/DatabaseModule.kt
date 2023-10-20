package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.data.local.DatabaseCallBack
import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.RealEstateDataBase
import com.openclassrooms.realestatemanager.domain.mapper.PropertyMapper
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
    fun provideDatabase(
        @ApplicationContext context: Context,
    ):RealEstateDataBase = Room.databaseBuilder(
        context.applicationContext,
        RealEstateDataBase::class.java,
        "real_estate_db"
    ).build()

    @Provides
    @Singleton
    fun providePropertyDao(database: RealEstateDataBase): PropertyDao = database.propertyDao

    @Provides
    @Singleton
    fun providePropertyMapper() = PropertyMapper()

    /*@Provides
    @Singleton
    fun provideDatabaseCallback(propertyDao: PropertyDao): DatabaseCallBack{
        return DatabaseCallBack(propertyDao)
    }*/

}