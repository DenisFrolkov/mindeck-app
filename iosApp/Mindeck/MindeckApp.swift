//
//  MindeckApp.swift
//  iosApp
//
//  Created by Denis Frolkov on 5/28/26.
//

import SwiftUI
import sharedKit

@main
struct MindeckApp: App {
    init() {
        KoinIosKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
