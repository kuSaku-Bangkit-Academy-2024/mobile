package com.capstone.kusaku.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.CategoryRepository
import com.capstone.kusaku.data.remote.UserRepository
import com.capstone.kusaku.data.remote.retrofit.ApiService
import com.capstone.kusaku.di.Injection
import com.capstone.kusaku.ui.home.HomeViewModel
import com.capstone.kusaku.ui.login.LoginViewModel
import com.capstone.kusaku.ui.profile.ProfileViewModel
import com.capstone.kusaku.ui.register.RegisterViewModel
import com.capstone.kusaku.ui.splash.SplashViewModel
import com.capstone.kusaku.ui.transaction.TransactionViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val apiService: ApiService,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(authRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                TransactionViewModel(apiService, authRepository, categoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(),
                    Injection.provideApiService(),
                    Injection.provideCategoryRepository()
                ).also { INSTANCE = it }
            }
        }
    }
}