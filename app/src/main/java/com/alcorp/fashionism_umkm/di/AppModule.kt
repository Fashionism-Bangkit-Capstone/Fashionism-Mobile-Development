package com.alcorp.fashionism_umkm.di

import com.alcorp.fashionism_umkm.BuildConfig
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.retrofit.ApiService
import com.alcorp.fashionism_umkm.ui.auth.login.LoginViewModel
import com.alcorp.fashionism_umkm.ui.auth.register.RegisterViewModel
import com.alcorp.fashionism_umkm.ui.home.HomeViewModel
import com.alcorp.fashionism_umkm.ui.product.ProductViewModel
import com.alcorp.fashionism_umkm.ui.product.add_or_edit_product.AddEditProductViewModel
import com.alcorp.fashionism_umkm.ui.product.detail_product.DetailProductViewModel
import com.alcorp.fashionism_umkm.ui.profile.ProfileViewModel
import com.alcorp.fashionism_umkm.ui.profile.change_password.ChangePasswordViewModel
import com.alcorp.fashionism_umkm.ui.profile.edit_profile.EditProfileViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )
            )
            .build()
    }
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    singleOf(::AppRepository)
}

val viewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddEditProductViewModel)
    viewModelOf(::DetailProductViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::ChangePasswordViewModel)
}
