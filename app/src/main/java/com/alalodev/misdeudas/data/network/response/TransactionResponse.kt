package com.alalodev.misdeudas.data.network.response

import com.google.firebase.Timestamp

data class TransactionResponse(
    val id: String? = null,
    val title: String? = null,
    val amount: Double? = null,
    val date: Timestamp? = null
)
