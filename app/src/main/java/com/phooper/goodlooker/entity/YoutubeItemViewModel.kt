package com.phooper.goodlooker.entity

import com.example.delegateadapter.delegate.diff.IComparableItem

class YoutubeItemViewModel(val videoCode: String) :
    IComparableItem {

    override fun id(): Any = videoCode

    override fun content() = videoCode
}