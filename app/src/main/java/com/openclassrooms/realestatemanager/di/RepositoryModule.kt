package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.currency_converter.CurrencyRepositoryImpl
import com.openclassrooms.realestatemanager.data.geocoding.GeocodingRepositoryImp
import com.openclassrooms.realestatemanager.data.repository.PropertyRepositoryImpl
import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.repository.Repository
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
    abstract fun bindRepository(propertyRepositoryImpl: PropertyRepositoryImpl):Repository

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(geocodingRepositoryImp: GeocodingRepositoryImp):GeocodingRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl):CurrencyRepository
}