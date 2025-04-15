import java.io.File

fun main() {
    val filePath = "the_little_prince.txt"
    val text = File(filePath).readText()

    val processedText = EbookProcessor.processText(text)

    File("test4.txt").writeText(processedText)

    println("Procesare completă! Textul curățat a fost salvat în 'test4.txt'.")
}

object EbookProcessor {
    fun processText(text: String): String {
        var clean_txt = text

       // clean_txt = removeMultipleSpaces(clean_txt)
        clean_txt = removeMultipleNewLines(clean_txt)
        clean_txt = removePageNumbers(clean_txt)
        clean_txt = removeChapterTitles(clean_txt)


        return clean_txt
    }

    //private fun removeMultipleSpaces(text: String): String {
       // return text.replace(Regex(" +"), " ")
    //}

    private fun removeMultipleNewLines(text: String): String {
        return text.replace(Regex("(\r?\n)+"), "\n")
    }

    private fun removePageNumbers(text: String): String {
        return text.replace(Regex("\\s+\\d+\\s+"), " ")
    }

    private fun removeChapterTitles(text: String): String {
        return text.replace(Regex("(?i)\\s*CHAPTER\\s+[IVXLCDM0-9a-zA-Z:\\-]*\\s*"), " ")
    }

}
