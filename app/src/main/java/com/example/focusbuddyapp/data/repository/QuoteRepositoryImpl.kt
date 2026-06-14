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

    private val localQuotes = listOf(
        Quote("Focus is the art of knowing what to ignore.", "Academic Momentum"),
        Quote("Every task completed is one step closer to academic mastery.", "FocusBuddy"),
        Quote("Small steps every day lead to extraordinary results.", "FocusBuddy"),
        Quote("Your focus determines your reality.", "George Lucas"),
        Quote("Discipline is doing what needs to be done even when you don't feel like it.", "FocusBuddy"),
        Quote("Success is the sum of small efforts, repeated day-in and day-out.", "Robert Collier"),
        Quote("Don't let what you cannot do interfere with what you can do.", "John Wooden"),
        Quote("The secret of getting ahead is getting started.", "Mark Twain"),
        Quote("It's not that I'm so smart, it's just that I stay with problems longer.", "Albert Einstein"),
        Quote("Motivation is what gets you started. Habit is what keeps you going.", "Jim Ryun")
    )

    override suspend fun getDailyQuote(): Quote = withContext(Dispatchers.IO) {
        localQuotes.random()
    }
}
