package com.alcorp.fashionism_umkm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.di.Injection
import com.alcorp.fashionism_umkm.ui.auth.login.LoginViewModel
import com.alcorp.fashionism_umkm.ui.auth.signup.SignUpViewModel
import com.alcorp.fashionism_umkm.ui.home.HomeViewModel
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.AddEditOutfitViewModel
import com.alcorp.fashionism_umkm.ui.home.detail_outfit.DetailOutfitViewModel
import com.alcorp.fashionism_umkm.ui.profile.ProfileViewModel
import com.alcorp.fashionism_umkm.ui.profile.change_password.ChangePasswordViewModel
import com.alcorp.fashionism_umkm.ui.profile.edit_profile.EditProfileViewModel
import com.alcorp.fashionism_umkm.ui.transaction.TransactionViewModel
import com.alcorp.fashionism_umkm.ui.transaction.detail.ProductViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddEditOutfitViewModel::class.java)) {
            return AddEditOutfitViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailOutfitViewModel::class.java)) {
            return DetailOutfitViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}