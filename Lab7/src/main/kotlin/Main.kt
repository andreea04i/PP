
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryLogRecord(
    val time : LocalDateTime,
    val command : String
):Comparable<HistoryLogRecord>{
    override fun compareTo(other: HistoryLogRecord): Int = this.time.compareTo(other.time)
}

fun extraction(filepath : String) : List <HistoryLogRecord>
{
    val data_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val all_logs = mutableListOf<HistoryLogRecord>()
    var time : LocalDateTime? = null
    var command : String? = null

    File(filepath).forEachLine { line ->
        if(line.startsWith("Start-Date: "))
        {
            val dataText = line.removePrefix("Start-Date: ").trim()
            time = LocalDateTime.parse(dataText,data_format)
        }
        if(line.startsWith("Commandline: "))
        {
            command = line.removePrefix("Commandline: ").trim()
        }
        if(time!=null && command!=null) {
            all_logs.add(HistoryLogRecord(time!!, command!!))
            time = null
            command = null
        }
    }

    return all_logs.takeLast(50)
}
fun<T : Comparable<T>>maxim(a: T,b: T): T
{
    return if(a > b) a else b
}
fun <T>update(map: MutableMap<LocalDateTime,T>,old: T,new: T)where T:HistoryLogRecord
{
    val key = old.time
    if(map.containsKey(key))
    {
        map[key] = new;
    }
}
fun main()
{
    val filepath = "src/history.log"
    val logs = extraction(filepath)

    val map_history = mutableMapOf<LocalDateTime,HistoryLogRecord>()
    for(log in logs)
    {
        map_history[log.time]=log
    }
    val date_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    println("Ultimele 50 de comenzi: ")
    map_history.values.forEach{ println("${it.time.format(date_format)}:${it.command}") }

}