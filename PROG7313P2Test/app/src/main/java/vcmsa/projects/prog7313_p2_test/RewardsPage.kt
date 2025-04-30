package vcmsa.projects.prog7313_p2_test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RewardsPage : AppCompatActivity() {


    private var userId: Int = -1
    private lateinit var pointsText: TextView
    private lateinit var rewardsContainer: LinearLayout
    private var points = 6500

    data class Reward(val title: String, val description: String, val cost: Int)

    private val rewards = listOf(
        Reward("NuMetro", "15% Off Your Next Movie Ticket", 4000),
        Reward("Mr Price", "30% Off Your Next Purchase", 6000),
        Reward("Uber", "10% Off Your Next Ride", 2500),
        Reward("KFC", "5% Off Your Next Meal Purchase", 1000),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards_page)

        userId = intent.getIntExtra("userId", -1)

        pointsText = findViewById(R.id.pointsText)
        rewardsContainer = findViewById(R.id.rewardsContainer)
        val returnButton: Button = findViewById(R.id.returnButton)

        pointsText.text = "Points Accumulated: $points"

        rewards.forEach { reward ->
            val card = layoutInflater.inflate(R.layout.reward_card_item, rewardsContainer, false)
            val title = card.findViewById<TextView>(R.id.rewardTitle)
            val description = card.findViewById<TextView>(R.id.rewardDescription)
            val cost = card.findViewById<TextView>(R.id.rewardCost)
            val purchaseButton = card.findViewById<Button>(R.id.purchaseButton)

            title.text = reward.title
            description.text = reward.description
            cost.text = "Costs: ${reward.cost} Points"

            purchaseButton.setOnClickListener {
                if (points >= reward.cost) {
                    points -= reward.cost
                    pointsText.text = "Points Accumulated: $points"
                    Toast.makeText(this, "Purchased ${reward.title}!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show()
                }
            }

            rewardsContainer.addView(card)
        }

        returnButton.setOnClickListener {
            finish() // Or navigate to HomePage if needed
        }
    }
}
