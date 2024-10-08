package com.shared.tic_tac_toe.domain.useCases

import com.shared.compose_foundation.ktor.socket.serializer
import com.shared.compose_foundation.platform.getHostAddress
import com.shared.tic_tac_toe.domain.models.PlayRequest
import com.shared.tic_tac_toe.data.dto.PlayRequestDTO
import com.shared.tic_tac_toe.domain.Participant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

class UseCaseRespondToPlayWithMeRequest(
    val sessionHandler: TicTacToeSessionHandler
) {
    suspend fun onNewRequest(
        coroutineScope: CoroutineScope,
    ) = sessionHandler
        .on("play_request")
        .map {
            val requestDTO = serializer.decodeFromJsonElement<PlayRequestDTO>(it)
            PlayRequest(
                participant = Participant(
                    bio = requestDTO.participant.bio,
                    profile_image = requestDTO.participant.profile_image
                        .replace("localhost", getHostAddress()),
                    user_name = requestDTO.participant.user_name,
                    isRequestingToPlay = true
                ),
                accept = {
                    coroutineScope.launch {
                        this@UseCaseRespondToPlayWithMeRequest.sessionHandler.emmit(
                            event = "play_request_accept",
                            payload = requestDTO.invitationID
                        )
                    }
                },
                reject = {
                    coroutineScope.launch {
                        this@UseCaseRespondToPlayWithMeRequest.sessionHandler.emmit(
                            event = "play_request_reject",
                            payload = requestDTO.invitationID
                        )
                    }
                },
                invitationID = requestDTO.invitationID,
            )
        }
}
