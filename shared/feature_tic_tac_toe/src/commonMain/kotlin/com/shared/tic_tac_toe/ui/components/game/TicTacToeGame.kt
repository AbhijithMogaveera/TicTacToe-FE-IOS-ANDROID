package com.shared.tic_tac_toe.ui.components.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.some
import com.shared.compose_foundation.platform.Platform
import com.shared.tic_tac_toe.data.dto.BoardState
import com.shared.tic_tac_toe.domain.models.TileState
import com.shared.tic_tac_toe.domain.viewmodels.TicTacToeViewModel
import com.shared.tic_tac_toe.ui.components.ActivePlayerIndicatorColor
import com.shared.tic_tac_toe.ui.components.InActivePlayerIndicatorColor
import com.shared.tic_tac_toe.ui.components.activeStrokeWidth
import com.shared.tic_tac_toe.ui.components.inActiveStrokeWidth
import com.shared.tic_tac_toe.ui.components.invitations.Timer
import com.shared.feature_profile.domain.models.User
import com.shared.tic_tac_toe.domain.viewmodels.ProfileViewModel


enum class PlayerDetailsAlignment {
    START, END
}

@Composable
fun CurrentUser(): Option<User> {
    val vm = viewModel { ProfileViewModel() }
    return vm.loggedInUserDetails
}

val BoardPadding = 10.dp

@Composable
fun TicTacToeGame(modifier: Modifier = Modifier) {
    CurrentUser().onSome { activeUser ->

        val boardState: BoardState = TicTacToeViewModel.boardState.getOrNull() ?: return

        var parentLayoutCoordinates: Option<LayoutCoordinates> by remember { mutableStateOf(None) }
        var boardLayoutCoordinates: Option<LayoutCoordinates> by remember { mutableStateOf(None) }
        var playerOneLayoutCoordinates: Option<LayoutCoordinates> by remember { mutableStateOf(None) }
        var playerTwoLayoutCoordinates: Option<LayoutCoordinates> by remember { mutableStateOf(None) }

        val playerXIndicatorColor by animateColorAsState(targetValue = if (boardState.getCurrentTurnTile() == TileState.X) ActivePlayerIndicatorColor else InActivePlayerIndicatorColor)
        val player0IndicatorColor by animateColorAsState(targetValue = if (boardState.getCurrentTurnTile() == TileState.O) ActivePlayerIndicatorColor else InActivePlayerIndicatorColor)

        val playerXStockWidth by animateDpAsState(targetValue = if (boardState.getCurrentTurnTile() == TileState.X) activeStrokeWidth else inActiveStrokeWidth)
        val playerOIndicatorColor by animateDpAsState(targetValue = if (boardState.getCurrentTurnTile() == TileState.O) activeStrokeWidth else inActiveStrokeWidth)

        Box {
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxSize()
                    .drawBehind {
                        val boardLayoutCoordinates =
                            boardLayoutCoordinates.getOrElse { return@drawBehind }
                        val playerOneLayoutCoordinates =
                            playerOneLayoutCoordinates.getOrElse { return@drawBehind }
                        val playerTwoLayoutCoordinates =
                            playerTwoLayoutCoordinates.getOrElse { return@drawBehind }
                        val parentLayoutCoordinates =
                            parentLayoutCoordinates.getOrElse { return@drawBehind }
                        val startLineY =
                            (boardLayoutCoordinates.localToRoot(Offset.Zero).y - BoardPadding.toPx()) + boardLayoutCoordinates.size.height.toFloat()
                        val startLineStart = Offset(
                            x = 160f,
                            y = startLineY
                        )
                        val startLineEnd = let {
                            parentLayoutCoordinates
                                .localPositionOf(
                                    playerOneLayoutCoordinates,
                                    Offset.Zero
                                ).let {
                                    it.copy(
                                        x = 160f,
                                        y = it.y
                                    )
                                }
                        }
                        drawLine(
                            start = if(Platform.isIphone) startLineEnd else startLineStart,
                            end = if(Platform.isIphone) startLineEnd+(startLineEnd-startLineStart) else startLineEnd,
                            color = playerXIndicatorColor,
                            strokeWidth = playerXStockWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                        val end = parentLayoutCoordinates
                            .localPositionOf(playerTwoLayoutCoordinates, Offset.Zero).let {
                                it.copy(
                                    x = boardLayoutCoordinates.size.width.toFloat() - 160f,
                                    y = it.y
                                )
                            }
                        drawLine(
                            start = Offset(
                                x = boardLayoutCoordinates.size.width.toFloat() - 160f,
                                y = boardLayoutCoordinates.size.height.toFloat()
                            ),
                            end = end,
                            color = player0IndicatorColor,
                            strokeWidth = playerOIndicatorColor.toPx(),
                            cap = StrokeCap.Round
                        )

                    }.onGloballyPositioned { parentLayoutCoordinates = it.some() }
            ) {
                Text(
                    text = "Tic Tac Toe",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    textAlign = TextAlign.Center
                )
                BoardGrid(
                    modifier = Modifier
                        .padding(BoardPadding)
                        .onGloballyPositioned { boardLayoutCoordinates = it.some() }
                )
                Spacer(modifier = Modifier.height(10.dp))
                PlayDetails(
                    alignment = PlayerDetailsAlignment.START,
                    modifier = Modifier.onGloballyPositioned {
                        playerOneLayoutCoordinates = it.some()
                    },
                    indicatorColor = playerXIndicatorColor,
                    strokeWidth = playerXStockWidth,
                    playerProfile = boardState.getPlayerX()
                )
                Spacer(modifier = Modifier.height(10.dp))
                PlayDetails(
                    alignment = PlayerDetailsAlignment.END,
                    modifier = Modifier.onGloballyPositioned {
                        playerTwoLayoutCoordinates = it.some()
                    },
                    indicatorColor = player0IndicatorColor,
                    strokeWidth = playerOIndicatorColor,
                    playerProfile = boardState.getPlayerO()
                )
                Spacer(modifier = Modifier.weight(1f))
                Footer(boardState)
            }
        }
    }

}

@Composable
private fun Footer(boardState: BoardState) {
    Box {
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = boardState.gameEndsIn.isSome()
        ) {
            boardState.gameEndsIn.onSome {
                Timer(it, "Start your next game")
            }
        }
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = boardState.gameEndsIn.isNone()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AssistChip(
                    onClick = {
                        TicTacToeViewModel.stopOnGoingGame()
                    },
                    label = {
                        Text(
                            "Exit Game..🧐",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    },
                    shape = CircleShape,
                )
            }
        }
    }
}

