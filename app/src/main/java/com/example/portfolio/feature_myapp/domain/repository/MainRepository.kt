package com.example.demoapp.repository

import android.util.Log
import com.example.demoapp.model.MyApp
import com.example.demoapp.repository.webservices.fakedata.FakeServer
import com.example.portfolio.feature_myapp.domain.repository.local.myapp.MyAppDao
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.demoapp.repository.webservices.myapp.LocalMapper
import com.example.demoapp.repository.webservices.myapp.NetworkMapper
import com.example.portfolio.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    /*val apiServices:ApiServices,*/
    val db_dao: MyAppDao,
    val networkMapper: NetworkMapper,
    val localMapper: LocalMapper,
    ) {
    /*
    @Inject
    lateinit var fakeData:List<MyApp>

     */

    var fakeData:List<MyApp> = FakeServer.provideData()

    private var internetAvail = true

    /*
    companion object {
        val BASE_URL = "https://api.github.com or somewhere cloud"

    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getApi():ApiServices{
        val retrofit = buildRetrofit()
        val api:ApiServices = retrofit.create(ApiServices::class.java)
        return api
    }

     */


    suspend fun getMyApp(): Flow<DataState<List<MyAppLocal>>> {
        Log.d(TAG, "getMyApp: underMyApp")
        return flow<DataState<List<MyAppLocal>>>{
            emit(DataState.Loading<List<MyAppLocal>>(null))
            try{
                //get data from a server
                val listOfmyApp = getDataFromServer()

                Log.d(TAG, "getMyApp: from server ${listOfmyApp.toString()}")

                //map them to local data
                val listOfmyAppLocal = networkMapper.mapFromListOf(listOfmyApp)

                //insert them into database
                cacheDataToRoom(listOfmyAppLocal)
                //request local data from room
                val cachedData = getDataFromRoom()

                Log.d(TAG, "getMyApp: from local ${cachedData}")
                //emit data where it collects
                emit(DataState.Success(cachedData))
            }catch(e:Exception){
                //when there is error,
                emit(DataState.Error(e.message!!, null))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDataFromServer():List<MyApp>{
        //return apiServices.getMyApps()
        return fakeData
    }





    suspend fun cacheDataToRoom(listLocalData:List<MyAppLocal>){
        for(myAppLocal in listLocalData)
            db_dao.insert(myAppLocal)
    }
    suspend fun getDataFromRoom():List<MyAppLocal> = db_dao.getAll()

    companion object{
        private val TAG:String = this.javaClass.name
    }
}