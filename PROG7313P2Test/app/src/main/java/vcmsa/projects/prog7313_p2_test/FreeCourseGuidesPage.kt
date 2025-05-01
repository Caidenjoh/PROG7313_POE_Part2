package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FreeCourseGuidesPage : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_free_course_guides_page)
        supportActionBar?.title = ""

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupCard(
            R.id.khanCard,
            "A step by step tutorial on how to manage your personal finance.",
            "https://www.khanacademy.org/college-careers-more/personal-finance/pf-saving-and-budgeting",
            R.drawable.khan_logo
        )

        setupCard(
            R.id.oldMutualCard,
            "Six steps to taking control of your debts, insurance, life policies and more.",
            "https://www.oldmutual.co.za/articles/springclean-your-finances/",
            R.drawable.oldmutual_logo
        )

        setupCard(
            R.id.openLearnCard,
            "Free courses on managing your money with different levels and durations.",
            "https://www.open.edu/openlearn/money-management/free-courses?filter=date/grid/107/all/all/all",
            R.drawable.openlearn_logo
        )

        setupCard(
            R.id.udemyCard,
            "Learn about personal budgets, interest, savings, credit, insurance and retirement planning.",
            "https://www.udemy.com/course/fnb-financial-literacy/",
            R.drawable.udemy
        )

        findViewById<Button>(R.id.btnReturn).setOnClickListener {
            finish() // Or navigate to home screen if needed
        }
    }

    private fun setupCard(cardId: Int, description: String, url: String, imageResId: Int) {
        val card = findViewById<LinearLayout>(cardId)
        val descriptionText = card.findViewById<TextView>(R.id.courseDescription)
        val linkText = card.findViewById<TextView>(R.id.courseLink)
        val imageView = card.findViewById<ImageView>(R.id.courseLogo)

        descriptionText.text = description
        linkText.text = url
        imageView.setImageResource(imageResId)

        linkText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}
