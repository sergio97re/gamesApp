package com.example.gamesapp.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gamesapp.components.CardGames
import com.example.gamesapp.components.Loader
import com.example.gamesapp.components.MainTopBar
import com.example.gamesapp.ui.theme.BlueCard
import com.example.gamesapp.util.Constants.Companion.CUSTOM_BLACK
import com.example.gamesapp.viewModel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: GamesViewModel, navController: NavController){
    Scaffold(
        topBar = {
            MainTopBar(title = "APP GAMES", onClickBackButton = { /*TODO*/ }) {
                navController.navigate("SearchGameView")
            }
        }
    ) {
        ContentHomeView(viewModel, it, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentHomeView(viewModel: GamesViewModel, padding:PaddingValues, navController: NavController){
    //val games by viewModel.games.collectAsState()
    val gamesPage = viewModel.gamesPage.collectAsLazyPagingItems()
    var search by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val games by viewModel.games.collectAsState()
    
    Column(modifier = Modifier.padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) 
    {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = { active = false },
            active = active,
            onActiveChange =  { active = it },
            placeholder = { Text(text = "Search Game", color = Color.White) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "",
                    tint = Color.White)
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Close, contentDescription = "",
                    modifier = Modifier.clickable { navController.popBackStack() },
                    tint = Color.White
                    )
            },
            colors = SearchBarDefaults.colors(
                containerColor = BlueCard
            )
        ) {
            if (query.isNotEmpty()){
                val filterGames = games.filter { it.name.contains(query, ignoreCase = true) }
                filterGames.forEach {
                    Text(text = it.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 10.dp)
                            .clickable {
                                navController.navigate("DetailView/${it.id}/?${search}")
                            }
                    )
                }
            }
        }
        
        LazyColumn(modifier = Modifier
            .background(Color(CUSTOM_BLACK))
        ){
            items(gamesPage.itemCount){index ->
                val item = gamesPage[index]
                if (item != null){
                    CardGames(item) {
                        navController.navigate("DetailView/${item.id}/?${search}")
                    }
                }
            }
            when(gamesPage.loadState.append){
                is LoadState.NotLoading -> Unit
                LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Loader()
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Text(text = "Error al cargar")
                    }
                }
            }
        }
    }
}