package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
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

        val iconImage = fullLayout.findViewById<ImageView>(R.id.iconImage2)
        iconImage.setOnClickListener {
            val popup = android.widget.PopupMenu(this, iconImage)
            popup.menuInflater.inflate(R.menu.logout_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_logout) {
                    showLogoutDialog()
                    true
                } else {
                    false
                }
            }
            popup.show()
        }


    }

    private fun showLogoutDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
