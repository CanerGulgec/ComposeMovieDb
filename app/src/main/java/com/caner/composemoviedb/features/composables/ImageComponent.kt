package com.caner.composemoviedb.features.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.caner.composemoviedb.R

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    image: String?,
    description: String = "",
    fadeDuration: Int = 0
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(fadeDuration)
            .build(),
        //placeholder = painterResource(R.drawable.bg_image_placeholder),
        error = painterResource(R.drawable.bg_image_placeholder),
        contentDescription = description,
        contentScale = ContentScale.Crop
    )
}