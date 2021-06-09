package com.puneet.vortochallenge.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.puneet.vortochallenge.R
import com.puneet.vortochallenge.constants.ACURACY_ALPHA
import com.puneet.vortochallenge.constants.ELEVATION
import com.puneet.vortochallenge.constants.MAP_ZOOM
import com.puneet.vortochallenge.data.model.Businesse
import com.puneet.vortochallenge.view_model.MasterViewModel
import com.puneet.vortochallenge.view_model.ViewModelFactory

class ShowLocation : Fragment(), OnMapReadyCallback {

    val LOG_TAG = "ShowLocation"
    lateinit var map: MapboxMap
    lateinit var mapView: MapView
    lateinit var markerViewManager: MarkerViewManager
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MasterViewModel
    lateinit var rootView: View

    companion object {
        fun instance(position: Int): Fragment {
            val showLocation = ShowLocation()
            val bundle = Bundle()
            bundle.putInt("position", position)
            showLocation.arguments = bundle
            return showLocation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory()
        Mapbox.getInstance(
            requireContext(),
            "pk.eyJ1IjoicHVuZWV0Y2h1Z2giLCJhIjoiY2twb3d3NXhuMnBjMzJxbXdjcHN4MGQ4MSJ9.JLpbqucglTCjoXL17C8SFw"
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MasterViewModel::class.java)

        mapView = rootView.findViewById(R.id.id_map)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_show_location, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        Log.d(LOG_TAG, "onMapReady() called...")
        map = mapboxMap

        map.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)
        }
        markerViewManager = MarkerViewManager(mapView, map)
        displayLocation(viewModel.businessData.value?.businesses!!.get(requireArguments().getInt("position")))
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(style: Style) {

        Log.d(LOG_TAG, "enabledLocationComponent() called..")
        if (!::map.isInitialized || map.style == null) {
            Log.e(LOG_TAG, "map isn't initialized yet..returning")
            return
        }

        val customLocationComponentOptions = LocationComponentOptions.builder(this.requireContext())
            .foregroundDrawable(R.drawable.mapbox_mylocation_icon_bearing)
            .foregroundTintColor(Color.CYAN)
            .trackingGesturesManagement(true)
            .elevation(ELEVATION)
            .accuracyAlpha(ACURACY_ALPHA)
            .accuracyColor(Color.BLUE)
            .build()

        val locationComponentActivationOptions =
            LocationComponentActivationOptions.builder(this.requireContext(), style)
                .locationComponentOptions(customLocationComponentOptions)
                .useDefaultLocationEngine(true)
                .build()

        map.locationComponent.apply {
            activateLocationComponent(locationComponentActivationOptions)
            isLocationComponentEnabled = true
            cameraMode = CameraMode.TRACKING
            renderMode = RenderMode.COMPASS
        }

    }

    private fun displayLocation(businesse: Businesse) {
        //fun displayLocation(latLng: LatLng, isUserLoc : Boolean){

        Log.d(LOG_TAG, "displayLocation() called..")

        val imageView = ImageView(activity)
        imageView.setImageResource(R.drawable.mapbox_marker_icon_default)
        imageView.layoutParams = FrameLayout.LayoutParams(128, 128) as ViewGroup.LayoutParams

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(businesse.coordinates.latitude, businesse.coordinates.longitude))
            .zoom(MAP_ZOOM)
            .build()

        TooltipCompat.setTooltipText(imageView, "${businesse.name}\n${businesse.display_phone}\n${businesse.categories[0].title}")
        markerViewManager.addMarker(
            MarkerView(
                LatLng(
                    businesse.coordinates.latitude,
                    businesse.coordinates.longitude
                ), imageView
            )
        )
        map.cameraPosition = cameraPosition
    }

}