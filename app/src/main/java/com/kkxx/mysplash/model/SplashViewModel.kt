package com.kkxx.mysplash.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.kkxx.mysplash.SplashApplication
import com.kkxx.mysplash.repository.unsplash.UnSplashWebService

/**
 * @author chenwei
 * 2017/11/2
 */
class SplashViewModel : ViewModel() {

    private var splashLiveData: LiveData<List<SplashInfo>>? = null
    private var webService: UnSplashWebService? = null

    fun initData() {
        if (webService == null) {
            webService = UnSplashWebService.getService()
        }
        if (this.splashLiveData != null) {
            return
        }
        this.splashLiveData = webService!!.requestPhotoes(0, 15, SplashApplication.ORDER_BY_LATEST)
    }

    fun getSplashList(): LiveData<List<SplashInfo>>? {
        if (splashLiveData == null) {
            initData()
        }
        return splashLiveData!!
    }
}