package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import java.util.*

class HomePage : BaseActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        supportActionBar?.title = ""
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getIntExtra("userId", -1)

        val categoriesButton = findViewById<Button>(R.id.CategoriesButton)
        val expenseButton = findViewById<Button>(R.id.ExpensesButton)
        val rewardsButton = findViewById<Button>(R.id.RewardsButton)
        val courseGuidesButton = findViewById<Button>(R.id.CourseGuidesButton)
        val overspendText = findViewById<TextView>(R.id.tvClosestOverspend)

        categoriesButton.setOnClickListener {
            val intent = Intent(this, CategoriesPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        expenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseEntryPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        rewardsButton.setOnClickListener {
            val intent = Intent(this, RewardsPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        courseGuidesButton.setOnClickListener {
            val intent = Intent(this, FreeCourseGuidesPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // -------- FETCH AND DISPLAY CLOSEST TO OVERSPEND ----------
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).fallbackToDestructiveMigration().build()

        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUser(userId)
            }

            var closestCategoryName: String? = null
            var closestRemaining = Double.MAX_VALUE
            var overspentCount = 0
            var totalWithBudgets = 0

            for (category in categories) {
                val expenses = withContext(Dispatchers.IO) {
                    db.expenseDao().getExpensesForCategory(userId, category.id)
                }

                val now = Calendar.getInstance()
                val currentMonth = now.get(Calendar.MONTH) + 1
                val currentYear = now.get(Calendar.YEAR)

                val currentMonthExpenses = expenses.filter {
                    try {
                        val parts = it.date.split("/")
                        val month = parts[1].toInt()
                        val year = parts[2].toInt()
                        month == currentMonth && year == currentYear
                    } catch (e: Exception) {
                        false
                    }
                }

                val totalSpent = currentMonthExpenses.sumOf { it.amount }

                val budget = withContext(Dispatchers.IO) {
                    db.categoryBudgetDao().getBudgetByCategoryId(category.id)
                }

                if (budget != null) {
                    totalWithBudgets++
                    val remaining = budget.maxGoal - totalSpent
                    if (remaining < 0) {
                        overspentCount++
                    } else if (remaining < closestRemaining) {
                        closestRemaining = remaining
                        closestCategoryName = category.name
                    }
                }
            }

            withContext(Dispatchers.Main) {
                if (overspentCount == totalWithBudgets && totalWithBudgets > 0) {
                    overspendText.text = "All budgets are overspent!"
                    overspendText.setTextColor(resources.getColor(android.R.color.holo_red_light, null))
                } else if (closestCategoryName != null) {
                    overspendText.text = "R %.2f Till Overspend in %s".format(closestRemaining, closestCategoryName)
                    overspendText.setTextColor(resources.getColor(android.R.color.black, null))
                } else {
                    overspendText.text = "No budgets available."
                    overspendText.setTextColor(resources.getColor(android.R.color.black, null))
                }
            }
        }
    }
}
