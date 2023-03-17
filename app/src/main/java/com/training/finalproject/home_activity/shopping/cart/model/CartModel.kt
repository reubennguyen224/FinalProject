package com.training.finalproject.home_activity.shopping.cart.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.training.finalproject.model.HomeRecyclerViewItem
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cart")
@Parcelize
class Cart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uid") val uid: Int = 0,
    @ColumnInfo(name = "productID") val productID: Int = 0,
    @ColumnInfo(name = "number") val number: Int = 0
) : Parcelable {
    @Ignore
    constructor() : this(0)
}

@Parcelize
data class CartItem(
    val id: Int = 0,
    val product: HomeRecyclerViewItem.Product?,
    var number: Int = 0,
    var checked: Boolean = false
) : Parcelable