// file: data/CategoryBudget.kt
package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_budgets")
data class CategoryBudget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val minGoal: Double,
    val maxGoal: Double
)

