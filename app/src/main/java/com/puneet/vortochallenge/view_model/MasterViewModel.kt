package com.puneet.vortochallenge.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.puneet.vortochallenge.data.model.BusinessData
import com.puneet.vortochallenge.data.network.NetworkModule
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MasterViewModel() : ViewModel() {

    var businessData: MutableLiveData<BusinessData> = MutableLiveData()

    var errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    lateinit var disposable: Disposable

    var observer = object : Observer<BusinessData> {
        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(value: BusinessData) {
            businessData.value = value
        }

        override fun onError(e: Throwable) {
            errorLiveData.value = e
        }

        override fun onComplete() {
            //TODO("Not yet implemented")
        }
    }

    fun fetchLatestData(latitude: String, longitude: String, term: String) {
        NetworkModule.providesNetworkService().getSearchData(latitude, longitude, term, 20000)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(observer)
    }

    override fun onCleared() {
        super.onCleared()
        this::disposable.isInitialized.takeIf { it }.let {
            disposable.dispose()
        }
    }

}