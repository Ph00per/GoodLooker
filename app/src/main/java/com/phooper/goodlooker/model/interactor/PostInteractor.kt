package com.phooper.goodlooker.model.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.entity.*
import com.phooper.goodlooker.model.data.db.dao.FavouritePostsDao
import com.phooper.goodlooker.model.data.site.Connector
import com.phooper.goodlooker.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import javax.inject.Inject

class PostInteractor {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var connector: Connector

    @Inject
    lateinit var favouritePostsDao: FavouritePostsDao

    suspend fun getPostByLink(url: String): Response =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            try {
                return@withContext Response(mutableListOf<IComparableItem>().apply {
                    connector.connectTo(url).get()
                        .select("article[class=article-content_vn]")
                        .select("p, h1, h2, h3, ol, ul, div[class=article-header_meta]")
                        .forEach { it ->
                            when {
                                //Header 1
                                (it.select("h1").isNotEmpty()) -> {
                                    this.add(
                                        H1ItemViewModel(
                                            it.text()
                                        )
                                    )
                                }
                                //Meta
                                (it.select("div[class=article-header_meta]").isNotEmpty()) -> {
                                    this.add(
                                        MetaItemViewModel(
                                            it.select("div[class=article-header_meta]").text().substring(
                                                14,
                                                24
                                            ),
                                            it.select("div[class=article-header_meta]").text().substring(
                                                42,
                                                44
                                            ) + " мин."
                                        )
                                    )
                                }
                                //Header 2
                                (it.select("h2").isNotEmpty()) -> {
                                    this.add(
                                        H2ItemViewModel(
                                            it.text()
                                        )
                                    )
                                }
                                //Header 3
                                (it.select("h3").isNotEmpty()) -> {
                                    this.add(
                                        H3ItemViewModel(
                                            it.text()
                                        )
                                    )
                                }
                                //Button
                                (it.select("p").select("a").attr("class").contains("button")) -> {
                                    this.add(
                                        ButtonItemViewModel(
                                            it.text(),
                                            it.select("a").attr("href")
                                        )
                                    )
                                }
                                //Image
                                (it.select("p").select("img")).isNotEmpty() -> {
                                    if (it.select("img").attr("src").startsWith("/"))
                                        this.add(
                                            ImageItemViewModel(
                                                Constants.BASE_URL + it.select("img").attr(
                                                    "src"
                                                )
                                            )
                                        )
                                    else
                                        this.add(
                                            ImageItemViewModel(
                                                it.select("img").attr("src")
                                            )
                                        )
                                }
                                //Unordered list
                                (it.select("ul").isNotEmpty()) -> {
                                    if (it.select("a").isNotEmpty()) {
                                        it.select("ul").select("li").forEach {
                                            this.add(
                                                ULItemViewModel(
                                                    it.text(),
                                                    hyperlinkFinder(
                                                        it
                                                    )
                                                )
                                            )
                                        }
                                    } else {
                                        it.select("ul").select("li").forEach {
                                            this.add(
                                                ULItemViewModel(
                                                    it.text()
                                                )
                                            )
                                        }
                                    }
                                }
                                //Ordered list
                                (it.select("ol").isNotEmpty()) -> {
                                    var count = 1
                                    if (it.select("a").isNotEmpty()) {
                                        it.select("ol").select("li").forEach {
                                            this.add(
                                                OLItemViewModel(
                                                    (count++).toString() + ")",
                                                    it.text(),
                                                    hyperlinkFinder(
                                                        it
                                                    )
                                                )
                                            )
                                        }
                                    } else {
                                        it.select("ol").select("li").forEach {
                                            this.add(
                                                OLItemViewModel(
                                                    (count++).toString() + ")",
                                                    it.text()
                                                )
                                            )
                                        }
                                    }
                                }
                                //YouTube link
                                ((it.select("p").select("iframe").attr("src")).isNotEmpty()) -> {
                                    (("(?im)\\b(?:https?:\\/\\/)?(?:w{3}\\.)?youtu(?:be)?\\.(?:com|be)\\/(?:(?:\\??v=?i?=?\\/?)|watch\\?vi?=|watch\\?.*?&v=|embed\\/|)([A-Z0-9_-]{11})\\S*(?=\\s|\$)")
                                        .toRegex().find(
                                            it.select("iframe").attr("src")
                                        )?.groupValues?.get(1))?.let {
                                        this.add(
                                            YoutubeItemViewModel(
                                                it
                                            )
                                        )
                                    }

                                }
                                //Paragraph
                                ((it.select("p").select("img")).isEmpty() && it.select("p").isNotEmpty()) -> {
                                    if (it.select("a").isNotEmpty()) {
                                        this.add(
                                            PItemViewModel(
                                                it.text(),
                                                hyperlinkFinder(
                                                    it
                                                )
                                            )
                                        )
                                    } else {
                                        this.add(
                                            PItemViewModel(
                                                it.text()
                                            )
                                        )
                                    }
                                }
                            }

                        }
                }, isSuccessful = true)
            } catch (e: Exception) {
                return@withContext Response(
                    error = Constants.CONNECTION_ERROR,
                    isSuccessful = false
                )
            }
        }


    private fun hyperlinkFinder(element: Element) =
        mutableListOf<Hyperlink>().apply {
            element.select("a").forEach {
                this.add(Hyperlink(it.text(), it.attr("href")))
            }
        }


    suspend fun isPostFavourite(postLink: String) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            favouritePostsDao.getByLink(postLink)?.let {
                return@withContext true
            }
            false
        }

    suspend fun deletePostFromFav(link: String) {
        favouritePostsDao.deleteByLink(link)
    }

    suspend fun addNewFavPost(link: String, header: String) {
        favouritePostsDao.addNew(
            FavouritePost(
                0, link, header
            )
        )
    }

}