package com.osama.budgetmanagement.domain.usecase.transaction

import com.osama.budgetmanagement.data.models.Transaction
import com.osama.budgetmanagement.domain.repositories.TransactionsRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(private val repository: TransactionsRepository) {

    operator fun invoke(transaction: Transaction) = repository.delete(transaction)
}