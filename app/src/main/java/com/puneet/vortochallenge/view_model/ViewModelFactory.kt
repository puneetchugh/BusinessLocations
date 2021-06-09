package com.puneet.vortochallenge.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MasterViewModel::class.java)) {
            return MasterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}