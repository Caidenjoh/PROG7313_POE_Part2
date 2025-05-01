package vcmsa.projects.prog7313_p2_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button

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

        categoriesButton.setOnClickListener {
            val intent = Intent(this, CategoriesPage::class.java)
            intent.putExtra("userId", userId) // Pass userId here
            startActivity(intent)
        }

        expenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseEntryPage::class.java)
            intent.putExtra("userId", userId) // Pass userId here
            startActivity(intent)
        }

        rewardsButton.setOnClickListener {
            val intent = Intent(this, RewardsPage::class.java)
            intent.putExtra("userId", userId) // Pass userId here
            startActivity(intent)
        }

        courseGuidesButton.setOnClickListener {
            val intent = Intent(this, FreeCourseGuidesPage::class.java)
            intent.putExtra("userId", userId) // Pass userId here
            startActivity(intent)
        }
    }
}