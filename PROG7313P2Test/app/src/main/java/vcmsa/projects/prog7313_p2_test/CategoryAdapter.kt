package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.prog7313_p2_test.data.Category

class CategoryAdapter(private val categories: List<Category>, private val userId: Int) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameText: TextView = itemView.findViewById(R.id.categoryNameText)
        val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)
        val setBudgetButton: Button = itemView.findViewById(R.id.setBudgetButton) // New button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameText.text = category.name

        holder.viewDetailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CategoryDetailPage::class.java).apply {
                putExtra("categoryId", category.id)
                putExtra("categoryName", category.name)
                putExtra("userId", userId)
            }
            context.startActivity(intent)
        }

        holder.setBudgetButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CategoryBudgetPage::class.java).apply {
                putExtra("categoryId", category.id)
                putExtra("categoryName", category.name)
                putExtra("userId", userId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size
}
