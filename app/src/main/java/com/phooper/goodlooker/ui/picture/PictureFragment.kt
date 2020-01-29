package com.phooper.goodlooker.ui.picture

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestBuilder
import com.muddzdev.styleabletoast.StyleableToast
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.picture.PicturePresenter
import com.phooper.goodlooker.presentation.picture.PictureView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.util.shareText
import kotlinx.android.synthetic.main.fragment_picture.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class PictureFragment : BaseFragment(), PictureView {

    override val layoutRes = R.layout.fragment_picture

    @InjectPresenter
    lateinit var presenter: PicturePresenter

    @ProvidePresenter
    fun providePresenter(): PicturePresenter = PicturePresenter(arguments?.getString(IMG_LINK)!!)

    private val REQUEST_CODE = 23235

    override fun showImage(requestBuilder: RequestBuilder<Drawable>) {
        requestBuilder.into(image)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        image.setOnSingleFlingListener { e1, e2, _, _ ->
            presenter.onSwiped(e1.y, e2.y)
            true
        }

        btn_close.setOnClickListener {
            presenter.onBackPressed()
        }

        btn_menu.setOnClickListener {
            PopupMenu(this.context, it).apply {
                menuInflater.inflate(R.menu.picture_menu, menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.btn_save -> {
                            presenter.downloadImage()
                            true
                        }
                        R.id.btn_share -> {
                            shareText(arguments?.getString(IMG_LINK)!!)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                show()
            }
        }
    }

    //TODO: I'm not sure, is that MVP way to do it?
    /////////////////////////////////////////////////////////////////////////////
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.isWritePermissionGranted = true
                showMessage("Доступ разрешен!\nТеперь можно скачивать :)")
            }
        }
    }

    override fun checkWritePermission() {
        presenter.isWritePermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun askForWritePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }
    /////////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showMessage(msg: String) {
        StyleableToast.makeText(context!!, msg, R.style.toast).show()
    }

    override fun lockOrientation() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun unlockOrientation() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    companion object {
        private const val IMG_LINK = "IMG_LINK"
        fun create(imageLink: String) =
            PictureFragment().apply {
                arguments = Bundle().apply {
                    putString(IMG_LINK, imageLink)
                }
            }
    }
}