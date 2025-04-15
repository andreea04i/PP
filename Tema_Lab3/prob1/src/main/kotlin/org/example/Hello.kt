package org.example

import org.jsoup.Jsoup

fun procesareJsoup(url: String): MutableMap<String, MutableMap<String, String>> {
    val document = Jsoup.connect(url).get()
    val list = mutableMapOf<String, MutableMap<String, String>>()

    val item_list = document.select("item")
    for (item in item_list) {
        val titlu = item.select("title").text()

        val atribute = mutableMapOf(
            "Link" to item.select("link").text(),
            "Descriere" to item.select("description").text(),
            "Data Publicatiei" to item.select("pubDate").text()
        )
        list[titlu] = atribute
    }
    return list
}

fun main() {
    val url = "http://rss.cnn.com/rss/edition.rss"
    val list = procesareJsoup(url)
    list.forEach {
        println("${it.key}: ")
        println("Link: ${it.value["Link"]}")
        println("Descriere: ${it.value["Descriere"]}")
        println("Data Publicatiei: ${it.value["Data Publicatiei"]}")
        println()
    }
}