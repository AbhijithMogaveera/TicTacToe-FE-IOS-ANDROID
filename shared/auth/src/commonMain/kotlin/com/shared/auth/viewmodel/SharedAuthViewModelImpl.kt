package com.shared.auth.viewmodel
import arrow.core.Option
import com.shared.auth.models.LoginResult
import com.shared.auth.models.RegistrationResult
import com.shared.auth.viewmodel.usecases.UseCaseLogin
import com.shared.auth.viewmodel.usecases.UseCaseLogout
import com.shared.auth.viewmodel.usecases.UseCaseRegistration
import com.shared.auth.viewmodel.usecases.UseCaseGetAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class SharedAuthViewModelImpl constructor(
    private val useCaseLogin: UseCaseLogin,
    private val userCaseRegistration: UseCaseRegistration,
    private val useCaseGetAuthToken: UseCaseGetAuthToken,
    private val useCaseLogout: UseCaseLogout
) : SharedAuthViewModel {

    override var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun getLoginState(): StateFlow<Option<String>> {
        return useCaseGetAuthToken.getToken()
    }

    override fun login(
        userName: String,
        password: String
    ): Flow<LoginResult> = flow {
        val login = useCaseLogin.login(userName.trim(), password.trim())
        emit(login)
    }

    override fun register(userName: String, password: String): Flow<RegistrationResult> =
        flow {
            val it = userCaseRegistration.register(
                userName, password
            )
            emit(it)
        }

    override fun logout() {
        coroutineScope.launch {
            useCaseLogout.logout()
        }
    }

}