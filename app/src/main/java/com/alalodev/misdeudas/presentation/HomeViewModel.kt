package com.alalodev.misdeudas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alalodev.misdeudas.data.dto.TransactionDto
import com.alalodev.misdeudas.data.network.DatabaseRepository
import com.alalodev.misdeudas.domain.model.TransactionModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.nanoseconds

@HiltViewModel
class HomeViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            databaseRepository.getTransactions().collect { transactions ->

                val totalAmount = transactions.sumOf { it.amount }
                val totalAmountFormatted = String.format("%.2f ", totalAmount)

                _uiState.update {
                    it.copy(
                        transactions = transactions,
                        totalAmount = totalAmountFormatted
                    )
                }
            }
        }
    }

    fun onAddTransactionSelected() {
        _uiState.update { it.copy(showTransactionDialog = true) }
    }

    fun dissmissDialog() {
        _uiState.update { it.copy(showTransactionDialog = false) }
    }

    fun addTransaction(title: String, amount: String, date: Long?) {
        val dto = prepareDTO(title, amount, date)
        if (dto != null) {
            viewModelScope.launch {
                databaseRepository.addTransaction(dto)
            }
        }
        dissmissDialog()
    }

    private fun prepareDTO(title: String, amount: String, date: Long?): TransactionDto? {
        if (title.isBlank() || amount.isBlank()) return null
        val timestamp = if (date != null) {
            val seconds = date / 1000
            val nanoseconds = ((date % 1000) * 1000000).toInt()
            Timestamp(seconds, nanoseconds)

        } else {
            Timestamp.now()
        }
        return try {
            TransactionDto(title,amount.toDouble(),timestamp)
        }catch (e:Exception){
            null
        }
    }

    fun onItemRemove(id: String) {
        databaseRepository.removeTransaction(id)
    }

}

data class HomeUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionModel> = emptyList(),
    val totalAmount: String = "",
    val showTransactionDialog: Boolean = false
)