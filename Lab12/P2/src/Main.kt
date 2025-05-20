import java.io.File

fun caesarEncrypt(text: String, offset: Int): String = buildString {
    for (ch in text) {
        when {
            ch.isUpperCase() -> append('A' + (ch - 'A' + offset) % 26)
            ch.isLowerCase() -> append('a' + (ch - 'a' + offset) % 26)
            else -> append(ch)
        }
    }
}

fun proceseazaFisierCaesar(numeFisier: String, offset: Int) {
    val file = File(numeFisier)
    if (!file.exists()) {
        println("Fișierul nu există: $numeFisier")
        return
    }

    val textProcesat = file.readLines().joinToString("\n") { linie ->
        linie.split(" ").joinToString(" ") { cuvant ->
            if (cuvant.length in 4..7) caesarEncrypt(cuvant, offset) else cuvant
        }
    }
    println("2) Text procesat cu cifrul Caesar:")
    println(textProcesat)
}

fun main() {
    proceseazaFisierCaesar("input.txt", 3)
}

