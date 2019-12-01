package com.phooper.goodlooker.parser

import android.util.Log
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.ui.widgets.recyclerview.model.NewsItemViewModel
import org.jsoup.Jsoup

class Parser {

    fun parseNews(url: String): MutableList<IComparableItem> {
        val listNews = mutableListOf<IComparableItem>()
        val element =
            Jsoup.connect(
                url
            )
                .get()
                .select("article[class=item-cat_site],[class=item-cat_site last-element]")

        Log.d("Selected elements size:", element.size.toString())

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
                NewsItemViewModel(
                    title, date, comments, views, linkImage, linkPost
                )
            )
        }
        return listNews
    }

    fun parsePost(url: String): MutableList<IComparableItem> {

        val listPost = mutableListOf<IComparableItem>()

        Jsoup.connect(
            url
        )
            .get()
            .select("article[class=article-content_vn]").select("p, h1, h2, h3, ol, ul")
            .forEach { it ->
                when {

                    (it.select("h1").isNotEmpty()) -> {
                        Log.d("h1 found:", it.text())
                    }
                    (it.select("h2").isNotEmpty()) -> {
                        Log.d("h2 found:", it.text())
                    }
                    (it.select("h3").isNotEmpty()) -> {
                        Log.d("h3 found:", it.text())
                    }

                    (it.select("p").select("a").attr("class").contains("button")) -> {
                        Log.d("button found", "!")
                        Log.d("button's text:", it.text())
                        Log.d("button's link:", it.select("a").attr("href"))
                    }

                    (it.select("p").select("img")).isNotEmpty() -> {
                        Log.d("image found", it.select("img").attr("src"))
                    }
                    (it.select("ul").isNotEmpty()) -> {
                        it.select("ul").select("li").forEach {
                            Log.d("ul found, li: ", it.text())
                        }
                    }
                    (it.select("ol").isNotEmpty()) -> {
                        var count = 1
                        it.select("ol").select("li").forEach {

                            Log.d("ol found, li: ", (count++).toString() + ") " + it.text() )
                        }
                    }

                    ((it.select("p").select("iframe").attr("src")).isNotEmpty()) ->
                        Log.d("youtube video found: ", it.select("iframe").attr("src"))

                    ((it.select("p").select("img")).isEmpty() && it.select(
                        "p"
                    ).isNotEmpty()) ->
                        Log.d("p found: ", it.text())

                    else -> Log.d("unique element found: ", it.toString())
                }

            }

        return listPost
    }
}