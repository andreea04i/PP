import khttp.get
import org.jsoup.Jsoup
import org.json.JSONObject


interface Parser {
    fun parse(text: String): Map<String, Any>
}


class JsonParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        val jsonObject = JSONObject(text)
        return jsonObject.toMap()
    }
}


class XmlParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        val document = Jsoup.parse(text, "", org.jsoup.parser.Parser.xmlParser())
        return mapOf("root" to document.outerHtml())
    }
}


class YamlParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        return mapOf("yaml_content" to text.lines())
    }
}


class Crawler(private val url: String) {
    fun getResource(): String {
        val response = get(url)
        return response.text
    }

    fun processContent(contentType: String, parser: Parser): Map<String, Any> {
        val content = getResource()
        return parser.parse(content)
    }
}


fun main() {
    val url = "https://jsonplaceholder.typicode.com/todos/1"
    val crawler = Crawler(url)

    val jsonParser = JsonParser()
    val parsedData = crawler.processContent("application/json", jsonParser)

    println(parsedData)
}
