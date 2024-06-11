package com.example.gamesapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gamesapp.data.GamesDataSource
import com.example.gamesapp.model.GameList
import com.example.gamesapp.repository.GamesRepository
import com.example.gamesapp.state.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: GamesRepository): ViewModel() {

    private val _games = MutableStateFlow<List<GameList>>(emptyList())
    val games = _games.asStateFlow()

    var state by mutableStateOf(GameState())
        private set

    init {
        fetchGames()
    }

    val gamesPage = Pager(PagingConfig(pageSize = 5)){
        GamesDataSource(repository)
    }.flow.cachedIn(viewModelScope)

    private fun fetchGames(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = repository.getGames()
                _games.value = result ?: emptyList()
            }
        }
    }

    fun getGamesById(id: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = repository.getGameById(id)
                state = state.copy(
                    name = result?.name ?: "",
                    description_raw = result?.description_raw ?: "",
                    metacritic = result?.metacritic ?: 111,
                    website = result?.website ?: "sin web",
                    background_image = result?.background_image ?: "",
                )
            }
        }
    }

    fun getGamesByName(name: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = repository.getGameByName(name)
                state = state.copy(
                    name = result?.name ?: "",
                    description_raw = result?.description_raw ?: "",
                    metacritic = result?.metacritic ?: 111,
                    website = result?.website ?: "sin web",
                    background_image = result?.background_image ?: "",
                )
            }
        }
    }

    fun clean(){
        state = state.copy(
            name = "",
            description_raw = "",
            metacritic = 0,
            website = "",
            background_image = "",
        )
    }
}