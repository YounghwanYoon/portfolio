package com.example.portfolio.feature_pagination3.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.portfolio.feature_pagination3.domain.model.Beer


@Composable
fun BeerItem(
    beer: Beer,
    modifier: Modifier = Modifier
) {

    Card(
        modifier= modifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            AsyncImage(
                model = beer.image_url,
                contentDescription = beer.description,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier= Modifier
                    .weight(3f)
            ){
                Text(
                    text = beer.name,
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = beer.tagline,
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = beer.description,
                )
            }
        }
    }

}

@Preview
@Composable
fun BeerItemPreview() {
    MaterialTheme{
        BeerItem(
            beer = Beer(
                id = 5701,
                name = "Ken Weber",
                tagline = "referrentur",
                description = "offenditl;fafdja;sfjas;ljflk;asmf;lma what the hell is going in here" +
                        "woohoo~lalalla",
                first_brewed = "sonet",
                image_url = null
            )
        )
    }
}