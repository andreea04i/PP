import kotlinx.coroutines.*
import java.io.File

fun main() = runBlocking {
    val inputDir = File("input")

    if (!inputDir.exists() || !inputDir.isDirectory) {
        println("Folderul input nu există!")
        return@runBlocking
    }

    val files = inputDir.listFiles { f -> f.extension == "txt" } ?: return@runBlocking

    println("Procesare paralelă a ${files.size} fișiere...\n")

    val jobs = files.map { file ->
        launch(Dispatchers.IO) {
            println("Start procesare: ${file.name}")
            val lines = file.readLines()

            lines.forEach { line ->
                val num = line.trim().toIntOrNull()
                if (num != null) {
                    val fact = factorial(num)
                    println("→ ${file.name}: factorial($num) = $fact")
                } else {
                    println("→ ${file.name}: linie invalidă '$line'")
                }
            }

            println("Final procesare: ${file.name}")
        }
    }

    jobs.forEach { it.join() }
}

fun factorial(n: Int): Long =
    if (n <= 1) 1 else n * factorial(n - 1)
