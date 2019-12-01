package com.phooper.goodlooker.ui.widgets.recyclerview.model

import com.example.delegateadapter.delegate.diff.IComparableItem

class NewsItemViewModel(
    val title: String?,
    val date: String,
    val comments: String,
    val views: String,
    val linkImage: String,
    val linkPost: String
) : IComparableItem {

    override fun id(): Any = linkPost

    override fun content() = title + date + comments + views + linkImage + linkPost
}