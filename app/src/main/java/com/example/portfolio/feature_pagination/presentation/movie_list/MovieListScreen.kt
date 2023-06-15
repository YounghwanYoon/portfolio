package com.example.portfolio.feature_pagination.presentation.movie_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.presentation.ui.theme.PortfolioTheme
import com.google.android.material.progressindicator.CircularProgressIndicator


@Composable
fun MovieListScreen(
    movies: LazyPagingItems<Movie>
) {
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
                items(count = movies.itemCount){index ->
                    val item = movies[index]
                    MovieItem(item!!)
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
                model = movie.poster_path,
                contentDescription = movie.title,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(3f)
            ){
                Text(
                   text= movie.title,
                   style = MaterialTheme.typography.headlineMedium,
                   modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Popularity: ${movie.popularity}",
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
                original_language = "mucius",
                original_title = "contentiones",
                overview = "omnesque",
                popularity = 4.5,
                poster_path = "dolorum",
                release_date = "rhoncus",
                title = "faucibus",
                video = false,
                vote_average = 6.7,
                vote_count = 9799
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}