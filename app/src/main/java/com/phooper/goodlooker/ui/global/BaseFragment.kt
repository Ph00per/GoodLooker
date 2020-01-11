package com.phooper.goodlooker.ui.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phooper.goodlooker.App
import moxy.MvpAppCompatFragment


abstract class BaseFragment : MvpAppCompatFragment() {
    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    open fun onBackPressed() {}


}