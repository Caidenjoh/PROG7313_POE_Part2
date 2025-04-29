package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM Expense WHERE userId = :userId")
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    @Query("SELECT * FROM Expense WHERE categoryId = :categoryId AND userId = :userId")
    suspend fun getExpensesForCategory(userId: Int, categoryId: Int): List<Expense>

}
