package com.example.demoapp.repository.webservices.myapp

import com.example.demoapp.model.MyApp
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.demoapp.utils.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor(): EntityMapper<MyApp, MyAppLocal> {

    override fun mapFrom(data: MyApp): MyAppLocal {
        return MyAppLocal(
            id = data.id,
            title = data.title,
            description = data.description,
            picture = data.picture,
            picture_color = data.picture_color,
            referenceURL = data.referenceURL
        )
    }

    override fun mapFromListOf(listData:List<MyApp>):List<MyAppLocal>{
        return listData.map{ mapFrom(it)}
    }
}



class LocalMapper @Inject constructor(): EntityMapper<MyAppLocal, MyApp> {

    override fun mapFrom(data: MyAppLocal): MyApp {
        return MyApp(
            id = data.id,
            title = data.title,
            description = data.description,
            picture = data.picture,
            picture_color = data.picture_color,
            referenceURL = data.referenceURL
        )
    }

    override fun mapFromListOf(listData: List<MyAppLocal>): List<MyApp> {
        return listData.map{mapFrom(it)}
    }

}
