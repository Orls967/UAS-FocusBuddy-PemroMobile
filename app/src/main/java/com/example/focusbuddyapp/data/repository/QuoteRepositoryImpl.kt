package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.mapper.toDomain
import com.example.focusbuddyapp.data.remote.api.QuoteApiService
import com.example.focusbuddyapp.domain.model.Quote
import com.example.focusbuddyapp.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuoteRepositoryImpl(
    private val quoteApiService: QuoteApiService
) : QuoteRepository {

    private val fallbackQuotes = listOf(
        Quote("Focus is the art of knowing what to ignore.", "Academic Momentum"),
        Quote("Every task completed is one step closer to academic mastery.", "FocusBuddy"),
        Quote("Small steps every day lead to extraordinary results.", "FocusBuddy"),
        Quote("Your focus determines your reality.", "FocusBuddy"),
        Quote("Discipline is doing what needs to be done even when you don't feel like it.", "FocusBuddy")
    )

    override suspend fun getDailyQuote(): Quote = withContext(Dispatchers.IO) {
        runCatching { quoteApiService.getRandomQuote().toDomain() }
            .getOrElse { fallbackQuotes.random() }
    }
}
