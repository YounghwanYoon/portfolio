package com.example.portfolio.feature_shopping.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.mapper.SellingItemMapper

import kotlinx.coroutines.runBlocking
import org.junit.After
//import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ShoppingDataBaseTest{
    lateinit var roomDB:ShoppingDataBase
    lateinit var cartDao:CartDao
    lateinit var itemDao:SellingItemDao
    lateinit var localMapper: SellingItemMapper
    @Before
    fun setUp(){
        localMapper = SellingItemMapper()

        roomDB = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            ShoppingDataBase::class.java
        ).build()
        cartDao = roomDB.cartDao()
        itemDao = roomDB.sellingItemDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetItemTest()= runBlocking{
        println("Testing Started")
        val testingItem =
            SellingItemEntity(
                itemId = 2,
                image = 0,
                imageUrl = "test",
                title = "test",
                description = "test",
                price = 1.99,
                quantity = 99,
                cartId = null
            )
        val resultItems: List<SellingItemEntity>?
        val resultItem: SellingItemEntity?

        itemDao.insertItem( testingItem )
        resultItem = itemDao.getSelectedItem(itemId = 2)

        assertThat(resultItem).isEqualTo(testingItem)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetItemsTest()= runBlocking{
        println("Testing Started")
        val testingItem =
            SellingItemEntity(
                itemId = 2,
                image = 0,
                imageUrl = "test",
                title = "test",
                description = "test",
                price = 1.99,
                quantity = 99,
                cartId = null
            )
        val resultItems: List<SellingItemEntity>?
        //val resultItem: SellingItemEntity?

        itemDao.insertItem( testingItem )
        resultItems = itemDao.getSellingItems()

        assertThat(resultItems.contains(testingItem)).isTrue()
    }

    @After
    @Throws(Exception::class, IOException::class)
    fun closeDB(){
        println("Completed")
        roomDB.close()
    }

}