package com.training.finalproject.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class ReviewItem(
    val id: Int,
    val rate: Int,
    val user_name: String,
    val time: String,
    val content: String
)

class ReviewList : ArrayList<ReviewItem>()

@Entity(tableName = "product")
@Parcelize
class Product(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "seller") val seller: String = "",
    @ColumnInfo(name = "sale_price") var sale_price: Double = 0.0,
    @ColumnInfo(name = "price") val price: Double = 0.0,
    @ColumnInfo(name = "star") val star: Double = 0.0,
    @ColumnInfo(name = "image") val image: String = "",
    @ColumnInfo(name = "description") val description: String = "",
) : Parcelable {
    @Ignore
    constructor() : this(0)
}

@Parcelize
data class ProductX(
    val id: Int,
    val name: String,
    val seller: String,
    val sale_price: Double,
    val price: Double,
    val star: Double,
    val image: String,
) : Parcelable

class BannerList: ArrayList<HomeRecyclerViewItem.Banner>()

sealed class HomeRecyclerViewItem {
    data class Banner(val id: Int, val image: String = "") : HomeRecyclerViewItem()
    data class Title(val header: String) : HomeRecyclerViewItem()

    @Parcelize
    data class Product(
        val id: Int,
        val name: String,
        val seller: String,
        val sale_price: Double,
        val price: Double,
        val star: Double,
        val image: String,
        val description: String = "",
    ) : HomeRecyclerViewItem(), Parcelable
}

