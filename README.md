 user-friendly mobile application designed to help individuals effortlessly track their income and expenses, gain insights into their spending habits, and manage their personal finances more effectively. Whether you're saving for a goal, sticking to a budget, or just want to understand where your money goes, this app provides the tools you need in a clean and intuitive interface.




📘 Android Finance App - Full Architecture Overview

---

### ✅ 1. Entities (Room Database)

Entities represent your database tables.

#### 🔹 User

Represents each user of the app.

* Fields: id, name, email, password
* Used during login, signup, and displaying user info.

#### 🔹 Transaction

Represents a financial record.

* Fields: id, userId, type ("income" or "expense"), amount, category, note, date (timestamp)
* Used across charts, records list, and reports.

---

### ✅ 2. DAO (Data Access Object)

Defines SQL queries and methods.

#### 🔹 UserDao

* getUserById(id)
* getUserByEmailAndPassword(email, pass)
* insert(user)

#### 🔹 TransactionDao

* getAllForUser(userId)
* insert(transaction)
* update(transaction)
* delete(transaction)

---

### ✅ 3. Repository

Acts as the middle layer between DAOs and ViewModels.

#### 🔹 UserRepository

* Manages login/signup operations with UserDao

#### 🔹 TransactionRepository

* Manages all transaction operations (insert, delete, update)
* Uses Dispatchers.IO for background safety

---

### ✅ 4. ViewModel

Bridges data (repositories) to the UI.

#### 🔹 UserViewModel

* Uses UserRepository
* Supplies login/signup info to Profile or Records

#### 🔹 TransactionViewModel

* Uses TransactionRepository
* Feeds:

  * RecordsFragment
  * ChartsFragment
  * ReportsFragment

---

### ✅ 5. Fragments (UI Layer)

#### 🔹 RecordsFragment

* Lists all transactions in a RecyclerView
* Shows: Total Income, Expense, Balance
* Filters by month using DatePicker
* Supports deleting via long-press

#### 🔹 ChartsFragment

* Shows two charts using MPAndroidChart:

  * Pie chart for this month (Income vs Expense)
  * Bar chart (monthly grouped income and expense)

#### 🔹 ReportsFragment

* Lists total income/expense per category
* Designed for clean analytics view

#### 🔹 ProfileFragment

* Displays user name/email
* Logout button (clears SharedPreferences)

---

### ✅ 6. Adapters

#### 🔹 TransactionAdapter

* Displays each transaction in a CardView
* Supports long-click delete

#### 🔹 ReportAdapter

* Displays each category and its totals

---

### ✅ 7. SharedPreferences

* Stores logged-in userId
* Allows fragments to fetch current user context

---

### ✅ 8. RoomDatabase

* Singleton pattern
* Exposes DAOs via AppDatabase.getDatabase(context)

---

### ✅ 9. Navigation

* Bottom Navigation links:

  * Records
  * Charts
  * Reports
  * Profile

---

### ✅ 📊 Example Data Flow:

1. User opens app → SharedPref gives userId
2. TransactionViewModel.getAll(userId) fetches data
3. Fragment observes LiveData → updates RecyclerView, totals, charts, or reports

---
