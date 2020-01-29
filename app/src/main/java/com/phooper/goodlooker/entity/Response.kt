package com.phooper.goodlooker.entity

import com.example.delegateadapter.delegate.diff.IComparableItem

data class Response(val listContent: List<IComparableItem>? = null, val error: String? = null, val isSuccessful : Boolean)