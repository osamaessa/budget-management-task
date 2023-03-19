package com.osama.budgetmanagement.domain.usecase.account

import com.osama.budgetmanagement.domain.repositories.AccountsRepository
import javax.inject.Inject

class GetAllAccountsUseCase @Inject constructor(private val repository: AccountsRepository) {

    operator fun invoke() = repository.getAllAccounts()
}