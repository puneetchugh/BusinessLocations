package com.puneet.vortochallenge.data.api

import com.puneet.vortochallenge.data.model.BusinessData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v3/businesses/search")
    fun getSearchData(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("term") term: String,
        @Query("radius") radius: Int
    ): Observable<BusinessData>

    @GET("/v3/businesses/search")
    fun getSearchData(
        @Query("location") location: String,
        @Query("term") term: String,
        @Query("radius") radius: Int
    ): Observable<BusinessData>
}