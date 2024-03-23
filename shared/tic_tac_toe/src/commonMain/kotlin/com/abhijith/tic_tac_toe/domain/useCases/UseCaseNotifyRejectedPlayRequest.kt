package com.abhijith.tic_tac_toe.domain.useCases

import com.abhijith.foundation.ktor.socket.serializer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement

class UseCaseNotifyRejectedPlayRequest(
    val socketMediator: UseCaseSocketToUseCaseMediator
) {
    @Serializable
    data class OnRejectDTO(
        val reqId:String,
        val isAccepted:Boolean,
        val event:String
    )
    suspend fun onReject(
        onReject:(String)->Unit
    ) {
        coroutineScope {
            socketMediator
                .on("play_request_reject")
                .collectLatest {
                    val response:OnRejectDTO = serializer.decodeFromJsonElement(it)
                    onReject(response.reqId)
                }
        }
    }
}