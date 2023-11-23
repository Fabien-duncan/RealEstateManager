package com.openclassrooms.realestatemanager.presentation.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.AddressModel

@Composable
fun AddressDetail(
    modifier: Modifier = Modifier,
    address: AddressModel,
    isLargeView:Boolean,
    mapImageLink: String
){
    val padding = if(isLargeView) 8.dp else 8.dp
    Row(modifier = Modifier
        .padding(top = 16.dp, start = padding, end = 8.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(modifier = Modifier.weight(0.7f)) {
            Row {
                Image(painter = painterResource(id = R.drawable.address_image), contentDescription = "address")
                Text(text = "Location")
            }
            Column(Modifier.padding(start = 24.dp)) {
                Text(text = "${address.number} ${address.street}", fontSize = 16.sp, color = Color.DarkGray)
                if (address.extra != null ) Text(text = address.extra, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.city, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.state, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.postalCode, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.country, fontSize = 16.sp, color = Color.DarkGray)
            }
        }

        if (mapImageLink.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp).weight(if (isLargeView) 2.5f else 1f), horizontalAlignment = Alignment.End) {
                AsyncImage(
                    model = Uri.parse(mapImageLink),
                    contentDescription = "map view",
                    contentScale = ContentScale.FillWidth,
                    modifier = if (isLargeView) Modifier
                        .heightIn(max = 250.dp)
                        .fillMaxWidth()
                        .padding(horizontal = padding, vertical = 8.dp)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
                    else Modifier
                        .widthIn(180.dp)
                        .heightIn(180.dp)
                        .padding(horizontal = padding, vertical = 8.dp)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
                )
            }
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.extraSmall)
                    .weight(1f)
            ){
                Image(
                    painter = painterResource(id = R.drawable.missing_image),
                    contentDescription = "No Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(if (isLargeView) 180.dp else 100.dp)
                        .padding(4.dp)
                )
                Text(
                    text = "The address has not been verified yet! Click to verify",
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }

    }
}