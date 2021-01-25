import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*

actual class Engine {
    actual fun provideEngine(): HttpClientEngine {
        return Ios.create()
    }
}