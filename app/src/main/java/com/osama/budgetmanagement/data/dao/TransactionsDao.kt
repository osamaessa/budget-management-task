package com.osama.budgetmanagement.data.dao

import androidx.room.*
import com.osama.budgetmanagement.data.models.Transaction as TransactionModel
@Dao
interface TransactionsDao {

    @Query("SELECT * FROM transactions WHERE account_id = :accountId")
    fun getAllTransactions(accountId: Int): List<TransactionModel>

    @Query("SELECT SUM(dollars) FROM transactions WHERE type = 'income' AND account_id = :accountId")
    fun getIncomeDollarsSum(accountId: Int): Double

    @Query("SELECT SUM(dollars) FROM transactions WHERE type = 'spend' AND account_id = :accountId")
    fun getSpendDollarsSum(accountId: Int): Double

    @Query("SELECT SUM(dinnars) FROM transactions WHERE type = 'income' AND account_id = :accountId")
    fun getIncomeDinnarsSum(accountId: Int): Double

    @Query("SELECT SUM(dinnars) FROM transactions WHERE type = 'spend' AND account_id = :accountId")
    fun getSpendDinnarsSum(accountId: Int): Double

    @Insert
    fun insert(transaction: TransactionModel)

    @Delete
    fun delete(transaction: TransactionModel)

    @Update
    fun update(transaction: TransactionModel)
}