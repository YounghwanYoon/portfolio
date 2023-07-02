package com.example.portfolio.feature_pagination.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.portfolio.feature_pagination.data.local.MovieRoomDB
import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.local.entity.RemoteKeysEntity
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import com.example.portfolio.feature_pagination.domain.model.Movie

import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * RemoteMediator acts as a signal from the Paging library when the app has run out of cached data.
 * You can use this signal to load additional data from the network and store it in the local database,
 * where a PagingSource can load it and provide it to the UI to display.
 */
@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val moviesApiService: MovieService,
    private val moviesDatabase: MovieRoomDB,
): RemoteMediator<Int, MovieEntity>() {
    /**
     * When additional data is needed, the Paging library calls the load() method from the RemoteMediator implementation.
     * This function typically fetches the new data from a network source and saves it to local storage.
     * Over time the data stored in the database requires invalidation, such as when the user manually triggers a refresh.
     * This is represented by the LoadType property passed to the load() method.
     * The LoadType informs the RemoteMediator whether it needs to refresh the existing data or fetch additional data that needs to be appended or prepended to the existing list.
     */

    /**
     * In cases where the local data needs to be fully refreshed, initialize() should return InitializeAction.LAUNCH_INITIAL_REFRESH.
     * This causes the RemoteMediator to perform a remote refresh to fully reload the data.
     *
     * In cases where the local data doesn't need to be refreshed, initialize() should return InitializeAction.SKIP_INITIAL_REFRESH.
     * This causes the RemoteMediator to skip the remote refresh and load the cached data.
     */
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        //calculate whether data is old or not.
        // If it is older than cacheTimeout, then refresh else skip
        return if (System.currentTimeMillis() - (moviesDatabase.remoteKeysDao.getCreationTime() ?: 0) < cacheTimeout) {
            println("Skip refresh")
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            println("intial_refresh")
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    /** LoadType.Append
     * When we need to load data at the end of the currently loaded data set, the load parameter is LoadType.APPEND
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained SellingItem.
        // From that last page, get the last item
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            moviesDatabase.remoteKeysDao.getRemoteKeyByMovieID(movie.id)
        }
    }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load parameter is LoadType.PREPEND
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained SellingItem.
        // From that first page, get the first item
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            moviesDatabase.remoteKeysDao.getRemoteKeyByMovieID(movie.id)
        }
    }

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh() is called;
     * so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null.
     * When PagingDataAdapter.refresh() is called, the anchorPosition is the first visible position in the displayed list, so we will need to load the page that contains that specific item.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                moviesDatabase.remoteKeysDao.getRemoteKeyByMovieID(id)
            }
        }
    }

    /**.
     *
     * @param state This gives us information about the pages that were loaded before,
     * the most recently accessed index in the list, and the PagingConfig we defined when initializing the paging stream.
     * @param loadType this tells us whether we need to load data at the end (LoadType.APPEND)
     * or at the beginning of the data (LoadType.PREPEND) that we previously loaded,
     * or if this the first time we're loading data (LoadType.REFRESH).
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        try {
            var page: Int = when (loadType) {
                LoadType.REFRESH -> {
                    println("MovieMediator: load() - Refresh")

                    //New Query so clear the DB
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    println("MovieMediator: load() - PREPEND")

                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                //Append will not be called when remoteKeys.next is null
                //So it is good idea to use initialization() first
                LoadType.APPEND -> {
                    println("MovieMediator: load() - APPEND")

                    val remoteKeys = getRemoteKeyForLastItem(state)
                    println("remote next key is ${remoteKeys?.nextKey}")

                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with endOfPaginationReached = false because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                    //TODO Because next key returns null and remoteKey is not null, MediatorResult return success and it
                    //stop calling load()
                    //It must have value 1 as starting
                }
            }
            println("page: $page")

            if(page == null) {
                println("forcing to input initial page")
                page = 1
            }

            val apiResponse = moviesApiService.getTopRateMoviesTBDB(page = page)

            delay(1000L) //TODO For testing only!

            val movies = apiResponse.movies

            val endOfPaginationReached = movies.isEmpty()
            println("endOfPaginationReached: $endOfPaginationReached")
            moviesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDatabase.remoteKeysDao.clearRemoteKeys()
                    moviesDatabase.movieDao.clearAll()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = movies.map {

                    RemoteKeysEntity(movieID = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                println("prevKey = $prevKey")
                println("nextKey = $nextKey")

                moviesDatabase.remoteKeysDao.insertAll(remoteKeys)
                moviesDatabase.movieDao.upsertAll(
                    movies.map{it.toMovieEntity()}
                        .onEachIndexed { _, movie -> movie.page = page }
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }
}