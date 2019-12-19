package com.phooper.goodlooker.ui.global

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment() {
    abstract val layoutRes: Int
    var instanceStateSaved: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onPause() {
        super.onPause()
        instanceStateSaved = true
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    open fun onBackPressed() {}
}