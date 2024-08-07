package com.shared.auth.viewmodel.usecases.impl

import arrow.core.Option
import com.shared.auth.util.UserAccountsManager
import com.shared.auth.viewmodel.usecases.UseCaseGetAuthToken
import kotlinx.coroutines.flow.StateFlow

internal class UseCaseGetAuthTokenDefaultImpl(
    val authManger: UserAccountsManager
):UseCaseGetAuthToken {
    override fun getToken(): StateFlow<Option<String>> {
        return authManger.token
    }
}