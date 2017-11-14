package com.kkxx.mysplash.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.kkxx.mysplash.SplashApplication
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.kkxx.mysplash.repository.unsplash.UnSplashWebService

/**
 * @author chenwei
 * 2017/11/2
 */
class SplashViewModel : ViewModel() {

    private var splashLiveData: LiveData<List<SplashPhoto>>? = null
    private var webService: UnSplashWebService? = null

    fun loadData(pageIndex: Int) {
        if (webService == null) {
            webService = UnSplashWebService.getService()
        }
        this.splashLiveData = webService!!.requestPhotoes(pageIndex, 3, SplashApplication
                .ORDER_BY_LATEST)
    }

    fun getSplashList(pageIndex: Int): LiveData<List<SplashPhoto>>? {
        loadData(pageIndex)
        return splashLiveData!!
    }
}