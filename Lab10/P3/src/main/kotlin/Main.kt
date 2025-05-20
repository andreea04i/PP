import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(args: Array<String>): Unit = runBlocking {
    println("Test de rulare!")
    val nQueue = Channel<Int>(Channel.UNLIMITED)

    listOf(5, 7, 3, 4).forEach { nQueue.trySend(it) }

    repeat(4) { workerId ->
        launch {
            for (n in nQueue) {
                delay(100)
                val result = factorial(n)
                println("Worker $workerId: factorial($n) = $result")
            }
        }
    }

    nQueue.close()
}
fun factorial(n: Int): Long = if (n <= 1) 1 else n * factorial(n - 1)
