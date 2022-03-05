package com.example.demoapp.repository.webservices.fakedata

import com.example.demoapp.model.MyApp
import com.example.portfolio.R
import javax.inject.Inject

class FakeServer @Inject constructor(){
    companion object{
        fun provideData(): List<MyApp> {
            return mutableListOf(
                MyApp(
                    0,
                    "SRT Media Player",
                    "A video player which play a video file along with asian language subtitle",
                    "picture url",
                    R.color.red,
                    "reference to google store url"
                ),
                MyApp(
                    1,
                    "SRT Subtitle Converter",
                    "This converter covert a file to SRT formatted subtitle",
                    "picture url",
                    R.color.blue,
                    "reference to google store url"
                ),
                MyApp(
                    2,
                    "Learn Korean",
                    "This app help you to learn most commonly use words in Korea",
                    "picture url",
                    R.color.yellow,
                    "reference to google store url"
                ),

                )

        }
    }
}