package com.phooper.goodlooker.entity

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.entity.Hyperlink

class ULItemViewModel(val text: String, val hyperlinks : List<Hyperlink>? = null) :
    IComparableItem {

    override fun id(): Any = text

    override fun content() = text
}