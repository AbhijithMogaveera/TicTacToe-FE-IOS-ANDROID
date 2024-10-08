package com.shared.tic_tac_toe.data.dto

import com.shared.tic_tac_toe.domain.models.TileState
import kotlinx.serialization.Serializable

@Serializable
data class BoardStateDTO(
    val board: List<TileState> = buildList {
        repeat(9) {
            add(TileState.NONE)
        }
    },
    val winTileStart: Int? = null,
    val winTileMiddle: Int? = null,
    val winTileEnd: Int? = null,
    val winPlayerUsername: String? = null,
    val activePlayerUserName: String? = null,
    val activePlayerTile: TileState = TileState.NONE,
    val invitation_id: String

)

