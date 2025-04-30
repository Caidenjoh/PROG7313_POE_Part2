package vcmsa.projects.prog7313_p2_test.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double,
    val date: String,
    val description: String,
    val categoryId: Int,
    val userId: Int,
    val receiptImage: ByteArray? = null
)
