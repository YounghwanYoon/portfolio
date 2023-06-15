package com.example.portfolio.feature_pagination.data.repo_impl.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.portfolio.feature_pagination.data.local.MovieEntity
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import retrofit2.HttpException
import java.io.IOException


//PagingSource<Type_Of_Paging_key, Type_of_Data_to_load>
/*
    PagingSource is used for load data from any single source, including network sources and local database.
    RemoteMediator is used to handle paging from a layered data source, such as a network data source with a local database cache
 */
@OptIn(ExperimentalPagingApi::class)
class MoviesPagingSource(
    private val key:String,
    private val movieService: MovieService
): PagingSource<Int, MovieResponse>(){
    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val pageIndex = params.key ?:TMDB_STARTING_PAGE_INDEX

        return try{
            val response = movieService.getTopRateMovies(
                key = key,
                language = "en-US",
                page = pageIndex
            )
            val movies: List<MovieResponse> = response.results

            val nextKey =
                if(movies.isEmpty()){
                    null
                }else{
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize/NETWORK_PAGE_SIZE)
                }

            LoadResult.Page(
                data = movies,
                prevKey = if(pageIndex == TMDB_STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )

        } catch(exception: IOException){
            return LoadResult.Error(exception)
        } catch(exception:HttpException){
            return LoadResult.Error(exception)
        }
    }
}