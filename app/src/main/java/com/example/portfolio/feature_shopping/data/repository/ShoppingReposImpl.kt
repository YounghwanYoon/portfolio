package com.example.portfolio.feature_shopping.data.repository

import android.util.Log
import com.example.portfolio.feature_shopping.data.local.CartDao
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.ShoppingRepository
import com.example.portfolio.utils.Resource
import com.example.portfolio.utils.ServerSideError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class ShoppingReposImpl @Inject constructor(
    @Named("ShoppingAPI")
    private val pixabayApi:PixabayAPI,
    @Named("PixabayKey")
    private val pixabayKey:String = "",
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val cartDao: CartDao,
):ShoppingRepository {
    private val TAG = "ShoppingReposImpl"

    override fun getSpecial(): Flow<Resource<List<SpecialItem>>> {
        Log.d(TAG, "getSpecial: is called")
        return flow<Resource<List<SpecialItem>>>{
            emit(Resource.Loading())
            val fakeSpecialItem:MutableList<SpecialItem> = mutableListOf(

                SpecialItem(
                    id = 0,
                    image = 0,
                    imageUrl = "",
                    description = "Winter Image",
                    title = "Winter Special",
                    start_date = "11-01-22",
                    end_date = "03-31-23",
                ),
                SpecialItem(
                    id = 1,
                    image = 0,
                    imageUrl = "",
                    description = "Spring Image",
                    title = "Spring Special",
                    start_date = "03-01-23",
                    end_date = "05-31-23",
                ),
                SpecialItem(
                    id = 2,
                    image = 0,
                    imageUrl = "",
                    description = "Summer Image",
                    title = "Summer Special",
                    start_date = "06-01-23",
                    end_date = "08-31-23",
                ),
                SpecialItem(
                    id = 3,
                    image = 0,
                    imageUrl = "",
                    description = "Fall Image",
                    title = "Fall Special",
                    start_date = "09-01-23",
                    end_date = "10-31-23",
                ),


            )

            val pixabayResponse = pixabayApi.getPixaBaySeasonHolidayData(pixabayKey, "seasonal+holiday")
            //check pixabay response code
            pixabayResponse.apply{
                Log.d(TAG, "getSpecial: " +
                        "${this.code()} " +
                        "and URL ${this.raw()} \n"+
                        "and header ${this.headers()} \n" +
                        "and ${this.errorBody()}")
            }


            
            if(pixabayResponse.code() == 500){
                throw ServerSideError("ServerSide Error with code 500")
            }
            if(pixabayResponse.isSuccessful && pixabayResponse.body() != null){
                var index = 0
                while(index < fakeSpecialItem.size){
                    pixabayResponse.body()!!.hits.get(index).apply{
                        fakeSpecialItem[index].id = this.id
                        fakeSpecialItem[index].imageUrl = this.largeImageURL ?: webformatURL
                        fakeSpecialItem[index].id = this.id
                    }
                    index++
                }
            }
            //Make sure Save it to room
            emit(Resource.Success(data = fakeSpecialItem))
        }
        .catch { exception ->
            Log.d(TAG, "getSpecial: Error Occurred from getSpecial() with ${exception.message}")
            emit(Resource.Error("Error from Repo while GET is called " +
                    "+ \n Exception ${exception.message}"))
        }
        .flowOn(dispatcher)
    }

    override fun getSellingItem(): Flow<Resource<List<SellingItem>>> {
        Log.d(TAG, "getSellingItem: is called()")
        
        return flow<Resource<List<SellingItem>>>{
            emit(Resource.Loading())
            val pixabayResponse = pixabayApi.getPixaBayCoffeeData(pixabayKey, "coffee+beans")
            Log.d(TAG, "getSpecial: ${pixabayResponse.code()}")

            if(pixabayResponse.code() == 500)
                throw ServerSideError("Server Side Error with Code 500")
            if(pixabayResponse.isSuccessful && pixabayResponse.body() != null){
                val listHit = pixabayResponse.body()!!.hits
                val listOfSellingItem = listHit.map{
                    SellingItem(
                        id = it.id,
                        image = 0,
                        imageUrl = it.imageURL,
                        title = "Coffee Bean",
                        description = "Coffee Bean Image",
                        price = (Random.nextInt(4, 10) + 0.99),
                        quantity = (Random.nextInt(3,99))
                    )
                }
                emit(Resource.Success(listOfSellingItem))
            }else
                emit(Resource.Error("There is no data from server"))
        }
        .retry(2){
            (it is ServerSideError).also{
                delay(500)
            }
        }
        .catch{ exception ->
            Log.d(TAG, "getSellingItem: Error Occured due to unknown reason: ${exception.message}")
        }
        .flowOn(dispatcher)


    }

    override suspend fun getItemById(id: Int): SellingItem? {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(sellingItem: SellingItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(sellingItem: SellingItem) {
        TODO("Not yet implemented")
    }
}