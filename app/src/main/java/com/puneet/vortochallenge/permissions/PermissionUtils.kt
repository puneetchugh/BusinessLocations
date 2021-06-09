package com.puneet.vortochallenge.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class PermissionUtils {
    /*
    * Check if version is marshmallow and above.
    * Used in deciding to ask runtime permission
    * */
    companion object {
        private fun needToAskPermission(): Boolean {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        }

        private fun needToAskPermission(context: Context, permission: String): Boolean {
            if (needToAskPermission()) {
                val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
                if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                    return true
                }
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkPermission(
            context: Context,
            permission: String,
            listener: PermissionAskListener
        ) {
            /*
        * If permission is not granted
        * */
            if (needToAskPermission(context, permission)) {
                /*
            * If permission denied previously
            * */
                if ((context as Activity).shouldShowRequestPermissionRationale(permission)) {
                    listener.onPermissionPreviouslyDenied()
                } else {
                    listener.onPermissionAsk(permission)
                }
            } else {
                listener.onPermissionGranted()
            }
        }
    }
}