package com.training.finalproject

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.training.finalproject.model.Cart
import com.training.finalproject.model.Product
import com.training.finalproject.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Dao
interface RoomDAO {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Insert
    suspend fun insertUserCart(cart: Cart)

    @Insert
    suspend fun insertProduct(vararg product: Product)

    @Query("SELECT * FROM cart")
    fun getProduct(): List<Cart>

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM cart WHERE productID = :idProduct")
    fun checkCartExist(idProduct: Int): List<Cart>

    @Update
    suspend fun updateCart(vararg cart: Cart)

    @Delete
    fun deleteCart(vararg cart: Cart)

    @Query("SELECT * FROM product WHERE id = :id")
    fun loadProduct(id: Int): Product

    @Query("SELECT COUNT(*) FROM cart WHERE uid = :id")
    suspend fun getBadge(id: Int): Int

    @Query("DELETE FROM product")
    fun cleanProduct()
}

@Database(entities = [User::class, Product::class, Cart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
