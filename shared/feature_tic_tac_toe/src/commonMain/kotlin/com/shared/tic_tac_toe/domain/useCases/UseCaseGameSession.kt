package com.shared.tic_tac_toe.domain.useCases

import com.shared.compose_foundation.ktor.socket.serializer
import com.shared.feature_profile.domain.use_case.UseCaseGetProfileDetails
import com.shared.tic_tac_toe.data.dto.BoardDTO
import com.shared.tic_tac_toe.data.dto.BoardState
import com.shared.tic_tac_toe.domain.models.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.decodeFromJsonElement

class UseCaseGameSession(
    private val sessionHandler: TicTacToeSessionHandler,
    private val profileDetails: UseCaseGetProfileDetails
)  {
    suspend fun execute(): Flow<Pair<GameState, BoardState>> {
        return sessionHandler
            .on("game")
            .map {
                val decodeFromJsonElement = serializer.decodeFromJsonElement<BoardDTO>(it)
                decodeFromJsonElement.gameState to decodeFromJsonElement.toBoardState()
            }
    }
}

