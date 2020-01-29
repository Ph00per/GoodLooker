package com.phooper.goodlooker.ui.drawer

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import com.muddzdev.styleabletoast.StyleableToast
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.drawer.DrawerPresenter
import com.phooper.goodlooker.presentation.drawer.DrawerView
import com.phooper.goodlooker.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_drawer.*
import moxy.presenter.InjectPresenter

class DrawerFragment : BaseFragment(),
    DrawerView {
    override val layoutRes = R.layout.fragment_drawer

    @InjectPresenter
    lateinit var presenter: DrawerPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        drawer_menu_layout?.forEachIndexed { index, view ->
            view.setOnClickListener {
                presenter.menuItemClicked(index)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    fun onScreenChanged(index: Int) {
        presenter.onScreenChanged(index)
    }

    override fun selectMenuItem(index: Int) {
        drawer_menu_layout?.forEach {
            it.isSelected = drawer_menu_layout.indexOfChild(it) == index
        }
    }

    override fun showMessage(msg: String) {
        StyleableToast.makeText(context!!, msg, R.style.toast).show()
    }
}