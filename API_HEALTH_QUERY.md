# 🍎 Health Query API Documentation

This document describes the APIs used to query health-related data, specifically for the "Health" or "Vitals" section of the mobile application.

---

## 📈 Daily Metrics

### 1. Get Daily Metrics
- **Endpoint:** `GET /backend/health/metrics/daily`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves all health metrics for a specific date (or today if omitted).
- **Query Parameters:**
  - `date` (optional): The target date in `YYYY-MM-DD` format.
- **Response (200 OK):**
  ```json
  {
    "date": "2024-03-08",
    "identityId": 123,
    "steps": 5420,
    "heartRateAvg": 72,
    "sleepHours": 7.5,
    "waterMl": 1500,
    "exerciseMinutes": 45,
    "focusMinutes": 120,
    "caloriesConsumed": 1850,
    "caloriesBurned": 400,
    "weightKg": 70.5,
    "updatedAt": "2024-03-08T15:30:00Z"
  }
  ```
- **Response (204 No Content):** No metrics found for the specified date.

---

## 💧 Hydration

### 1. Add Hydration
- **Endpoint:** `POST /backend/health/metrics/hydration/add`
- **Auth Required:** Yes (JWT)
- **Description:** Adds a specific amount of water (ml) to the current day's total.
- **Query Parameters:**
  - `amountMl` (integer): The amount of water to add in milliliters.
- **Response (200 OK):** The updated `DailyMetricsEntity`.

---

## 🏛️ Data Models

### `DailyMetricsEntity`
| Field | Type | Description |
| :--- | :--- | :--- |
| `date` | `LocalDate` | The reference date for the metrics. |
| `identityId` | `Integer` | The external ID of the user. |
| `steps` | `Integer` | Total steps taken. |
| `heartRateAvg` | `Integer` | Average heart rate (BPM). |
| `sleepHours` | `Float` | Total hours of sleep. |
| `waterMl` | `Integer` | Total water intake in ml. |
| `exerciseMinutes`` | `Integer` | Total active exercise minutes. |
| `focusMinutes` | `Integer` | Total focus/productivity minutes. |
| `caloriesConsumed` | `Integer` | Total calories from food. |
| `caloriesBurned` | `Integer` | Estimated calories burned. |
| `weightKg` | `Double` | Body weight in kg. |
| `updatedAt` | `Instant` | Last update timestamp. |
