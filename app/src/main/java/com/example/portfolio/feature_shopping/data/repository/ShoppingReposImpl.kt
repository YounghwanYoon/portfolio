package com.example.portfolio.feature_shopping.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.portfolio.feature_shopping.data.local.ShoppingDataBase
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.data.mapper.SellingItemMapper
import com.example.portfolio.feature_shopping.data.mapper.SpecialItemMapper
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.data.remote.dto.PixabayDTO
import com.example.portfolio.feature_shopping.data.remote.dto.SellingItemDTO
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.feature_shopping.domain.repository.webservices.utils.ShoppingDataType
import com.example.portfolio.utils.Resource
import com.example.portfolio.utils.ServerSideError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random



/**
 * RepositoryImpl is where fetch from api and store to room database.
 *
 * @property pixabayApi PixabayAPI
 * @property pixabayKey String
 * @property dispatcher CoroutineDispatcher
 * @property shoppingDataBase ShoppingDataBase
 * @property mapperSellingItem SellingItemMapperLocal
 * @property mapperSpecialItem SpecialItemMapper
 * @property pager Pager<Int, SellingItemEntity>
 *
 * @constructor
 */
class ShoppingReposImpl @Inject constructor(
    @Named("ShoppingAPI") private val pixabayApi: PixabayAPI,
    @Named("PixabayKey") private val pixabayKey: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val shoppingDataBase: ShoppingDataBase,
    private val mapperSellingItem: SellingItemMapper,
    private val mapperSpecialItem: SpecialItemMapper,
    //for pagination
    private val pager: Pager<Int, SellingItemEntity>

) : ShoppingRepository {
    private val TAG = "ShoppingReposImpl"


    override fun getPagingDate(): Flow<PagingData<SellingItem>> {
        Timber.tag(TAG).d("getPagingDate: is called")
        return pager
            .flow
            .map { pagingData ->
                pagingData.map {
                    //it.toSellingItem()
                    mapperSellingItem.mapFromEntity(it)
                }
            }
    }

    override suspend fun getItemById(id: Int): Flow<SellingItem> {
        return flow<SellingItem>{
            val entity = shoppingDataBase.sellingItemDao().getSelectedItem(id)
            val sellingItem = entity.toSellingItem()
            sellingItem?.let{
                emit(it)
            }
        }.flowOn(dispatcher)
    }

    /**
     * Handles fetch Special Item data from API then cache to room data base,
     * then emit the cached data to caller.
     * It is following single source of truth principle.
     *
     * @return Flow<Resource<List<SpecialItem>>>
     */
    override fun fetchAndLoadSpecialItems(): Flow<Resource<List<SpecialItem>>> {
        Timber.tag(TAG).d("getSpecial: is called")
        return flow<Resource<List<SpecialItem>>> {

            // * retrieve and emit cached data with Resource.Loading State.
            // * If none exists, emit empty state

            val entityData =
                getDataFromRoom(ShoppingDataType.SpecialItems()) as List<SpecialItemEntity>

            if (entityData.isNotEmpty()) {
                Timber.tag(TAG).d("size of cached data %s", entityData.size)
                val listLocalData = mapperSpecialItem.mapFromEntities(entityData)
                emit(Resource.Loading(listLocalData))
            } else emit(Resource.Loading())

            // * Request data from server to update data and cache into room database
            //Getting only Image data from server (pixabay)
            val pixabayResponse = getDataFromAPI(ShoppingDataType.SpecialItems()) as PixabayDTO
            val seasonalItemEntities: MutableList<SpecialItemEntity> = mutableListOf(

                SpecialItemEntity(
                    id = 0,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Winter Image",
                    title = "Winter Special",
                    start_date = "11-01-22",
                    end_date = "03-31-23",
                ),
                SpecialItemEntity(
                    id = 1,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Spring Image",
                    title = "Spring Special",
                    start_date = "03-01-23",
                    end_date = "05-31-23",
                ),
                SpecialItemEntity(
                    id = 2,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Summer Image",
                    title = "Summer Special",
                    start_date = "06-01-23",
                    end_date = "08-31-23",
                ),
                SpecialItemEntity(
                    id = 3,
                    image = 0,
                    imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
                    description = "Fall Image",
                    title = "Fall Special",
                    start_date = "09-01-23",
                    end_date = "10-31-23",
                ),
            )
            //When retrieving data from server was successful
            seasonalItemEntities.mapIndexed { index, current ->
                pixabayResponse.sellingItemsDTO[index].apply {
                    current.id = this.id
                    current.imageUrl = this.largeImageURL
                }
            }
            insertItemToDataBase(ShoppingDataType.SpecialItems(seasonalItemEntities))

            // * Retrieve and emit cached data from room data base
            val seasonalItem = getDataFromRoom(ShoppingDataType.SpecialItems()) as List<SpecialItem>
            emit(Resource.Success(data = seasonalItem))

        }.catch { exception ->
            Timber.tag(TAG).d("getSpecial: Error Occurred from getSpecial() with %s", exception.message)
            emit( Resource.Error("Error from Repo while GET is called " + "+ \n Exception ${exception.message}"))
        }.flowOn(dispatcher)
    }

    /**
     * Handles fetch Selling Item data from API then cache to room data base,
     * then emit the cached data to caller.
     * It is following single source of truth principle.
     *
     * @return Flow<Resource<List<SpecialItem>>>
     */
    override fun fetchAndLoadSellingItems(): Flow<Resource<List<SellingItem>>> {
        Log.d(TAG, "getSellingItem: is called()")

        return flow<Resource<List<SellingItem>>> {

            //get data from localDB and if not null, temporary load it.
            val localData: List<SellingItem> = shoppingDataBase.sellingItemDao().getSellingItems().let {
                mapperSellingItem.mapFromEntities(it)
            }
/*

            //Due to limited request, get and use data from local database if available.
            when{
                localData.get(0).imageUrl != "" -> {
                    Log.d(TAG, "getSellingItem: localData for selling SellingItem are not null")
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
            if (pixabayResponse.sellingItemsDTO.isNotEmpty()) {
                val sellingItemsDTOs = pixabayResponse.sellingItemsDTO
                val listOfSellingItem = sellingItemsDTOs.map {
                    SellingItem(
                        id = it.id,
                        image = 0,
                        imageUrl = it.largeImageURL,
                        title = "Coffee Bean By ${it.user}" ?: "Coffee Bean",
                        description = "Coffee Bean Image",
                        price = (Random.nextInt(4, 10) + 0.99),
                        supplyQty = (Random.nextInt(3, 99))
                    )
                }
                Timber.tag(TAG).d("getSellingItem: size of newData = %s", listOfSellingItem.size)

                //when retrieving data from server was successful
                //save them to RoomDB
                insertItemToDataBase(ShoppingDataType.SellingItems(listOfSellingItem))
                emit(Resource.Success(listOfSellingItem))
            } else emit(Resource.Error("There is no data from server"))


        }.retry(2) {
            (it is ServerSideError).also {
                delay(500)
            }
        }.catch { exception ->
            Timber.tag(TAG)
                .d("getSellingItem: Error Occured due to unknown reason: %s", exception.message)
        }.flowOn(dispatcher)
    }


    override suspend fun getDataFromAPI(data: ShoppingDataType): Any? {
        when (data) {
            is ShoppingDataType.SpecialItems -> {
                Timber.tag(TAG).d("getDataFromAPI:Special Items")
                val response =
                    pixabayApi.getPixaBaySeasonHolidayData(key = pixabayKey, "holiday+coffee")
                if (response.code() == 500) throw ServerSideError("Code 500, error from server side and need to try it again")
                //check pixabay response code
                response.apply {
                    Timber.tag(TAG)
                        .d(
                            "getDataFromAPI: size of special %s",
                            this.body()?.sellingItemsDTO?.size
                        )
                }
                return response.body()
            }
            is ShoppingDataType.SellingItems -> {

                Timber.tag(TAG).d("getDataFromAPI:Selling Items")
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
                Timber.tag(TAG).d("getDataFromRoom - SellingItem() is called")
                val dataEntity = shoppingDataBase.sellingItemDao().getSellingItems()
                Timber.tag(TAG).d("size of data cached in database - Special: %s", dataEntity.size)
                return mapperSellingItem.mapFromEntities(dataEntity) //Resource.Success(model)
            }
            is ShoppingDataType.SpecialItems -> {
                Timber.tag(TAG).d("getDataFromRoom - Special() is called")
                val dataEntity = shoppingDataBase.specialItemDao().getSpecialItems()
                Timber.tag(TAG).d("size of data cached in database - Special: %s", dataEntity.size)
                return mapperSpecialItem.mapFromEntities(dataEntity) //Resource.Success(model)
            }
            else -> {
                Timber.tag(TAG).d("getDataFromRoom: this should not be called.")
                return emptyList()
            }
        }
    }

    /**
     * Handling insertingItem from API to Room Database.
     * Item will be selected by ShoppingDataType
     * @param dataType ShoppingDataType
     */
    override suspend fun insertItemToDataBase(dataType: ShoppingDataType) {
        when (dataType) {
            is ShoppingDataType.SellingItem -> {
                shoppingDataBase.sellingItemDao().insertItem(mapperSellingItem.mapToEntity(dataType.data as SellingItem))
            }
            is ShoppingDataType.SellingItems -> {
                shoppingDataBase.sellingItemDao().insertItems(mapperSellingItem.mapToEntities(dataType.listData as List<SellingItem>))
            }
            is ShoppingDataType.SpecialItem -> {
                Timber.tag(TAG).d("insertItemToDataBase: Special Item")
                shoppingDataBase.specialItemDao().insertItem(dataType.data as SpecialItemEntity)

                //shoppingDataBase.specialItemDao().insertItem(mapperSpecialItem.mapFromDTO(dataType.data as SellingItemDTO))

            }
            is ShoppingDataType.SpecialItems -> {
                Timber.tag(TAG).d("insertItemToDataBase: Special Items")

                shoppingDataBase.specialItemDao().insertItems(dataType.listData as List<SpecialItemEntity>)
/*
                shoppingDataBase.specialItemDao().insertItems(mapperSpecialItem.mapFromListDTO(dataType.listData as List<SellingItemDTO>))
*/
            }
        }
    }
    override suspend fun deleteItemFromDataBase(dataType: ShoppingDataType) {
        when (dataType) {
            is ShoppingDataType.SellingItem -> {
                shoppingDataBase.sellingItemDao().deleteSellingItem(mapperSellingItem.mapToEntity(dataType.data as SellingItem))
            }
            is ShoppingDataType.SpecialItem -> {
                shoppingDataBase.specialItemDao().deleteSpecialItem(mapperSpecialItem.mapToEntity(dataType.data as SpecialItem))
            }

            else -> {}
        }
    }
}