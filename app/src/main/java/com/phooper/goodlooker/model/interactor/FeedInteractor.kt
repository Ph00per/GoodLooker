package com.phooper.goodlooker.model.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.entity.PostItemViewModel
import com.phooper.goodlooker.entity.Response
import com.phooper.goodlooker.model.data.site.Connector
import com.phooper.goodlooker.util.Constants.Companion.CONNECTION_ERROR
import com.phooper.goodlooker.util.Constants.Companion.NO_MORE_CONTENT_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import javax.inject.Inject

class FeedInteractor {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var connector: Connector

    private val mapCategories =
        mapOf(
            0 to "page/",
            1 to "category/uprazhneniya/page/",
            2 to "category/fitnes-inventar/page/",
            3 to "category/fitnes-programmy/page/",
            4 to "category/fitnes-sovety/page/",
            5 to "category/pitanie/page/",
            6 to "category/youtube-trenirovki/page/",
            7 to "category/poleznoe/page/"
        )

    suspend fun getFeedByCategoryAndPage(category: Int, page: Int): Response =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            try {
                return@withContext Response(listContent = mutableListOf<IComparableItem>().apply {
                    connector.connectTo(
                        options = *arrayOf(
                            mapCategories.getValue(category),
                            page.toString(), "?s"
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

                }, isSuccessful = true)
            } catch (e: HttpStatusException) {
                return@withContext Response(error = NO_MORE_CONTENT_ERROR, isSuccessful = false)
            } catch (e: Exception) {
                return@withContext Response(error = CONNECTION_ERROR, isSuccessful = false)
            }
        }

}