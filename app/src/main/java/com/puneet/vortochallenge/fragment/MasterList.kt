package com.puneet.vortochallenge.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.puneet.vortochallenge.R
import com.puneet.vortochallenge.adapter.BusinessListAdapter
import com.puneet.vortochallenge.constants.LOCATION_UPDATE_FREQ
import com.puneet.vortochallenge.constants.SHOWLOCATION_FRAGMENT
import com.puneet.vortochallenge.constants.SHOWLOCATION_FRAGMENT_STACK
import com.puneet.vortochallenge.data.model.BusinessData
import com.puneet.vortochallenge.data.model.Businesse
import com.puneet.vortochallenge.view_model.MasterViewModel
import com.puneet.vortochallenge.view_model.ViewModelFactory

class MasterList : Fragment(), LocationListener {
    private val LOG_TAG = "MasterList"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView

    private lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MasterViewModel
    lateinit var rootView: View
    lateinit var businessListAdapter: BusinessListAdapter

    private val businessDataObserver = Observer<BusinessData> { businessData ->
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        //TODO : Add DiffUtils for efficient comparison of old and new data.
        (recyclerView.adapter as BusinessListAdapter).addData(businessData.businesses)
        (recyclerView.adapter as BusinessListAdapter).notifyDataSetChanged()

        businessData.businesses.forEach {
            Log.d(LOG_TAG, "BusinessName ${it.name}")
        }
    }

    private val onQueryListener = object : SearchView.OnQueryTextListener {
        @SuppressLint("MissingPermission")
        override fun onQueryTextSubmit(query: String?): Boolean {
            query.takeIf { !TextUtils.isEmpty(query) }.let {
                val location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location.let {
                    val latitude = location?.latitude
                    val longitude = location?.longitude

                    viewModel.fetchLatestData(
                        latitude.toString(),
                        longitude.toString(),
                        query.toString()
                    )
                }
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            //TODO("Not yet implemented")
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory()
        Log.d(LOG_TAG, "Inside MasterList()..onCreate() called..")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(LOG_TAG, "Inside MasterList()..onCreateView() called..")

        locationManager =
            this.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        rootView = inflater.inflate(R.layout.activity_main, container, false)
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("PuneetChugh", "Inside MasterList()..onDestroyView() called..")

        if (locationManager != null) {
            locationManager.removeUpdates(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(LOG_TAG, "Inside MasterList()..onActivityCreated() called..")

        getLocation()
        viewModel = ViewModelProvider(
            this.requireActivity(),
            viewModelFactory
        ).get(MasterViewModel::class.java)
        recyclerView = rootView.findViewById(R.id.id_recyclerview)
        searchView = rootView.findViewById(R.id.id_searchview)
        searchView.setOnQueryTextListener(onQueryListener)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = BusinessListAdapter(requireContext(), listOf()).apply {
            itemClick = {
                Log.d(LOG_TAG, "Inside MasterList()...itemClick....$it")
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.id_main_container,
                        ShowLocation.instance(it),
                        SHOWLOCATION_FRAGMENT
                    )
                    .addToBackStack(SHOWLOCATION_FRAGMENT_STACK).commit()
            }
        }
        viewModel.businessData.observe(requireActivity(), businessDataObserver)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_UPDATE_FREQ.toLong(),
            0f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        //
    }
}