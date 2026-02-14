# Meal Planner App

**Plan. Cook. Sync. – Your Personal Culinary Assistant.**

An Android application built with **MVP Architecture** designed to help users discover global recipes, manage weekly meal plans, and keep a personal collection of favorite dishes with **Cloud Sync** capabilities.

---


##  Features & Demo

### 1. User Onboarding 

A welcoming experience that guides new users through the app's core values:

* Discovering new international recipes.
* Planning weekly meals effortlessly.
* Synchronizing data with the cloud for security.

**Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/e4540f9a-0027-43bc-ad11-e509602b84af" width="200" title="Onboarding Demo">
</p>

---

### 2. Authentication & Guest Mode 

Flexible access options for every user:

* **Secure Login/Sign-up:** Full access with Firebase authentication to save and sync data.
* **Guest Mode:** Direct access to explore recipes and search features without creating an account (stored locally via Room).

**Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/0ed8c90d-0d0e-4163-b727-0d53311af7b7" width="200" title="Onboarding Demo">
</p>

---

### 3. Home Dashboard 

The hub for daily inspiration:

* **Meal of the Day:** A featured recommendation to start your day.
* **Category Explorer:** Browse meals by type (Seafood, Beef, Vegan, etc.).
* **Popular Recipes:** Trending dishes from around the world.

**Demo:**

<p align="center">
  <table>
    <tr>
      <td><img src="https://github.com/user-attachments/assets/89a38479-2aa2-4927-bfcd-7e6975f3d39f" width="250"><br><p align="center">category list</p></td>
      <td><img src="https://github.com/user-attachments/assets/3a4cdb11-42b9-498b-899e-ba2266310fde" width="250"><br><p align="center">random meal</p></td>
    </tr>
  </table>
</p>

---

### 4. Multi-Criteria Advanced Search 

Find the perfect recipe using multiple filters:

* **By Category:** Filter by food groups.
* **By Area:** Explore cuisines (Italian, Egyptian, Japanese, etc.).
* **By Ingredients:** Search based on what’s in your pantry.

 **Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/bbab1bdd-ee52-448c-b048-b139493b667c" width="200" title="Onboarding Demo">
</p>

---

### 5. Detailed Recipe View 

Everything you need to master a dish:

* **Ingredients & Measures:** Visual icons and precise quantities.
* **Step-by-Step Instructions:** A clear guide to the cooking process.
* **YouTube Player:** In-app video tutorials for visual learners.
* **Interactive Actions:** Add to **Favorites** or **Set a Meal for a Day** in your planner.

**Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/5213267e-be77-4413-8880-870c123c7bc5" width="200" title="Onboarding Demo">
</p>

---

### 6. Weekly Meal Planner 

Take control of your nutrition:

* **Calendar Integration:** Select any date to see your scheduled meals.
* **Seamless Scheduling:** Adding a meal from the details screen reflects instantly on the selected date.
* **Plan Management:** Quick delete and overwrite options to keep your schedule updated.

**Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/9bfe6330-1d42-4a8e-9662-87cdb16dc1cd" width="200" title="Onboarding Demo">
</p>


---

### 7. Favorites & Offline Storage 

Your personal digital cookbook:

* **Offline Access:** Powered by **Room Database**, access your favorite recipes anytime, anywhere.
* **Instant Management:** Save or remove recipes with a single tap.

 **Demo:**

<p align="center">
  <img src="https://github.com/user-attachments/assets/30ee0191-ef24-419b-9e9d-84736c97182f" width="300" title="Onboarding Demo">
</p>

---

### 8. Account & Data Synchronization (Overwrite) 

* **Firebase Firestore Sync:** Upload your local favorites and plans to the cloud.
* **Overwrite Policy:** Ensures your Cloud data is always a perfect mirror of your most recent local changes.
* **Profile Management:** View user info and manage sessions (Logout).

## 🛠 Tech Stack

* **Architecture:** MVP (Model-View-Presenter).
* **Networking:** Retrofit & OkHttp (TheMealDB API : [https://themealdb.com/api.php]).
* **Local Database:** Room Database (Offline-first approach).
* **Cloud Database:** Firebase Firestore.
* **Auth:** Firebase Authentication.
* **Reactive Programming:** RxJava3.
* **UI/UX:** Glide, Material Design, ConstraintLayout, Lottie Animations, YouTube Player API.

---
