# 👤 Account & Profile Query API Documentation

This document describes the APIs used to query account status, personal identity, and extended profile information.

---

## 🔑 Account Status

### 1. Get Account Information
- **Endpoint:** `GET /backend/account/information`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves security-related account details for the logged-in user.
- **Response (200 OK):**
  ```json
  {
    "username": "duylong",
    "role": "USER",
    "isLocked": false,
    "failedLoginAttempts": 0,
    "lastLoginAt": "2024-03-08T08:00:00Z",
    "createdAt": "2023-12-01T10:00:00Z"
  }
  ```

---

## 🆔 Personal Identity

### 2. Get Basic Information
- **Endpoint:** `GET /backend/person/information`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves the basic profile (name, avatar, cover) of the authenticated user.
- **Response (200 OK):** `PersonEntity`

### 3. Get Profile by Alias
- **Endpoint:** `GET /backend/person/information/{alias}`
- **Auth Required:** No (Optional)
- **Description:** Retrieves public profile information for a specific user via their unique alias.

### 4. Get Contact & Bio (Archive)
- **Endpoint:** `GET /backend/person/archive`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves address, phone number, and long-form bio.
- **Response (200 OK):** `ArchiveEntity`

### 5. Get User Skills
- **Endpoint:** `GET /backend/person/skills`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves a list of skills associated with the user.
- **Response (200 OK):** `List<SkillEntity>`

---

## 📑 Extended Details

### 6. Get Extended Details
- **Endpoint:** `GET /backend/information/details`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves education, professional background, and social links.
- **Response (200 OK):** `InformationEntity`

---

## 🏛️ Data Models

### `AccountEntity` (Sanitized)
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | `String` | Unique login identifier. |
| `role` | `Enum` | `USER`, `ADMIN`, `HUNTER`. |
| `isLocked` | `Boolean` | Whether the account is currently suspended. |
| `lastLoginAt` | `Instant` | Timestamp of the last successful session. |

### `PersonEntity`
| Field | Type | Description |
| :--- | :--- | :--- |
| `firstName` | `String` | User's first name (Required). |
| `lastName` | `String` | User's last name. |
| `alias` | `String` | Unique public handle. |
| `profileImageUrl` | `String` | URL to the avatar image. |
| `coverImageUrl` | `String` | URL to the cover banner. |
| `gender` | `String` | Optional gender identifier. |

### `InformationEntity`
| Field | Type | Description |
| :--- | :--- | :--- |
| `university` | `String` | Educational institution. |
| `occupation` | `String` | Current job title/role. |
| `githubUrl` | `String` | URL to GitHub profile. |
| `websiteUrl` | `String` | Personal portfolio/link. |
