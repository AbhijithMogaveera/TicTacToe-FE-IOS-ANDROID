package com.shared.tic_tac_toe.ui.components.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.getOrElse
import coil3.compose.AsyncImage
import com.shared.feature_profile.domain.models.User
import com.shared.tic_tac_toe.domain.models.PlayerProfile

@Composable
fun PlayDetails(
    alignment: PlayerDetailsAlignment,
    modifier: Modifier = Modifier,
    indicatorColor: Color,
    strokeWidth: Dp,
    playerProfile: PlayerProfile,
) {
    CurrentUser().onSome {
        when (alignment) {
            PlayerDetailsAlignment.START -> PlayDetailsOnStart(
                strokeWidth,
                indicatorColor,
                modifier,
                playerProfile,
                it
            )

            PlayerDetailsAlignment.END -> PlayDetailsOnEnd(
                strokeWidth,
                indicatorColor,
                modifier,
                playerProfile,
                it
            )
        }
    }
}

@Composable
private fun PlayDetailsOnStart(
    strokeWidth: Dp,
    indicatorColor: Color,
    modifier: Modifier,
    playerProfile: PlayerProfile,
    it: User
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.66f)
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .height(80.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .border(BorderStroke(strokeWidth, indicatorColor), CircleShape)
                .background(color = Color.Black.copy(alpha = 0.6f))
                .then(modifier)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(Modifier.width(10.dp))
                PlayerPic(playerProfile)
                Spacer(Modifier.width(10.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (it.userName == playerProfile.user_name) "You" else playerProfile.user_name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        playerProfile.bio.getOrElse { "" },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}

@Composable
private fun PlayDetailsOnEnd(
    strokeWidth: Dp,
    indicatorColor: Color,
    modifier: Modifier,
    playerProfile: PlayerProfile,
    it: User
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.66f)
                .padding(horizontal = 5.dp)
                .padding(vertical = 10.dp)
                .height(80.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .border(BorderStroke(strokeWidth, indicatorColor), CircleShape)
                .background(color = Color.Black.copy(alpha = 0.6f))
                .then(modifier)

        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (it.userName == playerProfile.user_name) "You" else playerProfile.user_name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = playerProfile.bio.getOrElse { "" },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                PlayerPic(playerProfile)
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
private fun PlayerPic(playerProfile: PlayerProfile) {
    Box(modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.Black)) {
        AsyncImage(
            model = playerProfile.profile_image.getOrNull(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            playerProfile.tile.name,
            modifier = Modifier.align(Alignment.BottomEnd).padding(5.dp).size(20.dp).background(color = Color.Black, shape = CircleShape),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}