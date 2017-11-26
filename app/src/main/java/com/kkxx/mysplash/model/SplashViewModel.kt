package com.kkxx.mysplash.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.kkxx.mysplash.Splash
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.kkxx.mysplash.repository.unsplash.UnSplashWebService

/**
 * @author zsmj
 * 2017/11/2
 */
class SplashViewModel : ViewModel() {

    private var splashLiveData: LiveData<List<SplashPhoto>>? = null

    private var webService: UnSplashWebService? = null

    fun loadData(pageIndex: Int) {
        if (webService == null) {
            webService = UnSplashWebService.getService()
        }
        this.splashLiveData = webService!!.requestPhotoes(pageIndex, Splash.perPageCount, Splash
                .ORDER_BY_LATEST)
    }

    fun getSplashList(pageIndex: Int): LiveData<List<SplashPhoto>>? {
        return getSplashList(pageIndex, Splash.illogicalParams)
    }

    fun getSplashList(pageIndex: Int, categoryId: Int): LiveData<List<SplashPhoto>>? {
        if (null == webService) {
            webService = UnSplashWebService.getService()
        }
        if (Splash.illogicalParams != categoryId) {
            this.splashLiveData = webService!!.requestPhotosInAGivenCategory(categoryId, pageIndex, Splash
                    .perPageCount)
        } else {
            this.splashLiveData = webService!!.requestPhotoes(pageIndex, Splash.perPageCount,
                    Splash.ORDER_BY_LATEST)
        }
        return splashLiveData!!
    }
}