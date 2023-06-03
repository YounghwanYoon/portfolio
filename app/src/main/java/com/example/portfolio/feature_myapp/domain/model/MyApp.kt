package com.example.demoapp.model

data class MyApp(
    var id: Int,
    var title: String,
    var description: String,
    var picture:String,
    var picture_color:Int?,
    var referenceURL: String
)

data class MyGit(
    var id:Int,
    var url:String,
    var repos_url:String
)
