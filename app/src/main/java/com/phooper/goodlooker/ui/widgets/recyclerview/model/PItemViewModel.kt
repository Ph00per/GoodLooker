package com.phooper.goodlooker.ui.widgets.recyclerview.model

import com.example.delegateadapter.delegate.diff.IComparableItem

class PItemViewModel(val text: String) :
    IComparableItem {

    override fun id(): Any = text

    override fun content() = text
}