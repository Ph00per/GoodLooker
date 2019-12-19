package com.phooper.goodlooker.ui.widgets.recyclerview.model

import com.example.delegateadapter.delegate.diff.IComparableItem

class MetaItemViewModel(val date: String, val timeToRead: String) :
    IComparableItem {

    override fun id(): Any = date

    override fun content() = date + timeToRead
}