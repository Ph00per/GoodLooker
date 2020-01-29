package com.phooper.goodlooker.model.data.site

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup

class Connector(private val baseUrl: String) {

    suspend fun connectTo(url: String = baseUrl, vararg options: String = emptyArray()): Connection =
        withContext(CoroutineScope(IO).coroutineContext) {
            Jsoup.connect(
                url + options.joinToString(separator = "")
            ).timeout(15000)
        }
}
