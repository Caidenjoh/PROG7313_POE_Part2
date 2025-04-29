package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import vcmsa.projects.prog7313_p2_test.data.Category

class AddCategoryPage : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var categoryNameInput: EditText
    private lateinit var saveCategoryButton: Button
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category_page)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        )
            .fallbackToDestructiveMigration() // ← Add this line
            .build()


        categoryNameInput = findViewById(R.id.categoryNameField)
        saveCategoryButton = findViewById(R.id.saveCategoryButton)

        userId = intent.getIntExtra("userId", -1)

        saveCategoryButton.setOnClickListener {
            val categoryName = categoryNameInput.text.toString()

            if (categoryName.isNotEmpty() && userId != -1) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.categoryDao().insertCategory(
                            Category(name = categoryName, userId = userId)
                        )
                    }
                    Toast.makeText(this@AddCategoryPage, "Category saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Please enter a valid category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
