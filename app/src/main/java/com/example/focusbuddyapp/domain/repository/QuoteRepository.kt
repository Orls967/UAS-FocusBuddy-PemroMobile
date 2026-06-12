package com.example.focusbuddyapp.domain.repository

import com.example.focusbuddyapp.domain.model.Quote

interface QuoteRepository {
    suspend fun getDailyQuote(): Quote
}
