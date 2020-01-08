package com.phooper.goodlooker.parser

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.models.Hyperlink
import com.phooper.goodlooker.ui.widgets.recyclerview.model.*
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Parser {

    fun parseFeed(url: String): MutableList<IComparableItem> {
        val listNews = mutableListOf<IComparableItem>()
        val element =
            Jsoup.connect(
                url
            )
                .get()
                .select("article[class=item-cat_site],[class=item-cat_site last-element]")

        for (i in 0 until element.size) {

            val title = element.select("div[class=title-cat_site]")
                .select("span")
                .eq(i)
                .text()

            val linkImage =
                element.select("div[class=thumb-cat_site]")
                    .select("img")
                    .eq(i)
                    .attr("src")

            val linkPost =
                element.select("div[class=thumb-cat_site]")
                    .select("a")
                    .eq(i)
                    .attr("href")

            val date = element.select("meta[itemprop=datePublished]")
                .eq(i)
                .attr("content").substring(0, 10).split("-").asReversed().joinToString(".")

            val comments = element.select("div[class=meta-st-item_posts]")
                .select("div[class=comments-st_posts]")
                .eq(i)
                .text()

            val views = element.select("div[class=meta-st-item_posts]")
                .select("div[class=views-st_posts]")
                .eq(i)
                .text()

            listNews.add(
                PostItemViewModel(
                    title, date, comments, views, linkImage, linkPost
                )
            )
        }
        return listNews
    }

    fun parseSearchFeed(url: String): MutableList<IComparableItem> {
        val feedList = mutableListOf<IComparableItem>()
        Jsoup.connect(
            url
        )
            .get()
            .select("article[class=item-cat_site], article[class=item-cat_site last-element]")
            .forEach {
                feedList.add(
                    PostItemViewModel(
                        title = it.select("div[class=title-cat_site]").select("span").text(),
                        date = it.select("meta[itemprop=datePublished]").attr("content").substring(
                            0,
                            10
                        ).split("-").asReversed().joinToString("."),
                        comments = it.select("div[class=meta-st-item_posts]").select("div[class=comments-st_posts]").text(),
                        views = it.select("div[class=meta-st-item_posts]").select("div[class=views-st_posts]").text(),
                        imageLink = it.select("div[class=thumb-cat_site]").select("img").attr("src"),
                        linkPost = it.select("div[class=thumb-cat_site]").select("a").attr("href")
                    )
                )
            }
        return feedList
    }

    fun parsePost(url: String): MutableList<IComparableItem> {

        val listPost = mutableListOf<IComparableItem>()

            Jsoup.connect(
                url
            )
                .get()
                .select("article[class=article-content_vn]")
                .select("p, h1, h2, h3, ol, ul, div[class=article-header_meta]")
                .forEach { it ->
                    when {
                        //Header 1
                        (it.select("h1").isNotEmpty()) -> {
                            listPost.add(H1ItemViewModel(it.text()))
                        }
                        //Meta
                        (it.select("div[class=article-header_meta]").isNotEmpty()) -> {
                            listPost.add(
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
                            listPost.add(H2ItemViewModel(it.text()))
                        }
                        //Header 3
                        (it.select("h3").isNotEmpty()) -> {
                            listPost.add(H3ItemViewModel(it.text()))
                        }
                        //Button
                        (it.select("p").select("a").attr("class").contains("button")) -> {
                            listPost.add(
                                ButtonItemViewModel(
                                    it.text(),
                                    it.select("a").attr("href")
                                )
                            )
                        }
                        //Image
                        (it.select("p").select("img")).isNotEmpty() -> {
                            if (it.select("img").attr("src").startsWith("/"))
                                listPost.add(
                                    ImageItemViewModel(
                                        BASE_URL + it.select("img").attr(
                                            "src"
                                        )
                                    )
                                )
                            else
                                listPost.add(ImageItemViewModel(it.select("img").attr("src")))
                        }
                        //Unordered list
                        (it.select("ul").isNotEmpty()) -> {
                            if (it.select("a").isNotEmpty()) {
                                it.select("ul").select("li").forEach {
                                    listPost.add(ULItemViewModel(it.text(), hyperlinkFinder(it)))
                                }
                            } else {
                                it.select("ul").select("li").forEach {
                                    listPost.add(ULItemViewModel(it.text()))
                                }
                            }
                        }
                        //Ordered list
                        (it.select("ol").isNotEmpty()) -> {
                            var count = 1
                            if (it.select("a").isNotEmpty()) {
                                it.select("ol").select("li").forEach {
                                    listPost.add(
                                        OLItemViewModel(
                                            (count++).toString() + ")",
                                            it.text(),
                                            hyperlinkFinder(it)
                                        )
                                    )
                                }
                            } else {
                                it.select("ol").select("li").forEach {
                                    listPost.add(
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
                                listPost.add(
                                    YoutubeItemViewModel(it)
                                )
                            }

                        }
                        //Paragraph
                        ((it.select("p").select("img")).isEmpty() && it.select("p").isNotEmpty()) -> {
                            if (it.select("a").isNotEmpty()) {
                                listPost.add(PItemViewModel(it.text(), hyperlinkFinder(it)))
                            } else {
                                listPost.add(PItemViewModel(it.text()))
                            }
                        }
                    }

                }
        return listPost
    }
}


private fun hyperlinkFinder(element: Element) =
    mutableListOf<Hyperlink>().apply {
        element.select("a").forEach {
            this.add(Hyperlink(it.text(), it.attr("href")))
        }
    }

