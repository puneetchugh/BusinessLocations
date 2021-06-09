package com.puneet.vortochallenge.permissions

interface PermissionAskListener {

    /*
     * Callback to ask permission
     * */
    fun onPermissionAsk(permission: String)

    /*
     * Callback on permission denied
     * */
    fun onPermissionPreviouslyDenied()

    /*
     * Callback on permission granted
     * */
    fun onPermissionGranted()

    /*
     * Callback on permission denied
     */
    fun onPermissionDenied()
}