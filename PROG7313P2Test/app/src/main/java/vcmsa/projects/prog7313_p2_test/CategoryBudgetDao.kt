package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryBudgetDao {

    @Insert
    suspend fun insertBudget(budget: CategoryBudget)

    @Query("SELECT * FROM category_budgets WHERE categoryId = :categoryId LIMIT 1")
    suspend fun getBudgetByCategoryId(categoryId: Int): CategoryBudget?

    @Update
    suspend fun updateBudget(budget: CategoryBudget)

    @Query("SELECT * FROM category_budgets")
    suspend fun getAllBudgets(): List<CategoryBudget>

}

