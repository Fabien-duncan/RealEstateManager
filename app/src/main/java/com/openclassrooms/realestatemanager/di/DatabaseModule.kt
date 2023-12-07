package com.openclassrooms.realestatemanager.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.openclassrooms.realestatemanager.common.utils.PermissionCheckProvider
import com.openclassrooms.realestatemanager.common.utils.VersionProvider
import com.openclassrooms.realestatemanager.data.Connection.ConnectionCheckerRepositoryImpl
import com.openclassrooms.realestatemanager.data.api.GeocodingApiService
import com.openclassrooms.realestatemanager.data.currency_converter.CurrencyRepositoryImpl
import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.RealEstateDataBase
import com.openclassrooms.realestatemanager.data.location.DefaultLocationTracker
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import com.openclassrooms.realestatemanager.domain.location.LocationTracker
import com.openclassrooms.realestatemanager.domain.mapper.AddressMapper
import com.openclassrooms.realestatemanager.domain.mapper.NearbyPlacesMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyPhotosMapper
import com.openclassrooms.realestatemanager.domain.use_cases.CalculateLoanUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ):RealEstateDataBase = RealEstateDataBase.getInstance(context)

    @Provides
    @Singleton
    fun providePropertyDao(database: RealEstateDataBase): PropertyDao = database.propertyDao

    @Provides
    @Singleton
    fun provideAddressMapper() = AddressMapper()
    @Provides
    @Singleton
    fun provideNearbyPlacesMapper() = NearbyPlacesMapper()
    @Provides
    @Singleton
    fun providePropertyPhotosMapper() = PropertyPhotosMapper()
    @Provides
    @Singleton
    fun providePropertyMapper() = PropertyMapper(propertyPhotosMapper = providePropertyPhotosMapper(), nearbyPlacesMapper = provideNearbyPlacesMapper(), addressMapper = provideAddressMapper())

    @Provides
    @Singleton
    fun provideGeocodingApiService(retrofit: Retrofit): GeocodingApiService {
        return retrofit.create(GeocodingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideFusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application,
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application,
        permissionCheckProvider = PermissionCheckProvider()
    )

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("your_shared_preferences_name", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideCalculateLoanUseCase(): CalculateLoanUseCase {
        return CalculateLoanUseCase()
    }
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    @Provides
    @Singleton
    fun provideVersionProvider(): VersionProvider {
        return VersionProvider()
    }
}