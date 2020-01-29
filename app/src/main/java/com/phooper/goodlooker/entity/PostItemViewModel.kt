package com.phooper.goodlooker.entity

import android.os.Parcelable
import android.widget.ImageView
import com.example.delegateadapter.delegate.diff.IComparableItem
import kotlinx.android.parcel.Parcelize

class PostItemViewModel(
    val title: String,
    val date: String,
    val views: String,
    val imageLink: String,
    val linkPost: String
) : IComparableItem{

    override fun id(): Any = linkPost

    override fun content() = title + date + views + imageLink + linkPost
}