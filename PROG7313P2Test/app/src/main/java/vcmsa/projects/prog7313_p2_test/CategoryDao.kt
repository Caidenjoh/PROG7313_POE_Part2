package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    suspend fun getCategoriesForUser(userId: Int): List<Category>

    @Query("SELECT * FROM categories WHERE name = :name AND userId = :userId LIMIT 1")
    suspend fun getCategoryByName(name: String, userId: Int): Category?

}
