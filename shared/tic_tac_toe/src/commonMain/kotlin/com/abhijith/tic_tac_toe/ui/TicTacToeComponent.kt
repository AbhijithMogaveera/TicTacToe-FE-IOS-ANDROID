package com.abhijith.tic_tac_toe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import arrow.core.None
import com.abhijith.tic_tac_toe.domain.models.Player
import com.abhijith.tic_tac_toe.domain.viewmodels.TicTacToeViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun TicTacToeComponent() {
    if (TicTacToeViewModel.requestState != TicTacToeViewModel.PlayRequestState.PlayStarted) {
        if(TicTacToeViewModel.requestState == TicTacToeViewModel.PlayRequestState.Ended){
            GoForNextMatch()
        }else {
            PartnerPlayerConnectionStatePopUp()
            ChoosePlayer()
        }
    } else {
        TTTGame()
    }
}

@Composable
fun GoForNextMatch() {
    Box(modifier = Modifier.fillMaxSize()){
        Button(
            onClick = {
                TicTacToeViewModel.lookForNextMatch()
            },
            modifier = Modifier.align(Alignment.Center)
        ){
            Text("Go for next match")
        }
    }
}

@Composable
private fun PartnerPlayerConnectionStatePopUp() {
    val showAlertDialog by remember {
        derivedStateOf {
            TicTacToeViewModel.requestState == TicTacToeViewModel.PlayRequestState.Waiting
                    || TicTacToeViewModel.requestState == TicTacToeViewModel.PlayRequestState.Error
                    || TicTacToeViewModel.requestState == TicTacToeViewModel.PlayRequestState.Declined
        }
    }
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        TicTacToeViewModel.lookForNextMatch()
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            title = {
                when (TicTacToeViewModel.requestState) {
                    TicTacToeViewModel.PlayRequestState.Waiting -> Text("Waiting for player to accept your request")
                    TicTacToeViewModel.PlayRequestState.Declined -> Text("Player declined your request")
                    TicTacToeViewModel.PlayRequestState.Error -> Text("Oops! something went wrong")
                    else -> {/*Do nothing*/
                    }
                }
            },
            properties = DialogProperties(),
            modifier = Modifier.shadow(
                elevation = 10.dp, shape = RoundedCornerShape(10.dp)
            )

        )
    }
}


@Composable
fun ChoosePlayer(
    players: List<Player> =  TicTacToeViewModel.player.collectAsState().value,
    onPlayerSelected: (Player) -> Unit = {},
    onSearchValueChange: (String) -> Unit = {}
) {
    LaunchedEffect(key1 = Unit) {
        TicTacToeViewModel.fetchPlayers(searchKey = None)
    }

    Column {
        Spacer(Modifier.height(20.dp))
        SearchBar(
            value = "",
            onValueChange = onSearchValueChange
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn {
            items(players) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        TicTacToeViewModel.requestToPlayerToPlay(it)
                    }
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Row {
                            Box(
                                Modifier.size(40.dp).drawBehind {
                                    drawCircle(
                                        color = Color.Red
                                    )
                                }
                            ) {

                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    it.name, style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                )
                                Text(if (it.isAvailableToPlay) "Available" else "Not available")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TTTGame() {
    LaunchedEffect(key1 = Unit){
        delay(5.seconds)
        TicTacToeViewModel.endGame()
    }
    Box(modifier = Modifier.padding(10.dp).drawBehind {
        drawRoundRect(
            color = Color("#E78895".toColorInt()),
            cornerRadius = CornerRadius(30f, 30f)
        )
    }.aspectRatio(1 / 1.5f).fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            content = {
                items(count = 9) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1 / 1f)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = Color("#FFE4C9".toColorInt()))
                    ) {}
                }

            }
        )
    }
}

fun String.toColorInt(): Int {
    if (this[0] == '#') {
        var color = substring(1).toLong(16)
        if (length == 7) {
            color = color or 0x00000000ff000000L
        } else if (length != 9) {
            throw IllegalArgumentException("Unknown color")
        }
        return color.toInt()
    }
    throw IllegalArgumentException("Unknown color")
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        placeholder = {
            Text("Your friend name...")
        }
    )
}