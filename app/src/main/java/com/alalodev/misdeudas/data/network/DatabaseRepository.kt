package com.alalodev.misdeudas.data.network

import com.alalodev.misdeudas.data.dto.TransactionDto
import com.alalodev.misdeudas.data.network.response.TransactionResponse
import com.alalodev.misdeudas.domain.model.TransactionModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val db: FirebaseFirestore) {

    companion object {
        const val USER_COLLECTION = "ratilla"
        const val FIELD_DATE = "date"
    }

    fun getTransactions(): Flow<List<TransactionModel>> {
        return db
            .collection(USER_COLLECTION)
            .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(TransactionResponse::class.java)
                    .mapNotNull { transactionResponse ->
                        transactionToDomain(transactionResponse)
                    }
            }
    }

    private fun transactionToDomain(tr: TransactionResponse): TransactionModel? {
        if (tr.date == null || tr.amount == null || tr.id == null || tr.title == null) return null
        val date = timestampToString(tr.date) ?: return null

        return TransactionModel(
            id = tr.id,
            title = tr.title,
            amount = tr.amount,
            date = date
        )
    }

    private fun timestampToString(timestamp: Timestamp?): String? {
        timestamp ?: return null
        return try {
            val date = timestamp.toDate()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            null
        }
    }

    fun addTransaction(dto: TransactionDto) {
        val customId = getCustomId()
        val model = hashMapOf(
            "id" to customId,
            "title" to dto.title,
            "date" to dto.date,
            "amount" to dto.amount
        )
        db.collection(USER_COLLECTION).document(customId).set(model)
    }

    private fun getCustomId(): String {
        return Date().time.toString()
    }

    fun removeTransaction(id: String) {
        db.collection(USER_COLLECTION).document(id).delete()
    }

}