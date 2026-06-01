import SwiftUI
import composeKit

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
        MainViewControllerKt.MainViewController(rootComponent: rootHolder.component.rootComponent)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
