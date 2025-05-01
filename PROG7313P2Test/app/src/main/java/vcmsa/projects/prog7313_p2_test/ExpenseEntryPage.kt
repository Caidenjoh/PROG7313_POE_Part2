package vcmsa.projects.prog7313_p2_test

import android.app.DatePickerDialog
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import vcmsa.projects.prog7313_p2_test.data.Expense
import java.util.*

class ExpenseEntryPage : BaseActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private val REQUEST_IMAGE_PICK = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_entry_page)
        supportActionBar?.title = ""

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        )
//            .fallbackToDestructiveMigration()
            .build()

        userId = intent.getIntExtra("userId", -1)

        val nameField = findViewById<EditText>(R.id.expenseNameField)
        val amountField = findViewById<EditText>(R.id.expenseAmountField)
        val dateField = findViewById<EditText>(R.id.expenseDateField)
        val descField = findViewById<EditText>(R.id.expenseDescriptionField)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val saveButton = findViewById<Button>(R.id.saveExpenseButton)
        val attachImageButton = findViewById<Button>(R.id.attachImageButton)
        val receiptImageView = findViewById<ImageView>(R.id.receiptImageView)

        // Date Picker Setup
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

        dateField.setOnClickListener { datePickerDialog.show() }
        dateField.isFocusable = false
        dateField.isClickable = true

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

        // Attach Image
        attachImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // Save Expense
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
                    val receiptImageBytes = selectedImageUri?.let { uriToByteArray(it) }

                    val expense = Expense(
                        name = name,
                        amount = amount,
                        date = date,
                        description = desc,
                        categoryId = category.id,
                        userId = userId,
                        receiptImage = receiptImageBytes
                    )

                    db.expenseDao().insertExpense(expense)

                    runOnUiThread {
                        Toast.makeText(this@ExpenseEntryPage, "Expense saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ExpenseEntryPage, "Category not found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        findViewById<Button>(R.id.returnToPreviousPageButton).setOnClickListener {
            finish() // Or navigate to home screen if needed
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val receiptImageView = findViewById<ImageView>(R.id.receiptImageView)
            receiptImageView.setImageURI(selectedImageUri)
            receiptImageView.visibility = View.VISIBLE
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return contentResolver.openInputStream(uri)?.use { it.readBytes() }
    }

}
