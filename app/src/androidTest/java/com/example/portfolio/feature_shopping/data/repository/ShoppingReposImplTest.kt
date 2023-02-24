package com.example.portfolio.feature_shopping.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.portfolio.feature_shopping.data.local.ShoppingDataBase
import com.example.portfolio.feature_shopping.data.mapper.SellingItemMapperLocal
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.utils.Resource
import com.google.common.truth.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ShoppingReposImplTest constructor(){
    lateinit var repo:ShoppingReposImpl
    lateinit var roomDB:ShoppingDataBase
    lateinit var localMapper: SellingItemMapperLocal

    @Before
    fun initShoppingRepo(){
        localMapper = SellingItemMapperLocal()

        roomDB = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            ShoppingDataBase::class.java
        ).build()

        val api = Retrofit.Builder()
            .baseUrl("http://pixabay.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayAPI::class.java)

/*        repo = ShoppingReposImpl(
            pixabayApi = api,
            pixabayKey = "15227824-a2215005f70965bf20cc7de51",
            dispatcher = Dispatchers.Main,
            cartDao = roomDB.cartDao(),
            itemDao = roomDB.sellingItemDao(),
           // localMapper = localMapper//SellingItemMapperLocal()
        )*/

    }


    //@Test
    @Throws(Exception::class)
    fun getDataFromAPI() = runBlocking{
        repo.getSellingItem().collect{
            when(it){
                is Resource.Error -> println("Error - ${it.message}")
                is Resource.Loading -> println("Loading")
                is Resource.Success -> {
                    val size = it.data?.size
                    val isMore:Boolean = size!! > 0

                    println("Size is ${size}")
                    it.data?.forEach {
                        println(it)
                    }

                    assertThat(isMore).isTrue()

                }
            }
        }
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetItem(){
        println("Testing Started")
        val testingItem =
            SellingItem(
                id = 2,
                image = 0,
                imageUrl = "test",
                title = "test",
                description = "test",
                price = 1.99,
                quantity = 99
            )
        val resultItem:SellingItem?
        runBlocking{
            //repo.insertItem( testingItem )
            //repo.getItemById(2)
            //resultItem = repo.getItemById(2)
        }

        //assertThat(resultItem).isEqualTo(testingItem)


    }



    @After
    @Throws(Exception::class, IOException::class)
    fun closeDB(){
        roomDB.close()
    }





}