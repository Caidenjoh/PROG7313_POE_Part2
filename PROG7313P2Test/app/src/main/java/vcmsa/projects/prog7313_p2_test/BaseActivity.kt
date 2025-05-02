package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun setContentView(layoutResID: Int) {
        val fullLayout = layoutInflater.inflate(R.layout.drawer_base_layout, null)
        val contentFrame = fullLayout.findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
        super.setContentView(fullLayout)

        drawerLayout = fullLayout.findViewById(R.id.drawer_layout)
        navView = fullLayout.findViewById(R.id.nav_view)
        val toolbar: Toolbar = fullLayout.findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            val userId = intent.getIntExtra("userId", -1)

            val intent = when (menuItem.itemId) {
                R.id.nav_home -> Intent(this, HomePage::class.java)
                R.id.nav_categories -> Intent(this, CategoriesPage::class.java)
                R.id.nav_expenses -> Intent(this, ExpenseEntryPage::class.java)
                R.id.nav_rewards -> Intent(this, RewardsPage::class.java)
                R.id.nav_courses -> Intent(this, FreeCourseGuidesPage::class.java)
                else -> null
            }

            intent?.putExtra("userId", userId)
            intent?.let { startActivity(it) }

            drawerLayout.closeDrawers()
            true
        }
    }
}
