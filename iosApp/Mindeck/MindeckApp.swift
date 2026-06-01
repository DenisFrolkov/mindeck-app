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
    @StateObject private var rootHolder = RootHolder()

    init() {
        KoinIosKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(rootHolder)
        }
    }
}
