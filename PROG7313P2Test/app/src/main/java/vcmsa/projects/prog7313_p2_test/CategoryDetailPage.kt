package vcmsa.projects.prog7313_p2_test

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
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
import java.text.SimpleDateFormat
import java.util.*

class CategoryDetailPage : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var expenseList: LinearLayout
    private lateinit var startDateField: EditText
    private lateinit var endDateField: EditText
    private lateinit var applyDateFilterButton: Button

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var userId: Int = -1
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_detail_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryId = intent.getIntExtra("categoryId", -1)
        val categoryName = intent.getStringExtra("categoryName") ?: "Unknown"
        userId = intent.getIntExtra("userId", -1)

        findViewById<TextView>(R.id.categoryDetailText).text = "  Expenses for: \"$categoryName\""
        expenseList = findViewById(R.id.expenseListContainer)
        startDateField = findViewById(R.id.startDateField)
        endDateField = findViewById(R.id.endDateField)
        applyDateFilterButton = findViewById(R.id.applyDateFilterButton)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).build()

        // Load all expenses initially
        if (categoryId != -1 && userId != -1) {
            loadExpenses()
        } else {
            Toast.makeText(this, "Invalid category or user ID.", Toast.LENGTH_SHORT).show()
        }

        // Date pickers
        startDateField.setOnClickListener {
            showDatePicker { date -> startDateField.setText(date) }
        }

        endDateField.setOnClickListener {
            showDatePicker { date -> endDateField.setText(date) }
        }

        // Filter button
        applyDateFilterButton.setOnClickListener {
            val startDateStr = startDateField.text.toString()
            val endDateStr = endDateField.text.toString()

            if (startDateStr.isBlank() || endDateStr.isBlank()) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val filteredExpenses = withContext(Dispatchers.IO) {
                    val allExpenses = db.expenseDao().getExpensesForCategory(userId, categoryId)
                    allExpenses.filter { expense ->
                        try {
                            val expDate = dateFormat.parse(expense.date)
                            val start = dateFormat.parse(startDateStr)
                            val end = dateFormat.parse(endDateStr)
                            expDate != null && start != null && end != null && !expDate.before(start) && !expDate.after(end)
                        } catch (e: Exception) {
                            false
                        }
                    }
                }
                displayExpenses(filteredExpenses)
            }
        }


        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseEntryPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish() // Or navigate to home screen if needed
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val dateStr = "${dayOfMonth}/${month + 1}/$year"
                onDateSelected(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForCategory(userId, categoryId)
            }
            displayExpenses(expenses)
        }
    }

    private fun displayExpenses(expenses: List<Expense>) {
        expenseList.removeAllViews()
        val inflater = layoutInflater

        var totalAmount = 0.0 // Variable to store the total amount spent

        for (expense in expenses) {
            val cardView = inflater.inflate(R.layout.expense_item, expenseList, false)
            cardView.findViewById<TextView>(R.id.expenseName).text = expense.name
            cardView.findViewById<TextView>(R.id.expenseAmount).text = "R${expense.amount}"
            cardView.findViewById<TextView>(R.id.expenseDate).text = expense.date
            expenseList.addView(cardView)

            // Add the amount of the current expense to the totalAmount
            totalAmount += expense.amount
        }

        // Update the TextView for the total amount spent
        val totalAmountTextView = findViewById<TextView>(R.id.totalAmountSpentText)
        totalAmountTextView.text = "Total Spent: R${"%.2f".format(totalAmount)}"  // Format to 2 decimal places

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
