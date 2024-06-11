package com.example.gamesapp.repository

import com.example.gamesapp.data.ApiGames
import com.example.gamesapp.model.GameList
import com.example.gamesapp.model.GamesModel
import com.example.gamesapp.model.SingleGameModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class GamesRepository @Inject constructor(private val apiGames: ApiGames) {

    suspend fun getGames(): List<GameList>? {
        val response = apiGames.getGames()
        if(response.isSuccessful){
            return response.body()?.results
        }
        return null
    }

    suspend fun getGamesPaging(page: Int, pageSize: Int): GamesModel {
        delay(2000L)
        return apiGames.getGamesPaging(page, pageSize)
    }

    suspend fun getGameById(id: Int): SingleGameModel? {
        val response = apiGames.getGameById(id)
        if(response.isSuccessful){
            return response.body()
        }
        return null
    }


    suspend fun getGameByName(name: String): SingleGameModel? {
        val response = apiGames.getGameByName(name)
        if(response.isSuccessful){
            return response.body()
        }
        return null
    }
}