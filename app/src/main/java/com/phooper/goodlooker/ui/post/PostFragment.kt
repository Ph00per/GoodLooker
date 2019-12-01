package com.phooper.goodlooker.ui.post

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.presentation.post.PostView
import com.phooper.goodlooker.ui.global.BaseFragment
import moxy.presenter.InjectPresenter

class PostFragment : BaseFragment(), PostView {

    override val layoutRes = R.layout.fragment_post

    @InjectPresenter
    lateinit var presenter: PostPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onResume() {
        if (!instanceStateSaved) {
            presenter.showPost(arguments?.getString(LINK) as String)
        }
        super.onResume()
    }

    override fun onBackPressed() {

        presenter.onBackPressed()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }



    companion object {
        private const val LINK = "link"
        fun create(linkPost: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(LINK, linkPost)
                }
            }
    }
}