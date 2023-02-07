package com.example.portfolio.utils

import com.google.accompanist.permissions.*


@ExperimentalPermissionsApi
fun PermissionStatus.isPermanentlyDenied():Boolean{
    //This means user declined permission for second times.
    return !shouldShowRationale && !isGranted
}