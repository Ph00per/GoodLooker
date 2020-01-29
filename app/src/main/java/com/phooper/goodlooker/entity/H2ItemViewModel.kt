package com.phooper.goodlooker.entity

import com.example.delegateadapter.delegate.diff.IComparableItem

class H2ItemViewModel(val headerText: String) :
    IComparableItem {

    override fun id(): Any = headerText

    override fun content() = headerText
}