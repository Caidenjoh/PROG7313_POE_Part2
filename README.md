# PROG7313_POE_Part2
PROG7313 Group Assignment Part 2

# Money Management App

This Android application allows users to track their spending and enables them to create categories, add expenses to categories, set budgets for each category, earn points, and redeem those points for real-world rewards (e.g., discounts). Built using **Kotlin** and **XML** in **Android Studio**.

---
## Video of App on YouTube
https://youtu.be/CC4oi0tNE_U
---

## Key Features

- **User Registration & Login**
  - Secure account creation
  - Personalized experience based on saved data

- **Categories**
  - Users can create categories of their choice.
  - View details for each category (showing the expenses) and set budgets for each category.

- **Expense Tracking**
  - Add and categorize expenses.
  - View and manage the expense data.
  - The expenses can be filters with the user selectable date filter.

- **Reward System**
  - Points accumulate based on user activity.
  - Points redeemable for discounts from various brands.
  - Visually engaging rewards page with a custom background.

- **Course Guides**
  - Provides resources (links) for users to go read.
  - Educates them on the best ways to budget.

- **Budget Setting**
  - Users can set their min and max monthly spending limit goals for each category.
  - Minimum goal is how much the user would ideally stay under.
  - Maximum goal is the max they would not want to exceed.
  - A comment is provided to users that informs them if they are withing the min and max goals for the month.

- **Navigation**
  - Intuitive transitions between pages (e.g., Home or Rewards).
  - Button-based interaction and clear layout.
  - Users can also navigate the app using the navigation bar.

- **Room DB**
  - Saves all of the user data (Users, Categories, Expenses, Category Budgets) to the database.
  - The data can be viewed using the app inspection when the app is running and the database is open.

---

## Functional Requirements

- Users must be able to:
  - Register and log in
  - Make categories, view details and set budget for each category
  - View details displays all the expenses inside the category with the total spent at the top of the screen. There is also a date filter where users can select 2 dates and then display expenses within the range. Each expense has a view     image button and a delete button. The view image button displays the photo for the expense if the user added an image for it when doing the expense entry. The delete button will prompt the suer to delete the expense entry. 
  - The set budget allows users to set their minimum budget goal and their maximum budget goal
  - See accumulated points to redeem rewards
  - Redeem rewards based on point availability
  - Navigate to the free course guides page which gives users educational websites.
  - View a graph on the home screen that shows overall budget left.
  - Recent transactions can be seen on the home screen at the bottom.
  - Text can be seen at the top of each users home screen which informs the user if one of their categories is close to the maximum budget set.
  - A navigation bar that users can interact with at the top left of the screen and can simply go to different pages.
  - Navigate between pages smoothly

---

## Non-Functional Requirements

- **Performance:** The app must load pages within 2 seconds.
- **Usability:** Easy-to-use interface with readable fonts and accessible buttons.
- **Compatibility:** Works on Android 8.0 (API level 26) and above.
- **Security:** User data must be stored securely using Room database.
- **Maintainability:** Code is modular and follows proper Kotlin conventions.

---

## Installation Instructions

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


## App Functionality (Feature Overview)

| Feature              | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| User Registration    | Allows new users to register securely and create a profile.                 |
| Home Page            | Displays navigation options.                              		               |
| Expense Management   | Users can add, categorize, and track expenses.                              |
| Rewards Page         | Displays point total and available rewards (NuMetro, Mr Price, Uber, KFC).  |
| Reward Redemption    | Users can redeem rewards if they have enough points.                        |
| Navigation System    | Smooth screen transitions between Home, Rewards, and other pages.           |
| Category Creation    | Users can create their own categories.          			                       |
| Free Course Guides   | Users can view a course guides page which provides links.     		           |


---

## Notes

- The first time the emulator runs it can take a while
- Sometimes the first run on a new device takes longer than 5 minutes so it might timeout the app when starting up, but if you run it again after that it should run the app perfectly fine.
- Points are currently static but can be made dynamic in the future by passing data via `Intent`
- The view graph page currently is not implemented because the POE only says it needs it for Part 3. The other graph on the home screen however is functional.
- We used Pixel 8a API 28 for our emulator phone

---

## Future Improvements

- Firebase Authentication and Database support
- API integration for real-time reward updates
- Dark mode and accessibility enhancements

---

## References
- PhilJay. (2020). MPAndroidChart – A powerful chart library for Android. [online] GitHub. Available at: https://github.com/PhilJay/MPAndroidChart [Accessed 30 April 2025].

Caiden Johanson - ST10377348
Cade Gamble - ST10262209
Connor Grobler - ST10323212
Kuhle Langa - ST10372352
Reece Corbett - ST10279058
