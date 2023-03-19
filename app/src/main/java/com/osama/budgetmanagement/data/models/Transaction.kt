package com.osama.budgetmanagement.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "date") var date: Long?,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "dollars") var dollars: Double?,
    @ColumnInfo(name = "dinnars") var dinnars: Double?,
    @ColumnInfo(name = "account_id") var accountId: Int?,
): Parcelable

enum class TransactionType(val value: String) {
    Income("income"), Spend("spend")
}
enum class TransactionCurrency(val value: String) {
    Dollar("dollar"), Dinnar("dinnar")
}