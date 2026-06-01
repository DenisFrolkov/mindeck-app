import Foundation
import Combine
import composeKit

final class RootHolder: ObservableObject {
    let component = IosApplicationComponent()

    deinit {
        component.onStop()
        component.onDestroy()
    }
}
