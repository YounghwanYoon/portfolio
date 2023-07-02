package com.example.demoapp.repository.webservices.myapp

import com.example.demoapp.model.MyApp
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.portfolio.feature_shopping.data.local.util.NetworkMapper
import com.example.portfolio.feature_shopping.domain.model.util.EntityMapper
import javax.inject.Inject

class NetworkMapperImpl @Inject constructor():
    NetworkMapper<MyApp, MyAppLocal> {

    override fun mapFromDTO(data: MyApp): MyAppLocal {
        return MyAppLocal(
            id = data.id,
            title = data.title,
            description = data.description,
            picture = data.picture,
            picture_color = data.picture_color,
            referenceURL = data.referenceURL
        )
    }

    override fun mapFromListDTO(listData:List<MyApp>):List<MyAppLocal>{
        return listData.map{ mapFromDTO(it) }
    }

}



class LocalMapper @Inject constructor():
    EntityMapper<MyAppLocal, MyApp> {

    override fun mapFromEntity(data: MyAppLocal): MyApp {
        return MyApp(
            id = data.id,
            title = data.title,
            description = data.description,
            picture = data.picture,
            picture_color = data.picture_color,
            referenceURL = data.referenceURL
        )
    }

    override fun mapFromEntities(listData: List<MyAppLocal>): List<MyApp> {
        return listData.map{mapFromEntity(it)}
    }

    override fun mapToEntity(model: MyApp): MyAppLocal {
        TODO("Not yet implemented")
    }

    override fun mapToEntities(models: List<MyApp>): List<MyAppLocal> {
        TODO("Not yet implemented")
    }

}
