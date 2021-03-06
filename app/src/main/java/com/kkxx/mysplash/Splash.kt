package com.kkxx.mysplash

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray

/**
 * @author zsmj
 * @date 2017/11/5
 */
class Splash : Application() {


    private var instance: Splash = this@Splash


    companion object {
        //cache image for view detail.
        val detailImg = SparseArray<Drawable>()
        /**
         * Unsplash API
         */
        val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
        val STREAM_API_BASE_URL = "https://api.getstream.io/"
        val UNSPLASH_URL = "https://unsplash.com/"

        val DATE_FORMAT = "yyyy/MM/dd"
        val DOWNLOAD_PATH = "/Pictures/splash/"
        val DOWNLOAD_PHOTO_FORMAT = ".jpg"
        val DOWNLOAD_COLLECTION_FORMAT = ".zip"

        /**
         * Unsplash PhotoCategory
         */
        val CATEGORY_TOTAL_NEW = 0
        val CATEGORY_TOTAL_FEATURED = 1
        val CATEGORY_BUILDINGS_ID = 2
        val CATEGORY_FOOD_DRINK_ID = 3
        val CATEGORY_NATURE_ID = 4
        val CATEGORY_OBJECTS_ID = 8
        val CATEGORY_PEOPLE_ID = 6
        val CATEGORY_TECHNOLOGY_ID = 7

        //Per Page request count
        var perPageCount = 10

        var illogicalParams = -1

        /**
         * Unsplash photos order
         */
        val ORDER_BY_LATEST = "latest"
        val ORDER_BY_OLDEST = "oldest"
        val ORDER_BY_POPULAR = "popular"

        val unsplash_application_id = "42ca73ce1b06000aefbc36aa95c204a8b75685d97249878139c0e3114bb2b702"
        val unsplash_secret = "a1c3d30d21227b0dd416303566feb770a98912ac0ecb4d9c353c27f64048f6a3"

        fun getSplashAppId(auth: Boolean): String {
            return unsplash_application_id
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getInstance(): Splash {
        return instance
    }
}