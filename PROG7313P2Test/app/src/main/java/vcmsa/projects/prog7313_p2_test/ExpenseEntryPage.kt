package vcmsa.projects.prog7313_p2_test

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import vcmsa.projects.prog7313_p2_test.data.Category
import vcmsa.projects.prog7313_p2_test.data.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseEntryPage : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_entry_page)

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).build()

        // Get userId passed from intent
        userId = intent.getIntExtra("userId", -1)

        val nameField = findViewById<EditText>(R.id.expenseNameField)
        val amountField = findViewById<EditText>(R.id.expenseAmountField)
        val dateField = findViewById<EditText>(R.id.expenseDateField)
        val descField = findViewById<EditText>(R.id.expenseDescriptionField)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val saveButton = findViewById<Button>(R.id.saveExpenseButton)

        // Initialize DatePickerDialog
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateField.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Make the date field clickable
        dateField.setFocusable(false)
        dateField.setClickable(true)
        dateField.setOnClickListener {
            datePickerDialog.show()
        }

        // Load categories into spinner
        lifecycleScope.launch {
            val categories = db.categoryDao().getCategoriesForUser(userId)
            val adapter = ArrayAdapter(
                this@ExpenseEntryPage,
                android.R.layout.simple_spinner_item,
                categories.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // Save button click listener
        saveButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val amount = amountField.text.toString().toDoubleOrNull() ?: 0.0
            val date = dateField.text.toString().trim()
            val desc = descField.text.toString().trim()
            val categoryName = categorySpinner.selectedItem?.toString() ?: ""

            if (name.isEmpty() || date.isEmpty() || categoryName.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val category = db.categoryDao().getCategoryByName(categoryName, userId)
                if (category != null) {
                    val expense = Expense(
                        name = name,
                        amount = amount,
                        date = date,
                        description = desc,
                        categoryId = category.id,
                        userId = userId
                    )
                    db.expenseDao().insertExpense(expense)

                    runOnUiThread {
                        Toast.makeText(this@ExpenseEntryPage, "Expense saved!", Toast.LENGTH_SHORT).show()
                        finish() // Close page after saving
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ExpenseEntryPage, "Category not found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
