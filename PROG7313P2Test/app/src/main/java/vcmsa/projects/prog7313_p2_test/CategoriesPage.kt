package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import vcmsa.projects.prog7313_p2_test.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_p2_test.data.Category

class CategoriesPage : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_page)

        userId = intent.getIntExtra("userId", -1)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-database.db"
        ).fallbackToDestructiveMigration().build()

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addCategoryButton = findViewById<Button>(R.id.addCategoryButton)
        addCategoryButton.setOnClickListener {
            val intent = Intent(this, AddCategoryPage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories: List<Category> = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUser(userId)
            }
            adapter = CategoryAdapter(categories, userId)
            recyclerView.adapter = adapter
        }
    }
}
