package com.abhijith.tic_tac_toe
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.ComposeUIViewController
import com.abhijith.tic_tac_toe.ui.TicTacToeComponent
import platform.UIKit.UIViewController
class TicTacToeController(){
    fun TicTacToeContoller(): UIViewController {
        return ComposeUIViewController {
            Column(

            ) {
                TicTacToeComponent()
            }
        }
    }

}