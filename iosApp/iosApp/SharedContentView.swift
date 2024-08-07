//
//  TicTacToe.swift
//  iosApp
//
//  Created by Abhijith Mogaveera on 19/02/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SharedContentView: View{
    var body: some View{
        ComposeView().ignoresSafeArea(.keyboard)
    }
}
