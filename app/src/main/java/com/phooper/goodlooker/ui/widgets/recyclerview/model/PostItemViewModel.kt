package com.phooper.goodlooker.ui.widgets.recyclerview.model

import android.os.Parcelable
import android.widget.ImageView
import com.example.delegateadapter.delegate.diff.IComparableItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class PostItemViewModel(
    val title: String,
    val date: String,
    val comments: String,
    val views: String,
    val imageLink: String,
    val linkPost: String
) : IComparableItem, Parcelable {

    override fun id(): Any = linkPost

    override fun content() = title + date + comments + views + imageLink + linkPost
}