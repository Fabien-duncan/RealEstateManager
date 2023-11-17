package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.geocoding.GeocodingRepositoryImp
import com.openclassrooms.realestatemanager.data.repository.PropertyRepositoryImpl
import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.repository.Respository
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
    abstract fun bindRepository(propertyRepositoryImpl: PropertyRepositoryImpl):Respository

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(geocodingRepositoryImp: GeocodingRepositoryImp):GeocodingRepository
}