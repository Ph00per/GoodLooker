package com.phooper.goodlooker.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.phooper.goodlooker.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideRequestOptions(): RequestOptions =
        RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)

    @Provides
    @Singleton
    fun provideGlideInstance(
        context: Context, requestOptions: RequestOptions
    ): RequestManager =
        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
}
