package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Category::class, Expense::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}


