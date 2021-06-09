package com.puneet.vortochallenge

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.puneet.vortochallenge.constants.MASTERLIST_FRAGMENT
import com.puneet.vortochallenge.constants.MASTERLIST_FRAGMENT_STACK
import com.puneet.vortochallenge.fragment.MasterList
import com.puneet.vortochallenge.permissions.PermissionAskListener
import com.puneet.vortochallenge.permissions.PermissionUtils
import kotlinx.android.synthetic.main.blank_activity.*

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blank_activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "Inside onResume() of MainActivity..")
        PermissionUtils.checkPermission(
            this,
            ACCESS_FINE_LOCATION,
            myPermissionAskListener
        )
    }

    private val myPermissionAskListener = object :
        PermissionAskListener {

        override fun onPermissionAsk(permission: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this@MainActivity.requestPermissions(arrayOf(permission), 101)
            }
        }

        override fun onPermissionPreviouslyDenied() {
            Log.d(LOG_TAG, "Inside onPermissionPerviouslyDenied()...")

            Snackbar.make(
                this@MainActivity,
                id_blank_activity,
                resources.getString(R.string.permission_denied),
                Snackbar.LENGTH_LONG
            ).show()
            id_error_image.visibility = View.VISIBLE

            /*TODO : make a clickable link for getting into the App Content for permission
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }

        override fun onPermissionDenied() {
            Log.d(LOG_TAG, "Inside onPermissionDenied()...")
            Snackbar.make(
                this@MainActivity,
                id_blank_activity,
                resources.getString(R.string.permission_denied),
                Snackbar.LENGTH_LONG
            ).show()
            id_error_image.visibility = View.VISIBLE
            id_main_container.visibility = View.INVISIBLE
        }

        override fun onPermissionGranted() {
            //viewModel.businessData.observe(this@MainActivity, businessDataObserver)
            //getLocation()
            Log.d(LOG_TAG, "Inside onPermissionGranted()...")
            id_error_image.visibility = View.INVISIBLE
            id_main_container.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.id_main_container, MasterList(), MASTERLIST_FRAGMENT)
                .addToBackStack(MASTERLIST_FRAGMENT_STACK)
                .commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for ((index, value) in permissions.withIndex()) {
            when (value) {
                ACCESS_FINE_LOCATION -> {
                    if (grantResults[index] == 1) {
                        Log.d(
                            LOG_TAG,
                            "Inside onRequestPermissionResult().. permission granted.."
                        )
                        myPermissionAskListener.onPermissionGranted()
                    } else {
                        Log.i(
                            LOG_TAG,
                            "Inside onRequestPermissionResult().. permission denied.."
                        )

                        myPermissionAskListener.onPermissionDenied()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, "Inside MainActivity()...onStop()..")
    }

    override fun onPause() {
        super.onPause()
        Log.i(LOG_TAG, "Inside MainActivity()...onPause()..")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(LOG_TAG, "Inside MainActivity()...onRestart()...")
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "Inside MainActivity()...onStart()...")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}