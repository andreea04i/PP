import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Note(val author: String, val content: String) {
    private val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    fun saveToFile() {
        val fileName = "notes/\${author}_\${timestamp.replace(":", "-").replace(" ", "_")}.txt"
        File("notes").mkdirs()
        File(fileName).writeText("Autor: $author\nDată: $timestamp\nConținut:\n$content")
        println("Notița a fost salvată: $fileName")
    }
}

class User(val name: String) {
    fun createNote(content: String): Note {
        return Note(name, content)
    }
}

class NoteManager {
    fun listNotes() {
        val dir = File("notes")
        val files = dir.listFiles() ?: emptyArray()
        if (files.isEmpty()) {
            println("Nu există notițe salvate.")
        } else {
            files.forEachIndexed { index, file -> println("\${index + 1}. \${file.name}") }
        }
    }

    fun loadNote(fileName: String) {
        val file = File("notes/$fileName")
        if (file.exists()) {
            println(file.readText())
        } else {
            println("Fișierul nu există.")
        }
    }

    fun deleteNote(fileName: String) {
        val file = File("notes/$fileName")
        if (file.exists() && file.delete()) {
            println("Notița $fileName a fost ștearsă.")
        } else {
            println("Eroare la ștergerea notiței.")
        }
    }
}

fun main() {
    val noteManager = NoteManager()
    println("Introduceți numele utilizatorului: ")
    val userName = readLine() ?: "Anonim"
    val user = User(userName)

    while (true) {
        println("\n1. Afișare notițe\n2. Încărcare notiță\n3. Creare notiță\n4. Ștergere notiță\n5. Ieșire")
        when (readLine()) {
            "1" -> noteManager.listNotes()
            "2" -> {
                println("Introduceți numele fișierului: ")
                val fileName = readLine() ?: ""
                noteManager.loadNote(fileName)
            }
            "3" -> {
                println("Introduceți conținutul notiței: ")
                val content = readLine() ?: ""
                val note = user.createNote(content)
                note.saveToFile()
            }
            "4" -> {
                println("Introduceți numele fișierului: ")
                val fileName = readLine() ?: ""
                noteManager.deleteNote(fileName)
            }
            "5" -> return
            else -> println("Opțiune invalidă.")
        }
    }
}
