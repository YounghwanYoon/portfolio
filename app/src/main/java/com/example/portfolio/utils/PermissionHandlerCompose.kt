package com.example.portfolio.utils

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import pub.devrel.easypermissions.AppSettingsDialog


/*@OptIn(ExperimentalPermissionsApi::class)
class RequestMultiplePermissionsSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AccompanistSampleTheme {
                val multiplePermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                    )
                )
                Sample(multiplePermissionsState)
            }
        }
    }
}*/

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandlerCompose(
    multiplePermissionsState: MultiplePermissionsState,
    activity:Activity = LocalContext.current as Activity,
    afterGrant:@Composable ()->Unit,
    afterDenied:@Composable (activity:Activity)-> Unit = { DefaultAfterDenied(activity) },
    afterPermDenied:@Composable (activity:Activity)-> Unit = { DefaultAfterPermDenied(activity) }
) {


    if (multiplePermissionsState.allPermissionsGranted) {
        // If all permissions are granted, then show screen with the feature enabled
        Text("Camera and Read storage permissions Granted! Thank you!")
        runBlocking {
            delay(1000)
        }
        afterGrant()


    } else {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(0.9f)
            ) {
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {

                        val observer = LifecycleEventObserver{ _, event ->
                            if(event == Lifecycle.Event.ON_START){
                                //request permission
                                multiplePermissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                multiplePermissionsState.permissions.forEach { perm ->
                    when (perm.permission) {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            when{
                                perm.status.isGranted ->{
                                    Text("Read External Storage Permission is accepted")
                                }
                                perm.status.shouldShowRationale ->{
                                    Text("Read External Storage Permission is needed" +
                                            "to access your last save state such as cart")
                                    afterDenied(activity)

                                }
                                perm.status.isPermanentlyDenied()->{
                                    Text("Read External Storage Permission is permanently denied" +
                                            "you can enable it in the app settings"
                                    )
                                    afterPermDenied(activity)

                                }
                            }
                        }
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            when{
                                perm.status.isGranted ->{
                                    Text("Write External Storage Permission is accepted")

                                }
                                perm.status.shouldShowRationale ->{
                                    Text("Write External Storage Permission is needed" +
                                            "to save your last state of app such as cart")
                                    afterDenied(activity)

                                }
                                perm.status.isPermanentlyDenied()->{
                                    Text("WRite External Storage Permission is permanently denied" +
                                            "you can enable it in the app settings"
                                    )
                                    afterPermDenied(activity)
                                }
                            }
                        }
                    }

                }
            }

        /*            Text(
                getTextToShowGivenPermissions(
                    multiplePermissionsState.revokedPermissions,
                    multiplePermissionsState.shouldShowRationale
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }

 */
        }
    }
}
/*

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}*/


@Composable
fun DefaultAfterDenied(activity: Activity, action:(activity:Activity)->Unit = { closeApp(activity) }){
    //val activity = (LocalContext.current as? Activity)

    runBlocking{
        delay(1000)
    }
    Button(
        onClick = {
            action(activity)
        },
    ) {
        Text("Exit")
    }
}

@Composable
fun DefaultAfterPermDenied(activity: Activity, action:(activity:Activity)->Unit = {
        AppSettingsDialog.Builder(activity).build().show()//closeApp(activity)
    }
){
    Button(
        onClick = {
            action(activity)
        }
    ) {
        Text("To App Setting")
    }
}

private fun closeApp(activity: Activity){
    //val activity = (LocalContext.current as? Activity)
    runBlocking{
        delay(1000)
    }
    activity.finish()

    /*
    LaunchedEffect(
        key1 = activity,
        block = {

        }
    )*/
}