package com.training.finalproject.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user")
@Parcelize
class User(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String? = null,
    @ColumnInfo(name = "fullName") val fullName: String? = null,
    @ColumnInfo(name = "balance") val balance: Double = 0.0,
    @ColumnInfo(name = "point") val point: Int = 0,
) : Parcelable {
    @Ignore
    constructor() : this(0) {
    }
}