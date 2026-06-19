import SwiftUI
import appKit

struct ContentView: View {
    @EnvironmentObject private var rootHolder: RootHolder

    var body: some View {
        ComposeView(rootHolder: rootHolder)
            .ignoresSafeArea(.all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    let rootHolder: RootHolder

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(component: rootHolder.component)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
