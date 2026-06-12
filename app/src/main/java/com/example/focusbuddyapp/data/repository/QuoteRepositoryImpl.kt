package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.remote.api.QuoteApiService
import com.example.focusbuddyapp.data.mapper.toDomain
import com.example.focusbuddyapp.domain.model.Quote
import com.example.focusbuddyapp.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuoteRepositoryImpl(
    private val quoteApiService: QuoteApiService
) : QuoteRepository {
    override suspend fun getDailyQuote(): Quote = withContext(Dispatchers.IO) {
        runCatching {
            quoteApiService.getDailyQuote().toDomain()
        }.getOrDefault(
            Quote(
                content = "The only way to do great work is to love what you do.",
                author = "Steve Jobs"
            )
        )
    }
}
