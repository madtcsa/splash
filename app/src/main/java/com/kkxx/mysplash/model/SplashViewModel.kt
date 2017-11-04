package com.kkxx.mysplash.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * @author chenwei
 * 2017/11/2
 */
class SplashViewModel : ViewModel() {

    private lateinit var splashLiveData: LiveData<List<SplashInfo>>

    fun getSplashs(): LiveData<List<SplashInfo>> {
        return splashLiveData
    }
}