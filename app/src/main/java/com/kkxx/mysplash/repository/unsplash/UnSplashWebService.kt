package com.kkxx.mysplash.repository.unsplash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.kkxx.mysplash.SplashApplication
import com.kkxx.mysplash.model.SplashInfo
import com.kkxx.mysplash.service.unsplash.SplashAuthInterceptor
import com.kkxx.mysplash.service.unsplash.SplashPhotoApi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author chenwei
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

        return Retrofit.Builder().baseUrl(SplashApplication.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat
                (SplashApplication.DATE_FORMAT).create()))
                .build()
                .create(SplashPhotoApi::class.java)
    }

    fun requestPhotoes(page: Int, perPage: Int, orderBy: String): LiveData<List<SplashInfo>> {
        val getPhotos = buildApi(buildClient()).getPhotos(page, perPage, orderBy)
        val data: MutableLiveData<List<SplashInfo>> = MutableLiveData()
        getPhotos.enqueue(object : Callback<List<SplashInfo>> {
            override fun onResponse(call: Call<List<SplashInfo>>, response: retrofit2
            .Response<List<SplashInfo>>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<SplashInfo>>, t: Throwable) {

            }
        })
        return data
    }
}