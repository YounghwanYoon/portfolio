package com.example.portfolio.feature_secondactivity.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolio.R

class SecondActivity:AppCompatActivity() {

    companion object {
        private const val TAG = "SecondActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(TAG, "onCreate: ")
        setContentView(R.layout.activity_second)
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        Log.d(SecondActivity.TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(SecondActivity.TAG, "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d(SecondActivity.TAG, "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d(SecondActivity.TAG, "onStop: ")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(SecondActivity.TAG, "onRestart: ")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(SecondActivity.TAG, "onDestroy: ")
        super.onDestroy()
    }

}