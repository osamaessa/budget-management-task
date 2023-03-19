package com.osama.budgetmanagement.domain.usecase.account

import com.osama.budgetmanagement.data.models.Account
import com.osama.budgetmanagement.domain.repositories.AccountsRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(private val repository: AccountsRepository) {

    operator fun invoke(account: Account) = repository.update(account)
}