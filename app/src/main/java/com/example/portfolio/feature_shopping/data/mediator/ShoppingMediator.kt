package com.example.portfolio.feature_shopping.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.local.entity.RemoteKeysEntity
import com.example.portfolio.feature_shopping.data.local.ShoppingDataBase
import com.example.portfolio.feature_shopping.data.local.entities.PaginationKeysEntity
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.mapper.SellingItemMapperLocal
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.presentation.utils.Helper
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random


@OptIn(ExperimentalPagingApi::class)
class ShoppingMediator @Inject constructor(
    @Named("PixabayKey")
    private val apiKey:String,
    private val shoppingDB:ShoppingDataBase,
    @Named("ShoppingAPI")
    private val pixabayAPI: PixabayAPI,
    private val mapper: SellingItemMapperLocal
):RemoteMediator<Int,SellingItemEntity > () {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH

        /*     val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
             return if(System.currentTimeMillis() - (shoppingDB.paginationKeyDao().getCreationTime() ?: 0) < cacheTimeout){
                 println("Initial Refresh")
                 InitializeAction.LAUNCH_INITIAL_REFRESH
             }else{
                 println("SKIP Refresh")
                 InitializeAction.SKIP_INITIAL_REFRESH
             }*/
    }

    //Prepend
    private suspend fun getRemoteKeyForFirstItem(state:PagingState<Int,SellingItemEntity>):PaginationKeysEntity?{
        return state.pages.firstOrNull{
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let{item ->
            shoppingDB.paginationKeyDao().getKeysByItemID(item.itemId)
        }
    }

    //Append
    /*
        Entities will be organized by primary key, therefore, it is better to create and use separate id as pimary key
        if data is fetched/provided in random order( by the server or depends on the data type like top 10 can be change ever time).
     */
    private suspend fun getRemoteKeyForLastItem(state:PagingState<Int,SellingItemEntity>):PaginationKeysEntity?{

        return shoppingDB.paginationKeyDao().getLastKeys() ?: null

    /*  return state.pages.lastOrNull(){
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let{item ->
            println("last item id:"+item.itemId)
            shoppingDB.paginationKeyDao().getKeysByItemID(item.itemId)
        }*/
    }

    //Refresh/current
    private suspend fun getRemoteKeyClosestToCurrentPosition(state:PagingState<Int,SellingItemEntity>): PaginationKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.itemId?.let { id ->
                shoppingDB.paginationKeyDao().getKeysByItemID(id)
            }
        }
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SellingItemEntity>
    ): MediatorResult {
        val nextKey:Int = when(loadType){
            LoadType.REFRESH -> {
                println("Refresh")

                //Things trigger:
                //1. Refresh will be called and block prepend and Append if initialize() is called for first time.
                //2. User call refresh

                //Steps to take
                //1. Check whether there is stored PagingKeys in local DB or not.
                //2. If there is none, it should return 1
                val pagingKeys = getRemoteKeyClosestToCurrentPosition(state)
                pagingKeys?.nextPage?.minus(1) ?:1
            }

            LoadType.PREPEND -> {
                println("Prepend")

                //will not use this
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            LoadType.APPEND -> {
                println("Append")

                //This will be called whenever user reach the bottom of page or given set trigger item
                //If pagingKeys is null, then it means refresh call has not yet finished storing api data into db

                val pagingKeys = getRemoteKeyForLastItem(state)
                val nextPage: Int? = pagingKeys?.nextPage

                nextPage?:return MediatorResult.Success(endOfPaginationReached = pagingKeys != null)
            }
        }
        println("next page - $nextKey")
        //TODO: Limiting api data to 100 items for this demo/portfolio case due to the limit.
        if(nextKey > 5) return MediatorResult.Success(endOfPaginationReached = true)
        try{
            //To Do
            //1. call api/service to fetch and save api data
            //2. update and save pagination keys(page)

            val apiResponse = pixabayAPI.getCoffeeDataByPagination(
                key = apiKey,
                search = "coffee bean",
                imageType = "photo",
                page = nextKey
            )

            //fetch data from server
            val sellingItems = apiResponse.body()?.items

            val reachedEndPage = apiResponse.body()?.items?.isEmpty()

            //withTransaction will execute all or nothing.
            //This helps to prevent alter partial changes
            shoppingDB.withTransaction {
                if(loadType == LoadType.REFRESH){
                    shoppingDB.paginationKeyDao().clearRemoteKeys()
                    shoppingDB.sellingItemDao().clearAll()
                }
                //prepare next key sets
                val prevPage:Int? = if(nextKey >1) nextKey-1 else null
                val nextPage:Int? = if(reachedEndPage == true) null else nextKey + 1

                if(sellingItems != null){
                    //map fetch api response (DTO) to local data(Entity)
                    val remoteKeys = sellingItems.map{
                        PaginationKeysEntity(
                            sellingItemId = it.id,
                            prePage = prevPage,
                            curPage = nextKey,
                            nextPage = nextPage
                        )
                    }
                    //insert to local room db
                    shoppingDB.paginationKeyDao().insertAll(remoteKeys)
                    shoppingDB.sellingItemDao().insertItems(
                        sellingItems.map{
                            SellingItemEntity(
                                itemId = it.id,
                                cartId = null,
                                imageUrl = it.webformatURL,
                                title = it.user,
                                description = it.tags,
                                price = Helper.formatHelper(Random.nextDouble(4.99,8.99)),
                                quantity = Random.nextInt(10,100),
                                page = nextKey,
                            )
                        }
                    )
                }
            }
            return MediatorResult.Success(
                endOfPaginationReached = reachedEndPage ?: false
            )

        }catch(e:IOException){
            return MediatorResult.Error(e)
        }catch(e:HttpException){
            return MediatorResult.Error(e)
        }
    }
}