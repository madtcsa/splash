package com.kkxx.mysplash.adapter

import android.view.View
import android.widget.ImageView
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto

/**
 * @author zsmj
 * @date 2017/11/26
 */
interface ClickViewDetail {

    fun clickToDetail(position: Int,target:ImageView,photo:SplashPhoto)
}