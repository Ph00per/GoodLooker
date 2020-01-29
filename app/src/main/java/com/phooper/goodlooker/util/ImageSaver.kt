package com.phooper.goodlooker.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.phooper.goodlooker.App
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageSaver {

    @Inject
    lateinit var context: Context

    init {
        App.daggerComponent.inject(this)
    }

    fun save(img: Bitmap, name: String?) {
        name?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveQ(img, it)
            } else {
                saveLegacy(img, it)
            }
        }
    }

    private fun saveLegacy(img: Bitmap, name: String) {
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            , "GoodLooker"
        )
        if (!storageDir.exists()) storageDir.mkdirs()

        val file = File(storageDir, name)

        FileOutputStream(
            file
        ).apply {
            img.compress(Bitmap.CompressFormat.JPEG, 100, this)
            this.close()
        }
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            null,
            null
        )
    }

    private fun saveQ(img: Bitmap, name: String) {

        val contentResolver = context.contentResolver

        contentResolver.insert(
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GoodLooker")
                put(MediaStore.Video.Media.MIME_TYPE, "image")
            })?.let { uri ->
            contentResolver.openOutputStream(uri).let {
                img.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
    }

}
