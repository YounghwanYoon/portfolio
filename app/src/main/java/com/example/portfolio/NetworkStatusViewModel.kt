package com.example.portfolio

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.portfolio.utils.NetworkMonitor
import com.example.portfolio.utils.NetworkStateTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class NetworkStatusViewModel @Inject constructor(
    val networkMonitor:NetworkMonitor,
    @Named("NetworkDispatcher")
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {



}