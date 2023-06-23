package com.example.portfolio.feature_pagination.presentation.movie_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.presentation.movie_list.viewmodel.MovieListViewModel
import com.example.portfolio.feature_pagination.presentation.ui.theme.PortfolioTheme
import com.google.android.material.progressindicator.CircularProgressIndicator

/*
@Composable
fun MovieListScreen(
    //movies: LazyPagingItems<Movie>
) {

    val movieVM: MovieListViewModel = hiltViewModel()
    val movies = movieVM.moviePagingFlow.collectAsLazyPagingItems()

    val context = LocalContext.current
    //whenever loadState changes, we check whether load state is actually
    LaunchedEffect(key1 =movies.loadState){
        if(movies.loadState.refresh is LoadState.Error){
            Toast.makeText(
                context,
                "Error: ${(movies.loadState.refresh as LoadState.Error).error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        if(movies.loadState.refresh is LoadState.Loading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(movies){item ->
                    item?.let{
                        MovieItem(it)
                    }
                }
                item{
                    if(movies.loadState.append is LoadState.Loading){
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}*/

@Composable
fun MovieListScreen() {
    val movieVM: MovieListViewModel = hiltViewModel()
    //val movies = movieVM.getPager().collectAsLazyPagingItems()



    val movies = movieVM.moviePagingFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(
            items = movies
        ) { movie ->
            movie?.let {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (movie.poster_path != null) {
                        var isImageLoading by remember { mutableStateOf(false) }

                        val painter = rememberAsyncImagePainter(
                            model = "https://image.tmdb.org/t/p/w154" + movie.poster_path,
                        )

                        isImageLoading = when(painter.state) {
                            is AsyncImagePainter.State.Loading -> true
                            else -> false
                        }

                        Box (
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                    .height(115.dp)
                                    .width(77.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                painter = painter,
                                contentDescription = "Poster Image",
                                contentScale = ContentScale.FillBounds,
                            )

                            if (isImageLoading) {
                                androidx.compose.material.CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp, vertical = 3.dp),
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 8.dp),
                        text = it.original_title
                    )
                }
                Divider()
            }
        }

        val loadState = movies.loadState.mediator
        item {
            if (loadState?.refresh == LoadState.Loading) {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    androidx.compose.material.Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Refresh Loading"
                    )

                    androidx.compose.material.CircularProgressIndicator(color = androidx.compose.material.MaterialTheme.colors.primary)
                }
            }

            if (loadState?.append == LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.material.CircularProgressIndicator(color = androidx.compose.material.MaterialTheme.colors.primary)
                }
            }

            if (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) {
                val isPaginatingError = (loadState.append is LoadState.Error) || movies.itemCount > 1
                val error = if (loadState.append is LoadState.Error)
                    (loadState.append as LoadState.Error).error
                else
                    (loadState.refresh as LoadState.Error).error

                val modifier = if (isPaginatingError) {
                    Modifier.padding(8.dp)
                } else {
                    Modifier.fillParentMaxSize()
                }
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (!isPaginatingError) {
                        Icon(
                            modifier = Modifier
                                .size(64.dp),
                            imageVector = Icons.Rounded.Warning, contentDescription = null
                        )
                    }

                    androidx.compose.material.Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = error.message ?: error.toString(),
                        textAlign = TextAlign.Center,
                    )

                    Button(
                        onClick = {
                            movies.refresh()
                        },
                        content = {
                            androidx.compose.material.Text(text = "Refresh")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = androidx.compose.material.MaterialTheme.colors.primary,
                            contentColor = Color.White,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie:Movie,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(4.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ){
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w154" + movie.poster_path,
                contentDescription = movie.original_title,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(3f)
            ){
                Text(
                   text= movie.original_title,
                   style = MaterialTheme.typography.headlineMedium,
                   modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                   text= "ID: ${movie.id}",
                   style = MaterialTheme.typography.headlineMedium,
                   modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Popularity: ${movie.vote_average}",
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    
}

@Preview
@Composable
fun MovieItemPreview() {
    PortfolioTheme() {
        MovieItem(
            Movie(
                id = 4404,
                original_title = "contentiones",
                overview = "omnesque",
                poster_path = "dolorum",
                release_date = "rhoncus",
                vote_average = 6.7,
                vote_count = 9799
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}