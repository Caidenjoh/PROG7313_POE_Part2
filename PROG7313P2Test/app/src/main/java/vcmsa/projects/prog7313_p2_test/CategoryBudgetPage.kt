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
import java.util.*

class CategoryBudgetPage : AppCompatActivity() {

    private var categoryId: Int = -1
    private var userId: Int = -1
    private lateinit var categoryName: String
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_budget_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get category ID, user ID, and name from intent
        categoryId = intent.getIntExtra("categoryId", -1)
        userId = intent.getIntExtra("userId", -1)
        categoryName = intent.getStringExtra("categoryName") ?: "Unknown"

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).build()

        val headingText = findViewById<TextView>(R.id.budgetHeadingText)
        headingText.text = "Budget for: $categoryName"

        val minGoalInput = findViewById<EditText>(R.id.minGoalInput)
        val maxGoalInput = findViewById<EditText>(R.id.maxGoalInput)
        val saveButton = findViewById<Button>(R.id.saveBudgetButton)
        val spendingSummaryText = findViewById<TextView>(R.id.spendingSummaryText)

        // Fetch and show existing budget
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

        // Fetch and show current month spending
        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForCategory(userId, categoryId)
            }

            val now = Calendar.getInstance()
            val currentMonth = now.get(Calendar.MONTH) + 1
            val currentYear = now.get(Calendar.YEAR)

            val currentMonthExpenses = expenses.filter { expense ->
                try {
                    val parts = expense.date.split("/")
                    val day = parts[0].toInt()
                    val month = parts[1].toInt()
                    val year = parts[2].toInt()
                    month == currentMonth && year == currentYear
                } catch (e: Exception) {
                    false
                }
            }

            val totalSpending = currentMonthExpenses.sumOf { it.amount }

            // Fetch budget info to compare against spending
            val budget = withContext(Dispatchers.IO) {
                db.categoryBudgetDao().getBudgetByCategoryId(categoryId)
            }

            withContext(Dispatchers.Main) {
                spendingSummaryText.text = "Total spent this month: R%.2f".format(totalSpending)

                // Determine status based on budget limits
                if (budget != null) {
                    val status = when {
                        totalSpending > budget.maxGoal -> {
                            // Exceeded maximum budget, change color to red
                            findViewById<TextView>(R.id.spendingStatusText).setTextColor(
                                resources.getColor(android.R.color.holo_red_light, null) // Predefined red color
                            )
                            "Exceeded Maximum Budget"
                        }
                        totalSpending < budget.minGoal -> {
                            // Below minimum budget, no color change here unless you want to highlight it
                            findViewById<TextView>(R.id.spendingStatusText).setTextColor(
                                resources.getColor(android.R.color.holo_green_dark, null) // Set to default color
                            )
                            "Below Minimum Budget"
                        }
                        else -> {
                            // Within budget, reset color to default
                            findViewById<TextView>(R.id.spendingStatusText).setTextColor(
                                resources.getColor(android.R.color.holo_green_dark, null) // Default color
                            )
                            "Within Budget"
                        }
                    }
                    findViewById<TextView>(R.id.spendingStatusText).text = "Status: $status"
                } else {
                    findViewById<TextView>(R.id.spendingStatusText).text = "Status: No Budget Set"
                }
            }
        }


        // Save budget button logic
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

            lifecycleScope.launch {
                val existingBudget = withContext(Dispatchers.IO) {
                    db.categoryBudgetDao().getBudgetByCategoryId(categoryId)
                }

                if (existingBudget != null) {
                    // Update existing
                    val updatedBudget = existingBudget.copy(minGoal = minGoal, maxGoal = maxGoal)
                    withContext(Dispatchers.IO) {
                        db.categoryBudgetDao().updateBudget(updatedBudget)
                    }
                } else {
                    // Insert new
                    val newBudget = CategoryBudget(categoryId = categoryId, minGoal = minGoal, maxGoal = maxGoal)
                    withContext(Dispatchers.IO) {
                        db.categoryBudgetDao().insertBudget(newBudget)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CategoryBudgetPage, "Budget saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


        val returnToCategoryButton = findViewById<Button>(R.id.returnToCategoryPageButton)
        returnToCategoryButton.setOnClickListener {
            // Navigate back to Category Page (finish the current activity or use an Intent)
            finish() // This will take the user back to the previous screen, assuming CategoryPage was the previous one.
        }

    }
}
