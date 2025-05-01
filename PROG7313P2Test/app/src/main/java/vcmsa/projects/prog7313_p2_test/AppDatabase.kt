package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Category::class, Expense::class, CategoryBudget::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryBudgetDao(): CategoryBudgetDao
}



