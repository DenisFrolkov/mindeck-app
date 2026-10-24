import Foundation
import Combine
import appKit

final class RootHolder: ObservableObject {
    let component = IosApplicationComponent()

    deinit {
        component.onDestroy()
    }
}
