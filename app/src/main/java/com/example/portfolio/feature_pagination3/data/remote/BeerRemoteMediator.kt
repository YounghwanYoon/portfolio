package com.example.portfolio.feature_pagination3.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.portfolio.feature_pagination3.data.local.BeerDataBase
import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity
import retrofit2.HttpException
import androidx.room.withTransaction
import com.example.portfolio.feature_pagination3.data.mapper.toBeerEntity
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator @Inject constructor(
    private val beerDB:BeerDataBase,
    private val beerApi: BeerApi
):RemoteMediator<Int, BeerEntity>(){
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {

        return try{
           val loadKey = when(loadType){
                LoadType.REFRESH -> {
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null){
                        1
                    }else{
                        val nextPageKey = (lastItem.id / state.config.pageSize) + 1
                        nextPageKey
                    }
                }
            }
            delay(1500)
            val beers = beerApi.getBeers(
                page = loadKey,
                pageCount = state.config.pageSize,
            )

            //withTransaction, this execution will all succeed or failures
            //this way it won't or will alter the data.
            beerDB.withTransaction {
                if(loadType == LoadType.REFRESH){
                    //when Refresh, we need to clear all caches and return first page of data
                    beerDB.dao.clearAll()
                }

                val beerEntity = beers.map{
                    it.toBeerEntity()
                }
                beerDB.dao.upsertAll(beerEntity)

                MediatorResult.Success(
                    endOfPaginationReached =beers.isEmpty()
                )
            }
        }catch(e: IOException){
            return MediatorResult.Error(e)
        }catch(e: HttpException){
            return MediatorResult.Error(e)
        }



    }
}