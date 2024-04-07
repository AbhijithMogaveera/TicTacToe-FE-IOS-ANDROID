package com.abhijith.tic_tac_toe.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.some
import com.abhijith.tic_tac_toe.domain.useCases.TileState
import com.abhijith.tic_tac_toe.domain.viewmodels.TicTacToeViewModel
import com.abhijith.tic_tac_toe.ui.components.toColorInt

@Composable
fun BoardGrid(modifier: Modifier) {

    var tilesCoordinates by remember {
        mutableStateOf(listOf<Option<LayoutCoordinates>>(
            *(0..<9)
                .map { None }
                .toTypedArray()
        ))
    }
    var boardLayoutCoordinates: Option<LayoutCoordinates> by remember { mutableStateOf(None) }
    val boardState = TicTacToeViewModel.boardState.getOrNull() ?: return
    LazyVerticalGrid(

        columns = GridCells.Fixed(3),

        content = {
            items(count = 9) { index ->
                Tile(
                    tileState = boardState.board[index],
                    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                        tilesCoordinates = tilesCoordinates.mapIndexed { mIndex, option ->
                            if (mIndex == index)
                                layoutCoordinates.some()
                            else
                                option
                        }
                    }.clickable {
                        TicTacToeViewModel.onTileClick(index)
                    },
                    isFocused = boardState.winTileDiagonalStart.isSome { it == index }
                            || boardState.winTileDiagonalMiddle.isSome { it == index }
                            || boardState.winTileDiagonalEnd.isSome { it == index }
                )
            }
        },
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color("#FF407D".toColorInt()),
                    cornerRadius = CornerRadius(60f, 60f)
                )
            }
            .padding(10.dp)
            .drawBehind {
                (tilesCoordinates
                    .toList()
                    .takeIf { it.all { option -> option.isSome() } }
                    ?.mapNotNull { it.getOrNull() }
                    ?.some() ?: None)
                    .onSome { tilesCoordinates ->
                        val winTileDiagonalStart =
                            boardState.winTileDiagonalStart.getOrElse { return@drawBehind }
                        val winTileDiagonalEnd =
                            boardState.winTileDiagonalEnd.getOrElse { return@drawBehind }
                        val fromCoordinate = tilesCoordinates[winTileDiagonalStart]
                        val toCoordinate = tilesCoordinates[winTileDiagonalEnd]
                        boardLayoutCoordinates.onSome { parentLayoutCoordinates ->
                            drawLine(
                                end = parentLayoutCoordinates.localPositionOf(
                                    sourceCoordinates = fromCoordinate,
                                    relativeToSource = Offset.Zero
                                ).let {
                                    it.copy(
                                        it.x + toCoordinate.size.width / 2f,
                                        it.y + toCoordinate.size.height / 2f
                                    )
                                },
                                start = parentLayoutCoordinates.localPositionOf(
                                    sourceCoordinates = toCoordinate,
                                    relativeToSource = Offset.Zero
                                ).let {
                                    it.copy(
                                        it.x + toCoordinate.size.width / 2f,
                                        it.y + toCoordinate.size.height / 2f
                                    )
                                },
                                strokeWidth = 100f,
                                color = Color.Yellow,
                                cap = StrokeCap.Round
                            )
                        }
                    }
            }
            .then(modifier).onGloballyPositioned {
                boardLayoutCoordinates = it.some()
            }
    )
}

@Composable
fun Tile(
    tileState: TileState,
    modifier: Modifier = Modifier,
    isFocused: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1 / 1f)
            .padding(10.dp)
            .then(modifier)
            .clip(CircleShape)
            .let {
                if (isFocused)
                    it.border(color = Color.Yellow, width = 5.dp, shape = CircleShape)
                else
                    it
            }
            .background(
                color = Color("#FFCAD4".toColorInt()).copy(
                    alpha = when (tileState) {
                        TileState.NONE -> 0.5f
                        TileState.O, TileState.X -> 1f

                    }
                )
            )
    ) {
        val x = when (tileState) {
            TileState.NONE -> ""
            TileState.O -> "O"
            TileState.X -> "X"
        }
        Text(
            x,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}