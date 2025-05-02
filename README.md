# PROG7313_POE_Part2
PROG7313 Group assignment part 2

# Money Management App

This Android application allows users to track their spending, earn points, and redeem those points for real-world rewards (e.g., discounts at NuMetro, Mr Price, Uber, and KFC). Built using **Kotlin** and **XML** in **Android Studio**, the app includes user registration, categorized expense tracking, and a dynamic rewards page.

---

## 📱 Key Features

- **User Registration & Login**
  - Secure account creation
  - Personalized experience based on saved data

- **Expense Tracking**
  - Add and categorize expenses
  - View and manage categorized data

- **Reward System**
  - Points accumulate based on user activity
  - Points redeemable for discounts from various brands
  - Visually engaging rewards page with a custom background

- **Course Guides**

- **Budget Setting**

- **Navigation**
  - Intuitive transitions between pages (e.g., Home ↔ Rewards)
  - Button-based interaction and clear layout

---

## ✅ Functional Requirements

- Users must be able to:
  - Register and log in
  - Enter and view expense data
  - See accumulated points
  - Redeem rewards based on point availability
  - Navigate between pages smoothly

---

## 🚫 Non-Functional Requirements

- **Performance:** The app must load pages within 2 seconds.
- **Usability:** Easy-to-use interface with readable fonts and accessible buttons.
- **Compatibility:** Works on Android 8.0 (API level 26) and above.
- **Security:** User data must be stored securely using Room database.
- **Maintainability:** Code is modular and follows proper Kotlin conventions.

---

## ⚙️ Installation Instructions

### Prerequisites:
- Android Studio (latest stable version recommended)
- Android SDK 26 or higher

### Steps:
1. Clone the repository or copy project files into Android Studio:
2. Open the project in Android Studio.
3. Let Gradle sync complete.
4. Build the project using:
- **Build > Rebuild Project**
5. Run the app using an emulator or physical device.

### Optional Assets:
- Ensure `background_prog.png` is present in `res/drawable`.

---

## 🌟 App Functionality (Feature Overview)

| Feature              | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| User Registration    | Allows new users to register securely and create a profile.                 |
| Home Page            | Displays navigation options and user summary.                              |
| Expense Management   | Users can add, categorize, and track expenses.                              |
| Rewards Page         | Displays point total and available rewards (NuMetro, Mr Price, Uber, KFC). |
| Reward Redemption    | Users can redeem rewards if they have enough points.                        |
| Navigation System    | Smooth screen transitions between Home, Rewards, and other pages.           |

---

## 🧱 File Structure (Core Files)

- `RewardsPage.kt` – Handles logic and display of reward content
- `activity_rewards_page.xml` – XML layout of the Rewards screen
- `HomePage.kt` – Main navigation page
- `AppDatabase.kt`, `UserDao.kt`, etc. – Local data storage using Room
- `background_prog.png` – Custom image background for Rewards UI

---

## 📌 Notes

- Points are currently static but can be made dynamic by passing data via `Intent`

---

## 🔧 Future Improvements

- Firebase Authentication and Database support
- API integration for real-time reward updates
- Dark mode and accessibility enhancements

Reece Corbett - ST10279058
Caiden Johanson - ST10377348
Kuhle Langa - ST10372352
Connor Grobler - ST
Cade Gamble - ST10262290


