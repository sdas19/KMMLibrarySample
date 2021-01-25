import io.ktor.client.engine.*

expect class Engine() {
    fun provideEngine(): HttpClientEngine
}