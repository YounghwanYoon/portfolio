package com.example.portfolio.feature_pagination.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.portfolio.feature_pagination.data.local.MovieEntity
import com.example.portfolio.feature_pagination.data.local.MovieRoomDB
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


@OptIn(ExperimentalPagingApi::class)
class MovieMediator @Inject constructor(
    @Named("TBDM_API_KEY")
    private val key:String,
    private val movieService: MovieService,
    private val movieRoomDB: MovieRoomDB,
): RemoteMediator<Int, MovieEntity>() {

    /**
     * @param loadType- loadType is to determine when pagination should call. There are three options
     * 1. Refresh * 2. Prepend * 3. Append
     * I am here to use Append as user scroll down to bottom of page and load more pages of data from server
     *
     * @param state - PagingState is key value pair where key is page number and value is return item type
     *
     * loadKey == next page number
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try{
            //loadkey == next page
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
                    if(lastItem == null) {
                        1
                    } else{
                        //this will determine whether item have called all the items in the page data
                        //if so, then call next page
                        //example: 40/20 +1 => 2 +1 => 3 next page / key is 3
                        //example2: 25/20 +1 => 1 + 5/20(or 1/4) +1 => still 2
                        lastItem.id/state.config.pageSize + 1
                    }
                }


            }

            val movies = movieService.getTopRateMovies(
                key = key,
                language = "en-US",
                page = loadKey
            ).results

            //with withTransaction, either all or none will be successfully proceed.
            //this way, it will be not change anything if one of them fails.
            movieRoomDB.withTransaction {
                if(loadType == LoadType.REFRESH){
                    movieRoomDB.movieDao.clearAll()
                }
                val movieEntities:List<MovieEntity> = movies.map{
                    it.toMovieEntity()
                }
                movieRoomDB.movieDao.upsertAll(movieEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = if(movies== null) true else false
            )

        } catch(e:IOException){
            MediatorResult.Error(e)
        } catch(e:HttpException){
            MediatorResult.Error(e)
        }

    }

}
