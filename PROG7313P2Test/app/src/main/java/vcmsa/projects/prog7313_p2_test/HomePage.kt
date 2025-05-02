package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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

        val pieChart = findViewById<com.github.mikephil.charting.charts.PieChart>(R.id.pieChart)

        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUser(userId)
            }

            val categoryIds = categories.map { it.id }

            // Load only this user's budgets
            val budgets = withContext(Dispatchers.IO) {
                categoryIds.mapNotNull { categoryId ->
                    db.categoryBudgetDao().getBudgetByCategoryId(categoryId)
                }
            }

            // Load only this user's expenses
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getAllExpensesForUser(userId)
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
            val totalBudget = budgets.sumOf { it.maxGoal }
            val remaining = (totalBudget - totalSpent).coerceAtLeast(0.0)

            withContext(Dispatchers.Main) {
                val percentUsed = if (totalBudget > 0) {
                    (totalSpent / totalBudget * 100).coerceIn(0.0, 100.0).toFloat()
                } else {
                    0f
                }
                val percentRemaining = 100f - percentUsed

                val entries = listOf(
                    PieEntry(percentUsed, ""),
                    PieEntry(percentRemaining, "")
                )

                val dataSet = PieDataSet(entries, "")
                dataSet.setColors(
                    Color.parseColor("#5F39D3"),  // Violet
                    Color.parseColor("#E6E6E6")   // Light Gray
                )
                dataSet.sliceSpace = 4f
                dataSet.selectionShift = 0f
                dataSet.valueTextColor = Color.TRANSPARENT

                val data = PieData(dataSet)
                data.setDrawValues(false)

                pieChart.data = data
                pieChart.description.isEnabled = false
                pieChart.legend.isEnabled = false
                pieChart.setUsePercentValues(false)
                pieChart.setDrawEntryLabels(false)

                pieChart.setDrawHoleEnabled(true)
                pieChart.setHoleColor(Color.TRANSPARENT)
                pieChart.holeRadius = 80f
                pieChart.transparentCircleRadius = 85f

                pieChart.setDrawCenterText(true)
                pieChart.centerText = "%.0f%%\nUsed".format(percentUsed)
                pieChart.setCenterTextSize(20f)
                pieChart.setCenterTextColor(Color.BLACK)

                pieChart.invalidate()
            }
        }


        val recentExpensesList = findViewById<LinearLayout>(R.id.recentExpensesList)


        lifecycleScope.launch {
            val allExpenses = withContext(Dispatchers.IO) {
                db.expenseDao().getAllExpensesForUser(userId)
            }

            val recent = allExpenses
                .sortedByDescending { it.id } // or .date if formatted well
                .take(3)

            withContext(Dispatchers.Main) {
                recentExpensesList.removeAllViews()
                for (expense in recent) {
                    val expenseView = TextView(this@HomePage)
                    expenseView.text = "${expense.name}: R${"%.2f".format(expense.amount)} on ${expense.date}"
                    expenseView.setTextColor(Color.DKGRAY)
                    expenseView.textSize = 14f
                    recentExpensesList.addView(expenseView)
                }
            }
        }
    }
}
