package com.phooper.goodlooker.presentation.picture

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.phooper.goodlooker.App
import com.phooper.goodlooker.util.ImageSaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import kotlin.math.absoluteValue


@InjectViewState
class PicturePresenter(private val imgLink: String) :
    MvpPresenter<PictureView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var glide: RequestManager

    var isWritePermissionGranted = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.apply {
            unlockOrientation()
            checkWritePermission()
            try {
                showImage(glide.load(imgLink))
            } catch (e: Exception) {
            }
        }
    }
    //TODO:: Load image in normal way
    // There's much better||right solution below as I think, but animation doesn't work with it
//        try {
//            glide.asDrawable().load(imgLink).into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    viewState.showImage(resource)
//                }
//                override fun onLoadCleared(placeholder: Drawable?) {}
//            })
//        } catch (e: Exception) {}

    init {
        App.daggerComponent.inject(this)
    }

    fun onBackPressed() {
        viewState.lockOrientation()
        router.exit()
    }

    fun onSwiped(firstY: Float, secondY: Float) {
        if ((firstY - secondY).absoluteValue > 150f) {
            viewState.lockOrientation()
            router.exit()
        }
    }

    fun downloadImage() {
        if (!isWritePermissionGranted) {
            viewState.apply {
                askForWritePermission()
            }
        } else {
            glide.asBitmap().load(imgLink).into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    CoroutineScope(Dispatchers.IO).launch {
                        ImageSaver().save(
                            resource,
                            name = ("([0-9a-zA-Z\\._-]+.(png|PNG|gif|GIF|jp[e]?g|JP[E]?G))").toRegex().find(
                                imgLink
                            )?.groupValues?.get(1)
                        )
                        withContext(Dispatchers.Main)
                        {
                            viewState.showMessage("Изображение сохранено в галерею!")
                        }
                    }
                }
            })
        }

    }

}


