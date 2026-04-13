# 🌍 TripSwipe

> A swipe-based travel discovery app that makes exploring destinations simple, visual, and actually fun.

---

## ✨ Overview

TripSwipe is an Android application that helps users discover travel destinations through a swipe-based interface. Instead of searching for places manually, users can browse through destinations one at a time and decide whether they are interested by swiping.

The goal of this project is to make travel planning feel more natural and less overwhelming by focusing on exploration rather than search.

---

## 🚀 Features

- 👉 Swipe through destinations (like / skip interaction)
- ❤️ Save destinations for later
- 📂 View saved lists
- 🔍 Filter attractions by category
- 🏝️ Detailed attraction pages with images
- 🔐 User authentication (Firebase)
- ♻️ Soft delete + restore functionality
- 📸 Google Places API for real-world images

---

## 📱 App Flow

Login / Register → Main Page → Swipe Page → Results Page → Saved Lists → Attraction Details

---

## 🧩 Pages & Functionality

### 🔐 Login Page
- Firebase Authentication login
- Account creation for new users

### 🏠 Main Page
- Entry point into the app
- Displays cities or destinations

### 👉 Swipe Page
- Core interaction
- Swipe right = save
- Swipe left = skip

### 📊 Results Page
- Filter attractions
- Remove / restore items

### 📂 Saved List Page
- View saved destinations
- Manage saved items

### 🗑️ Deleted Saved Lists
- Restore previously deleted lists

### 🏝️ Attraction Detail Page
- View detailed info
- Image gallery

### 👤 Profile Page
- User info
- Logout

---

## 🏗️ Architecture

UI (Activities / Fragments) → ViewModel → Data Layer

---

## ⚙️ Tech Stack

- Kotlin
- Android SDK
- Firebase Authentication
- Google Places API

---

## 🛠️ How to Run

1. Clone repo
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator/device

---

## 🚧 Challenges

- Smooth swipe interactions
- State management
- API integration

---

## 📚 What I Learned

- UI/UX design
- ViewModel usage
- API integration

---

## 🔮 Future Improvements

- Personalized recommendations
- Map integration
- Better animations

---

## 👨‍💻 Author

Khanh Van
