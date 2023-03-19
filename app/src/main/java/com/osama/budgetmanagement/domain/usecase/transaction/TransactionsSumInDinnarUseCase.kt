package com.osama.budgetmanagement.domain.usecase.transaction

import com.osama.budgetmanagement.domain.repositories.TransactionsRepository
import javax.inject.Inject

class TransactionsSumInDinnarUseCase @Inject constructor(private val repository: TransactionsRepository) {

    operator fun invoke(accountId: Int) = repository.getSumInDinnar(accountId)
}