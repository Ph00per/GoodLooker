package com.phooper.goodlooker.presentation.post

import com.phooper.goodlooker.App
import com.phooper.goodlooker.parser.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class PostPresenter : MvpPresenter<PostView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    fun showPost(link: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Parser().parsePost(link)
        }
    }

    fun onBackPressed() {
        router.exit()
    }
}
