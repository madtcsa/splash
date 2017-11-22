package com.kkxx.mysplash.service.unsplash

import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

/**
 * Splash SplashPhoto Api
 * @author chenwei
 * 2017/11/5
 */
interface SplashPhotoApi {

    // data.
    val ORDER_BY_LATEST: String
        get() = "latest"
    val ORDER_BY_OLDEST: String
        get() = "oldest"
    val ORDER_BY_POPULAR: String
        get() = "popular"


    @GET("photos")
    fun getPhotos(@Query("page") page: Int,
                  @Query("per_page") per_page: Int,
                  @Query("order_by") order_by: String): Call<List<SplashPhoto>>

    @GET("photos/random")
    fun getRandomPhotos(@Query("tag") categoryId: Int?,
                        @Query("featured") featured: Boolean?,
                        @Query("username") username: String,
                        @Query("query") query: String,
                        @Query("orientation") orientation: String,
                        @Query("count") count: Int): Call<List<SplashPhoto>>


    @GET("categories/{id}/photos")
    abstract fun getPhotosInAGivenCategory(@Path("id") id: Int,
                                           @Query("page") page: Int,
                                           @Query("per_page") per_page: Int): Call<List<SplashPhoto>>
}