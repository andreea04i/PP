package SafeBrowser

interface HTTPGet
{
    fun getResponse() : String
}

class GenericRequest(val url: String, val params: Map<String,String>? = null) : Cloneable{
    override fun clone() : GenericRequest{
        return GenericRequest(url,params?.toMap())
    }
}
 class GetRequest(private val genericReq : GenericRequest,private val timeout: Int) : HTTPGet
 {
     override fun getResponse() : String
     {
         return "GET Response from : ${genericReq.url}(timeout: ${timeout}ms)"
     }
     fun getGenericRequest():GenericRequest
     {
         return genericReq
     }
 }
class PostRequest(private val genericReq: GenericRequest)
{
    fun postData() : String
    {
        return "POST Response to : ${genericReq.url}"
    }
}
class CleanGetRequest(private val getRequest: GetRequest):HTTPGet
{
    private val parentalControlDisallow = listOf(
        "alcohol","drugs","porn","rape","bet","gambling","suicide","violence"
    )
    private fun blocked(url : String):Boolean{
        return parentalControlDisallow.any{badWord -> url.contains(badWord, ignoreCase = true)}
    }
    override fun getResponse(): String
    {
        val url = getRequest.getGenericRequest().url
        return if(blocked(url))
        {
            "Access denied by parental control for :$url"
        }else
        {
            getRequest.getResponse()
        }
    }
}
class KidsBrowser(private val cleanGet : CleanGetRequest,private val postReq : PostRequest?)
{
    fun start()
    {
        println("\n Kids Safe Browser\n")
        println("Waiting for permission to access the site: ")
        print(cleanGet.getResponse())

        if(postReq !=null)
        {
            println(" \n A POST Response is sent\n ")
            println(postReq.postData())
        }

    }
}

fun main()
{
    val genericReq = GenericRequest("https://www.maxbet.ro")
    val getReq = GetRequest(genericReq,3000)
    val cleanGetReq = CleanGetRequest(getReq)

    val postGen = GenericRequest("https://httpbin.org/post",mapOf("key" to "value"))
    val postReq = PostRequest(postGen)

    val browser = KidsBrowser(cleanGetReq,postReq)
    browser.start()

}

