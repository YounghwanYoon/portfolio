import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import com.example.portfolio.feature_pagination.data.repo_impl.util.MoviesPagingSource
import com.example.portfolio.feature_pagination.data.repo_impl.util.NETWORK_PAGE_SIZE
import com.example.portfolio.feature_pagination.domain.repository.MoviesRemoteDataSource


//This is like repository which handle only remote data source whereas repository also handle local room database
internal class MoviesRemoteDataSourceImpl(
    private val key:String,
    private val movieService: MovieService
) : MoviesRemoteDataSource {

    override fun getMovies(): kotlinx.coroutines.flow.Flow<PagingData<MovieResponse>> {
        return Pager(
            config = PagingConfig (
                pageSize  = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                MoviesPagingSource (key, movieService = movieService)
            }
        ).flow
    }

}


