package com.phooper.goodlooker.ui.widgets.recyclerview.model

import com.example.delegateadapter.delegate.diff.IComparableItem

class ButtonItemViewModel(val text : String, val link : String): IComparableItem {

    override fun id(): Any = link

    override fun content() = text + link
}