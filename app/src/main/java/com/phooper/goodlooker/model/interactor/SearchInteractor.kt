package com.phooper.goodlooker.model.interactor

import android.util.Log
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.entity.PostItemViewModel
import com.phooper.goodlooker.entity.Response
import com.phooper.goodlooker.entity.SearchHistory
import com.phooper.goodlooker.model.data.db.dao.SearchHistoryDao
import com.phooper.goodlooker.model.data.site.Connector
import com.phooper.goodlooker.util.Constants.Companion.CONNECTION_ERROR
import com.phooper.goodlooker.util.Constants.Companion.NO_MORE_CONTENT_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import java.io.IOException
import javax.inject.Inject

class SearchInteractor {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var connector: Connector

    @Inject
    lateinit var searchHistoryDao: SearchHistoryDao

    suspend fun getFeedBySearchTextAndPage(searchText: String, page: Int): Response =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            try {
                mutableListOf<IComparableItem>().apply {
                    connector.connectTo(
                        options = *arrayOf(
                            "page/",
                            page.toString(),
                            "?s=",
                            searchText
                        )
                    ).get()
                        .select("article[class=item-cat_site], article[class=item-cat_site last-element]")
                        .forEach {
                            this.add(
                                PostItemViewModel(
                                    title = it.select("div[class=title-cat_site]").select("span").text(),
                                    date = it.select("meta[itemprop=datePublished]").attr("content").substring(
                                        0,
                                        10
                                    ).split("-").asReversed().joinToString("."),
                                    views = it.select("div[class=meta-st-item_posts]").select("div[class=views-st_posts]").text(),
                                    imageLink = it.select("div[class=thumb-cat_site]").select("img").attr(
                                        "src"
                                    ),
                                    linkPost = it.select("div[class=thumb-cat_site]").select("a").attr(
                                        "href"
                                    )
                                )
                            )
                        }
                }.let {
                    Log.d("list: ", it.joinToString())
                    if (!it.isNullOrEmpty()) {
                        return@withContext Response(listContent = it, isSuccessful = true)
                    } else {
                        return@withContext Response(
                            error = NO_MORE_CONTENT_ERROR,
                            isSuccessful = false
                        )
                    }
                }
            } catch (e: HttpStatusException) {
                return@withContext Response(error = NO_MORE_CONTENT_ERROR, isSuccessful = false)
            } catch (e: IOException) {
                Log.d("list: ", "ERROR")
                return@withContext Response(error = CONNECTION_ERROR, isSuccessful = false)
            }
        }


    suspend fun getSearchHistory() = searchHistoryDao.getAll()

    suspend fun addNewSearchRequest(searchText: String) {
        searchHistoryDao.insert(
            SearchHistory(
                0,
                searchText
            )
        )

    }

    suspend fun deleteSearchRequestById(id: Int) {
        searchHistoryDao.deleteById(id)
    }
}