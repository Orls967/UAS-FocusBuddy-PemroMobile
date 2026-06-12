package com.example.focusbuddyapp.data.remote.api

import com.example.focusbuddyapp.data.remote.dto.QuoteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApiService {
    @GET("random")
    suspend fun getDailyQuote(
        @Query("tags") tags: String = "inspirational,study"
    ): QuoteDto
}
