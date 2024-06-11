package com.example.gamesapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gamesapp.components.MainImageDetail
import com.example.gamesapp.components.MainTopBar
import com.example.gamesapp.components.MetaWebsite
import com.example.gamesapp.components.ReviewCard
import com.example.gamesapp.util.Constants.Companion.CUSTOM_BLACK
import com.example.gamesapp.viewModel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(viewModel: GamesViewModel, navController: NavController, id: Int, name: String?){
    LaunchedEffect(Unit){
        if (id == 0){
            name?.let { viewModel.getGamesByName(it.replace(" ", "-")) }
        }else{
            viewModel.getGamesById(id)
        }
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.clean()
        }
    }

    Scaffold(
        topBar = {
            MainTopBar(title = "Volver a juegos", showBackButton = true ,onClickBackButton = {
                navController.popBackStack()
            }) {}
        }
    ) {
        ContentDetailView(it, viewModel)
    }
}

@Composable
fun ContentDetailView(padding: PaddingValues, viewModel: GamesViewModel){
    val state = viewModel.state
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .padding(padding)
        .verticalScroll(scrollState)
        .background(Color(CUSTOM_BLACK)),
        horizontalAlignment = Alignment.CenterHorizontally) {
        MainImageDetail(image = state.background_image)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = viewModel.state.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 34.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 5.dp)
        ) {
            MetaWebsite(state.website)
            ReviewCard(state.metacritic)
        }

        //val scroll = rememberScrollState(0)
        Box(modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            .background(Color.DarkGray)){
            Text(text = state.description_raw,
                color = Color.White,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

    }
}