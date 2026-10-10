package com.fashionism.fashionismuserapp.di

import com.fashionism.fashionismuserapp.BuildConfig
import com.fashionism.fashionismuserapp.data.local.UserSession
import com.fashionism.fashionismuserapp.data.local.sessionDataStore
import com.fashionism.fashionismuserapp.data.remote.AuthInterceptor
import com.fashionism.fashionismuserapp.data.remote.ProductApi
import com.fashionism.fashionismuserapp.data.remote.RecommendationApi
import com.fashionism.fashionismuserapp.data.remote.UserApi
import com.fashionism.fashionismuserapp.data.repository.AuthRepository
import com.fashionism.fashionismuserapp.data.repository.ProductRepository
import com.fashionism.fashionismuserapp.data.repository.UserRepository
import com.fashionism.fashionismuserapp.ui.auth.AuthViewModel
import com.fashionism.fashionismuserapp.ui.home.HomeViewModel
import com.fashionism.fashionismuserapp.ui.product.FavoriteViewModel
import com.fashionism.fashionismuserapp.ui.product.ProductDetailViewModel
import com.fashionism.fashionismuserapp.ui.profile.ProfileViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val AUTHENTICATED = named("authenticated")

val networkModule = module {
    single {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        )
    }
    // The ML service needs no login, so it gets the plain client
    single { OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>()).build() }
    single(AUTHENTICATED) {
        get<OkHttpClient>().newBuilder()
            .apply { interceptors().add(0, AuthInterceptor(get())) } // before logging, so the header is logged
            .build()
    }

    single { retrofit(BuildConfig.API_URL_V1, get(AUTHENTICATED)).create(UserApi::class.java) }
    single { retrofit(BuildConfig.API_URL_V2, get(AUTHENTICATED)).create(ProductApi::class.java) }
    single { retrofit(BuildConfig.API_URL_ML, get()).create(RecommendationApi::class.java) }
}

val dataModule = module {
    single { UserSession(androidContext().sessionDataStore) }
    singleOf(::AuthRepository)
    singleOf(::UserRepository)
    singleOf(::ProductRepository)
}

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductDetailViewModel)
    viewModelOf(::FavoriteViewModel)
}

private fun retrofit(baseUrl: String, client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
