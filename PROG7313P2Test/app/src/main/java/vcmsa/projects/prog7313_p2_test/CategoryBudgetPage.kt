package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import vcmsa.projects.prog7313_p2_test.data.CategoryBudget

class CategoryBudgetPage : AppCompatActivity() {

    private var categoryId: Int = -1
    private lateinit var categoryName: String
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_budget_page)

        // Apply insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get category name and ID from Intent
        categoryId = intent.getIntExtra("categoryId", -1)
        categoryName = intent.getStringExtra("categoryName") ?: "Unknown"

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).build()

        // Set heading text
        val headingText = findViewById<TextView>(R.id.budgetHeadingText)
        headingText.text = "Budget for: $categoryName"

        // Initialize UI elements
        val minGoalInput = findViewById<EditText>(R.id.minGoalInput)
        val maxGoalInput = findViewById<EditText>(R.id.maxGoalInput)
        val saveButton = findViewById<Button>(R.id.saveBudgetButton)

        // Fetch and display existing budget data if available
        lifecycleScope.launch {
            val existingBudget = withContext(Dispatchers.IO) {
                db.categoryBudgetDao().getBudgetByCategoryId(categoryId)
            }

            withContext(Dispatchers.Main) {
                if (existingBudget != null) {
                    minGoalInput.setText(existingBudget.minGoal.toString())
                    maxGoalInput.setText(existingBudget.maxGoal.toString())
                }
            }
        }

        // Save button click listener
        saveButton.setOnClickListener {
            val minGoalStr = minGoalInput.text.toString()
            val maxGoalStr = maxGoalInput.text.toString()

            if (minGoalStr.isEmpty() || maxGoalStr.isEmpty()) {
                Toast.makeText(this, "Please enter both minimum and maximum goals.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val minGoal = minGoalStr.toDoubleOrNull()
            val maxGoal = maxGoalStr.toDoubleOrNull()

            if (minGoal == null || maxGoal == null) {
                Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save budget data
            lifecycleScope.launch {
                val budget = CategoryBudget(
                    categoryId = categoryId,
                    minGoal = minGoal,
                    maxGoal = maxGoal
                )

                withContext(Dispatchers.IO) {
                    // Insert or update the budget in the database
                    db.categoryBudgetDao().insertBudget(budget)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CategoryBudgetPage, "Budget saved!", Toast.LENGTH_SHORT).show()
                    finish() // Go back to the previous page
                }
            }
        }
    }
}
