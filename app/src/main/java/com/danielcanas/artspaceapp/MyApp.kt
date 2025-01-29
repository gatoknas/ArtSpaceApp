package com.danielcanas.artspaceapp

//import androidx.window.core.layout.WindowSizeClass

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.danielcanas.artspaceapp.ui.theme.ArtSpaceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp()
{

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var showAppBar by remember { mutableStateOf(true) }

    MaterialTheme {
        Scaffold(
            topBar = {
                if (!isLandscape) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Art Space Gallery")
                        },
                        windowInsets = TopAppBarDefaults.windowInsets
                    )
                }
            },
        ) { innerPadding ->
            ArtGallery(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}


@Composable
fun ArtGallery(modifier: Modifier = Modifier) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    //val showRightPane = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    val showRightPane = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)


    var artSequence by remember { mutableStateOf(0) }

    BuildGalleryLayout(
        showRightPane,
        artSequence,
        onNextClick = { artSequence = calculateNextSequence(artSequence) },
        onPreviousClick = { artSequence = calculatePreviousSequence(artSequence) }
    )
}

private fun calculateNextSequence(currentSequence: Int): Int {
    return if (currentSequence == 3) 0 else currentSequence + 1
}

private fun calculatePreviousSequence(currentSequence: Int): Int {
    return if (currentSequence == 0) 3 else currentSequence - 1
}

@Composable
fun BuildGalleryLayout(
    showRightPane: Boolean,
    artSequence: Int,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    if (!showRightPane) {
        SinglePanelGallery(scrollState, artSequence, onPreviousClick, onNextClick)
    } else {
        TwoPanelGallery(scrollState, artSequence, onPreviousClick, onNextClick)
    }
}

@Composable
fun TwoPanelGallery(
    scrollState: ScrollState,
    artSequence: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row (modifier = Modifier.fillMaxWidth().safeContentPadding())
    {
        SinglePanelGallery(scrollState, artSequence, onPreviousClick, onNextClick)
        Spacer(modifier = Modifier.width(16.dp))
        Surface (
            shadowElevation = 25.dp,
            modifier = Modifier.padding(25.dp).border(BorderStroke(1.dp, Color.Yellow)).width(300.dp))
        {
            Text(text = "This is my second panel")
        }
    }
}

@Composable
private fun SinglePanelGallery(
    scrollState: ScrollState,
    artSequence: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            //.padding(start = 10.dp, end = 10.dp, top = 90.dp)
            .border(BorderStroke(1.dp, Color.Green))
            .padding(25.dp)
            .width(300.dp)
            .verticalScroll(scrollState)
    ) {

        Surface(shadowElevation = 25.dp, modifier = Modifier.padding(16.dp)) {
        }

        val imageResourceIds = listOf(
            R.drawable.picture01w,
            R.drawable.picture02w,
            R.drawable.picture03w,
            R.drawable.picture04w
        )

        var myPicture = when (artSequence) {
            0 -> painterResource(imageResourceIds[0])
            1 -> painterResource(imageResourceIds[1])
            2 -> painterResource(imageResourceIds[2])
            else -> painterResource(imageResourceIds[3])
        }

        Image(
            painter = myPicture,
            contentDescription = "Image description",
            modifier = Modifier
                .heightIn(max = 500.dp)
                .widthIn(max = 600.dp)
                .border(BorderStroke(1.dp, Color.Blue)),
            contentScale = ContentScale.Crop
        )

        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)) {
            Text(
                text = "My artistic picture",
                color = Color.DarkGray,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "My subtitle",
                color = Color.DarkGray,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.padding(start = 60.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick =
                onPreviousClick
            ) {
                Text(text = "Previous")
            }

            Button(
                onClick =
                onNextClick
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtGalleryPreview() {
    ArtSpaceAppTheme {
        ArtGallery()
    }
}