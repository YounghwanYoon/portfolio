package com.example.portfolio.feature_shopping.data.repository

import android.util.Log
import com.example.portfolio.feature_shopping.data.local.CartDao
import com.example.portfolio.feature_shopping.data.local.SellingItemDao
import com.example.portfolio.feature_shopping.data.local.SpecialItemDao
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.data.mapper.SellingItemMapperLocal
import com.example.portfolio.feature_shopping.data.mapper.SpecialItemMapperLocal
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.data.remote.dto.PixabayDTO
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.Resource
import com.example.portfolio.utils.ServerSideError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

class ShoppingReposImpl @Inject constructor(
    @Named("ShoppingAPI") private val pixabayApi: PixabayAPI,
    @Named("PixabayKey") private val pixabayKey: String = "",
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val cartDao: CartDao,
    private val itemDao: SellingItemDao,
    private val specialDao: SpecialItemDao,
    private val localMapperSellingItem: SellingItemMapperLocal,
    private val localMapperSpecialItem: SpecialItemMapperLocal
) : ShoppingRepository {
    private val TAG = "ShoppingReposImpl"

    override fun fetchAndLoadSpecialItems(): Flow<Resource<List<SpecialItem>>> {
        Log.d(TAG, "getSpecial: is called")
        return flow<Resource<List<SpecialItem>>> {
            val entityData = getDataFromRoom(ShoppingDataType.SpecialItems()) as List<SpecialItemEntity>
            if(entityData.size > 3){
                val listLocalData = localMapperSpecialItem.mapToListOf(entityData)
                emit(Resource.Loading(listLocalData))
            }else emit(Resource.Loading())

            val fakeSpecialItem: MutableList<SpecialItem> = mutableListOf(

                SpecialItem(
                    id = 0,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Winter Image",
                    title = "Winter Special",
                    start_date = "11-01-22",
                    end_date = "03-31-23",
                ),
                SpecialItem(
                    id = 1,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Spring Image",
                    title = "Spring Special",
                    start_date = "03-01-23",
                    end_date = "05-31-23",
                ),
                SpecialItem(
                    id = 2,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Summer Image",
                    title = "Summer Special",
                    start_date = "06-01-23",
                    end_date = "08-31-23",
                ),
                SpecialItem(
                    id = 3,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Fall Image",
                    title = "Fall Special",
                    start_date = "09-01-23",
                    end_date = "10-31-23",
                ),
            )

            //Getting only Image data from server (pixabay)
            val pixabayResponse = getDataFromAPI(ShoppingDataType.SpecialItems()) as PixabayDTO
                //pixabayApi.getPixaBaySeasonHolidayData(pixabayKey, search = "holiday+coffee")

            //When retrieving data from server was successful
            fakeSpecialItem.mapIndexed { index, current ->
                pixabayResponse.hits[index].apply {
                    current.id = this.id
                    current.imageUrl = this.largeImageURL
                }
            }

            //insertItem(ShoppingDataType.SpecialItems(fakeSpecialItem))

            //Make sure Save it to room
            emit(Resource.Success(data = fakeSpecialItem))

        }.catch { exception ->
            Log.d(TAG, "getSpecial: Error Occurred from getSpecial() with ${exception.message}")
            emit(
                Resource.Error(
                    "Error from Repo while GET is called " + "+ \n Exception ${exception.message}"
                )
            )
        }.flowOn(dispatcher)
    }

    private fun mapRepoToLocalSelling(sellingItems: List<SellingItem>): List<SellingItemEntity> {
        return localMapperSellingItem.mapFromListOf(sellingItems)
    }


    sealed class ShoppingDataType(data: Any? = null, listData: List<Any>? = null) {
        class SpecialItem(val data: Any) : ShoppingDataType(data)
        class SpecialItems(val listData: List<Any>? = null) : ShoppingDataType(listData)
        class SellingItem(val data: Any) : ShoppingDataType(data)
        class SellingItems(val listData: List<Any>? = null) : ShoppingDataType(listData)
    }

    override fun fetchAndLoadSellingItems(): Flow<Resource<List<SellingItem>>> {
        Log.d(TAG, "getSellingItem: is called()")

        return flow<Resource<List<SellingItem>>> {

            //get data from localDB and if not null, temporary load it.
            val localData: List<SellingItem> = itemDao.getSellingItems().let {
                localMapperSellingItem.mapToListOf(it)
            }
/*

            //Due to limited request, get and use data from local database if available.
            when{
                localData.get(0).imageUrl != "" -> {
                    Log.d(TAG, "getSellingItem: localData for selling items are not null")
                    emit(Resource.Success(data = localData))
                }
                else ->{
                    Log.d(TAG, "getSellingItem: localData is empty and requesting new data from server")

                    //get data from local database for loading.
                    emit(Resource.Loading<List<SellingItem>>())

                    val pixabayResponse = pixabayApi.getPixaBayCoffeeData(pixabayKey, search = "coffee+beans")
                    Log.d(TAG, "getSellingItem: response code - ${pixabayResponse.code()}")

                    if (pixabayResponse.code() == 500)
                        throw ServerSideError("Server Side Error with Code 500")

                    if (pixabayResponse.isSuccessful && pixabayResponse.body() != null) {
                        val listHit = pixabayResponse.body()!!.hits
                        val listOfSellingItem = listHit.map {
                            SellingItem(
                                id = it.id,
                                image = 0,
                                imageUrl = it.imageURL,
                                title = "Coffee Bean",
                                description = "Coffee Bean Image",
                                price = (Random.nextInt(4, 10) + 0.99),
                                quantity = (Random.nextInt(3, 99))
                            )
                        }
                        Log.d(TAG, "getSellingItem: size of newData = ${listOfSellingItem.size}")
                        //when retrieving data from server was successful
                        //save them to RoomDB
                        saveToRoomDB(listOfSellingItem)

                        emit(
                            Resource.Success(
                                listOfSellingItem
                            )
                        )
                    } else
                        emit(Resource.Error("There is no data from server"))
                }
            }
*/


            //get data from local database for loading.
            emit(
                Resource.Loading<kotlin.collections.List<SellingItem>>(
                    getDataFromRoom(ShoppingDataType.SellingItems()) as List<SellingItem>
                )
            )

            val pixabayResponse = getDataFromAPI(ShoppingDataType.SellingItems()) as PixabayDTO
            /* val pixabayResponse =   pixabayApi.getPixaBayCoffeeData(pixabayKey, search = "coffee+beans")
             android.util.Log.d(TAG, "getSellingItem: response code - ${pixabayResponse.code()}")

             if (pixabayResponse.code() == 500)
                 throw com.example.portfolio.utils.ServerSideError("Server Side Error with Code 500")
 */

            if (pixabayResponse.hits.isNotEmpty()) {
                val listHit = pixabayResponse.hits
                val listOfSellingItem = listHit.map {
                    Log.d(TAG, "getSellingItem: title: ${it.tags}, ${it.comments}")
                    SellingItem(
                        id = it.id,
                        image = 0,
                        imageUrl = it.largeImageURL,
                        title = "Coffee Bean By ${it.user}" ?: "Coffee Bean",
                        description = "Coffee Bean Image",
                        price = (kotlin.random.Random.nextInt(4, 10) + 0.99),
                        quantity = (kotlin.random.Random.nextInt(3, 99))
                    )
                }
                Log.d(
                    TAG, "getSellingItem: size of newData = ${listOfSellingItem.size}"
                )

                //when retrieving data from server was successful
                //save them to RoomDB
                insertItem(ShoppingDataType.SellingItems(listOfSellingItem))
                emit(Resource.Success(listOfSellingItem))
            } else emit(Resource.Error("There is no data from server"))


        }.retry(2) {
            (it is ServerSideError).also {
                delay(500)
            }
        }.catch { exception ->
            Log.d(
                TAG, "getSellingItem: Error Occured due to unknown reason: ${exception.message}"
            )
        }.flowOn(dispatcher)
    }

    override suspend fun getDataFromAPI(data: ShoppingDataType): Any? {
        when (data) {
            is ShoppingDataType.SpecialItems -> {
                val response = pixabayApi.getPixaBaySeasonHolidayData(key = pixabayKey, "holiday+coffee")
                if (response.code() == 500) throw ServerSideError("Code 500, error from server side and need to try it again")
                //check pixabay response code
                response.apply {
                    Log.d(TAG, "getDataFromAPI: size of special ${this.body()?.hits?.size}")
                    /*Log.d(
                        TAG,
                        "getSpecial: " + "${this.code()} " + "and URL ${this.raw()} \n" + "and header ${this.headers()} \n" + "and ${this.errorBody()}"
                    )*/
                }
                return response.body()
            }
            is ShoppingDataType.SellingItems -> {
                val response = pixabayApi.getPixaBayCoffeeData(key = pixabayKey, "coffee+beans")
                if (response.code() == 500) throw ServerSideError("Code 500, error from server side and need to try it again")
                return response.body()
            }
            else -> {
                return null
            }
        }

    }


    override suspend fun  getDataFromRoom(dataType: ShoppingDataType): List<Any> {
        when (dataType) {
            is ShoppingDataType.SellingItems -> {
                return localMapperSellingItem
                    .mapToListOf(itemDao.getSellingItems()) //Resource.Success(model)
            }
            is ShoppingDataType.SpecialItems -> {
                val dataEntity = specialDao.getSpecialItems()
                return localMapperSpecialItem.mapToListOf(dataEntity) //Resource.Success(model)
            }
            else -> {
                Log.d(TAG, "getDataFromRoom: this should not be called.")
                return emptyList()
                //return Resource.Error("getting data from Room encounter an error")
                //not supposed to be called
            }
        }
    }

    override suspend fun getItemById(
        id: Int, dataType: ShoppingReposImpl.ShoppingDataType
    ): SellingItem? {
        return localMapperSellingItem.mapTo(itemDao.getSelectedItem(id))
    }

    override suspend fun insertItem(data: ShoppingReposImpl.ShoppingDataType) {
        when (data) {
            is ShoppingDataType.SellingItem -> {
                itemDao.insertItem(localMapperSellingItem.mapFrom(data.data as SellingItem))
            }
            is ShoppingDataType.SellingItems -> {
                itemDao.insertItems(localMapperSellingItem.mapFromListOf(data.listData as List<SellingItem>))
            }
            is ShoppingDataType.SpecialItem -> {
                specialDao.insertItem(localMapperSpecialItem.mapFrom(data.data as SpecialItem))
            }
            is ShoppingDataType.SpecialItems -> {
                specialDao.insertItems(localMapperSpecialItem.mapFromListOf(data.listData as List<SpecialItem>))
            }
        }
    }

    override suspend fun deleteItem(data: ShoppingReposImpl.ShoppingDataType) {
        when (data) {
            is ShoppingDataType.SellingItem -> {
                itemDao.deleteSellingItem(localMapperSellingItem.mapFrom(data.data as SellingItem))
            }
            is ShoppingDataType.SpecialItem -> {
                specialDao.deleteSpecialItem(localMapperSpecialItem.mapFrom(data.data as SpecialItem))
            }

            else -> {}
        }
    }
}