# Expense Tracker

A modern Android expense tracking application built with Kotlin and Jetpack Compose, designed to help you manage your personal finances effectively.

## Features

- **Transaction Management**: Track your income and expenses with ease
- **Category Management**: Create and organize transactions by custom categories
- **Charts & Visualization**: View your spending patterns with interactive charts
- **Persistent Storage**: All data is stored locally using Room database
- **Modern UI**: Built with Jetpack Compose for a smooth, responsive experience

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room (SQLite)
- **Charts**: Compose Charts & MPAndroidChart
- **Architecture**: MVVM with ViewModels and Repositories

## Requirements

- Android SDK 24 (Android 7.0) or higher
- Java 11 or higher

## Getting Started

### Prerequisites

- Android Studio (latest stable version)
- Android SDK 24+

### Installation

1. Clone or download the project:

    ```bash
    git clone <repository-url>
    ```

2. Open the project in Android Studio

3. Sync Gradle files:
    - Android Studio will automatically prompt to sync
    - Or manually go to `File > Sync Now`

4. Build and run:
    - Click **Run** (Shift + F10) or select your target device/emulator
    - Minimum API level: 24 (Android 7.0)

## Core Features

### Transactions

- Add income and expense transactions
- Track transactions by date and category
- View transaction history
- Organize transactions in groups

### Categories

- Create custom expense categories
- Default built-in categories
- Organize and manage transaction categories

### Charts

- Visualize spending patterns
- Interactive pie charts for expense breakdown
- View expense summaries

## Database Schema

The app uses Room with the following main entities:

- **TransactionEntity**: Stores transaction data (amount, date, category, type)
- **CategoryEntity**: Stores category information

All data is persisted locally on the device and converted between database entities and domain models through type converters.
