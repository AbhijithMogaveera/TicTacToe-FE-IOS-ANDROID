package com.abhijith.tic_tac_toe.domain.useCases

import arrow.core.Either
import arrow.core.Option
import arrow.core.right
import com.abhijith.foundation.ktor.exceptions.serializer
import com.abhijith.tic_tac_toe.domain.models.ActiveParticipantsEvent
import com.abhijith.tic_tac_toe.domain.models.Participant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.decodeFromJsonElement

internal class UseCaseGetAllPlayers(
    private val useCaseSocketToUseCaseMediator: UseCaseSocketToUseCaseMediator
) {

    enum class Failure {
        UnAuthorized, UnKnow
    }

    suspend fun execute(
        searchKey: Option<String>
    ): Flow<Either<Failure, List<Participant>>> {
        println("looking for other player...")
        val map = useCaseSocketToUseCaseMediator
            .on("activeParticipants")
            .map{
                serializer.decodeFromJsonElement<ActiveParticipantsEvent>(it).data.right()
            }
        return map
    }

}