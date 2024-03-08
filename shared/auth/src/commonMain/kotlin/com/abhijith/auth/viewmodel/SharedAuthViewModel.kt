package com.abhijith.auth.viewmodel

import arrow.core.Option
import com.abhijith.auth.viewmodel.usecases.LoginResult
import com.abhijith.auth.viewmodel.usecases.RegistrationResult
import com.abhijith.auth.viewmodel.usecases.UseCaseLogin
import com.abhijith.auth.viewmodel.usecases.UseCaseRegistration
import com.abhijith.foundation.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SharedAuthViewModel : SharedViewModel {
    fun getLoginState(): StateFlow<Option<String>>
    fun login(userName: String, password: String): Flow<LoginResult>
    fun register(userName: String, password: String): Flow<RegistrationResult>
    fun logout()
}

