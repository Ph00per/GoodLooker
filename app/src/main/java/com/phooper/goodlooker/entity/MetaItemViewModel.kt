package com.phooper.goodlooker.entity

import com.example.delegateadapter.delegate.diff.IComparableItem

class MetaItemViewModel(val date: String, val timeToRead: String) :
    IComparableItem {

    override fun id(): Any = date

    override fun content() = date + timeToRead
}