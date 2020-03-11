/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _status

    //live data and getter
    private val _property = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty> get() = _property

    /**init a co-routine job and scope */
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        /**encapsulating coroutines inside a launch block so that it automatically manages concurrency */
        coroutineScope.launch {
            /**calls the mars api singleton and makes a get request and await the results with co-routines*/
            var getItemsDeferred = MarsApi.retrofitService.getProperties()

            try {
                var resultsList = getItemsDeferred.await()
                if(resultsList.isNotEmpty()){
                    _property.value = resultsList[67] //TODO: debug not getting url error
                }

            }catch (t:Throwable){
                _status.value = "Error ${t.localizedMessage}"
            }
        }
        _status.value = "Awaiting..."
    }

    /**cancel job if view model is destroyed */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
