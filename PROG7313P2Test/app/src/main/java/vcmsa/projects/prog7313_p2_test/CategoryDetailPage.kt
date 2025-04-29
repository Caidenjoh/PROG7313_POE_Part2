package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import android.widget.LinearLayout
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
import vcmsa.projects.prog7313_p2_test.data.Expense

class CategoryDetailPage : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var expenseList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_detail_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val categoryId = intent.getIntExtra("categoryId", -1)
        val categoryName = intent.getStringExtra("categoryName") ?: "Unknown"
        val userId = intent.getIntExtra("userId", -1)

        val categoryTitleTextView = findViewById<TextView>(R.id.categoryDetailText)
        categoryTitleTextView.text = "Expenses for: \"$categoryName\""

        expenseList = findViewById(R.id.expenseListContainer)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).build()

        if (categoryId != -1 && userId != -1) {
            lifecycleScope.launch {
                val expenses = withContext(Dispatchers.IO) {
                    db.expenseDao().getExpensesForCategory(userId, categoryId)
                }

                withContext(Dispatchers.Main) {
                    displayExpenses(expenses)
                }
            }
        } else {
            Toast.makeText(this, "Invalid category or user ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayExpenses(expenses: List<Expense>) {
        expenseList.removeAllViews()
        val inflater = layoutInflater

        for (expense in expenses) {
            val cardView = inflater.inflate(R.layout.expense_item, expenseList, false)
            val nameText = cardView.findViewById<TextView>(R.id.expenseName)
            val amountText = cardView.findViewById<TextView>(R.id.expenseAmount)
            val dateText = cardView.findViewById<TextView>(R.id.expenseDate)

            nameText.text = expense.name
            amountText.text = "R${expense.amount}"
            dateText.text = expense.date

            expenseList.addView(cardView)
        }

        if (expenses.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No expenses found for this category."
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 16f
                setPadding(0, 8, 0, 8)
            }
            expenseList.addView(emptyText)
        }
    }

}
