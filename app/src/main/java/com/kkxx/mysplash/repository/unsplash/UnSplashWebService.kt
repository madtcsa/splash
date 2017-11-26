package com.kkxx.mysplash.repository.unsplash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.kkxx.mysplash.Splash
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.kkxx.mysplash.service.unsplash.SplashAuthInterceptor
import com.kkxx.mysplash.service.unsplash.SplashPhotoApi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author zsmj
 * 2017/11/4
 */
class UnSplashWebService {

    companion object {
        fun getService(): UnSplashWebService {
            return UnSplashWebService()
        }
    }

    private fun buildClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(SplashAuthInterceptor()).build()
    }

    private fun buildApi(client: OkHttpClient): SplashPhotoApi {

        return Retrofit.Builder().baseUrl(Splash.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat
                (Splash.DATE_FORMAT).create()))
                .build()
                .create(SplashPhotoApi::class.java)
    }

    fun requestPhotoes(page: Int, perPage: Int, orderBy: String): LiveData<List<SplashPhoto>> {
        val getPhotos = buildApi(buildClient()).getPhotos(page, perPage, orderBy)
        val data: MutableLiveData<List<SplashPhoto>> = MutableLiveData()
        getPhotos.enqueue(object : Callback<List<SplashPhoto>> {
            override fun onResponse(call: Call<List<SplashPhoto>>, response: retrofit2.Response<List<SplashPhoto>>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<SplashPhoto>>, t: Throwable) {

            }
        })
        return data
    }

    fun requestPhotosInAGivenCategory(id: Int,
                                      page: Int, per_page: Int): LiveData<List<SplashPhoto>> {
        val getPhotosInAGivenCategory = buildApi(buildClient()).getPhotosInAGivenCategory(id, page, per_page)
        val data: MutableLiveData<List<SplashPhoto>> = MutableLiveData()
        getPhotosInAGivenCategory.enqueue(object : Callback<List<SplashPhoto>> {
            override fun onResponse(call: Call<List<SplashPhoto>>, response: retrofit2.Response<List<SplashPhoto>>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<SplashPhoto>>, t: Throwable) {

            }
        })
        return data
    }
}