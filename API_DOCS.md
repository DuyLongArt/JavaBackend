# 🛰️ JavaBackend API Documentation

This document provides a comprehensive reference for the APIs available in the `JavaBackend` project, specifically designed for use by the Ice Gate Flutter application.

---

## 🔐 Authentication & Account Management

### 1. User Login
- **Endpoint:** `POST /backend/auth/login`
- **Description:** Authenticates a user and returns a JWT token.
- **Request Body:**
  ```json
  {
    "userName": "string",
    "password": "string"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "message": "Login successful",
    "token": "JWT_TOKEN_STRING"
  }
  ```
- **Response (401 Unauthorized):** Invalid credentials.

### 2. User Signup
- **Endpoint:** `POST /backend/auth/signup`
- **Description:** Registers a new user in the system.
- **Request Body:**
  ```json
  {
    "userName": "string",
    "password": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string (optional)",
    "bio": "string (optional)",
    "location": "string (optional)"
  }
  ```
- **Response (200 OK):** `"User registered successfully with ID: {id}"`

### 3. Supabase Auth Sync
- **Endpoint:** `POST /backend/auth/supabase/sync`
- **Description:** Syncs a user authenticated via Supabase (OAuth) with the local database.
- **Headers:** `Authorization: Bearer <SUPABASE_JWT>`
- **Response (200 OK):**
  ```json
  {
    "message": "User synced/created successfully",
    "username": "string",
    "status": "existing/created"
  }
  ```

---

## 👤 User Profile & Person Management

### 1. Get Person Information
- **Endpoint:** `GET /backend/person/information`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves the core profile information for the authenticated user.
- **Response (200 OK):** `PersonEntity` (first_name, last_name, profile_image_url, etc.)

### 2. App Sync
- **Endpoint:** `GET /backend/person/app_sync`
- **Auth Required:** Yes (JWT)
- **Description:** Triggered by the Flutter app to sync person data.

### 3. Update Avatar
- **Endpoint:** `POST /backend/person/avatar/update`
- **Auth Required:** Yes (JWT)
- **Content-Type:** `multipart/form-data`
- **Request Param:** `file` (MultipartFile)
- **Response (200 OK):** Returns the path to the uploaded avatar in Minio.

### 4. Update Cover Image
- **Endpoint:** `POST /backend/person/cover/update`
- **Auth Required:** Yes (JWT)
- **Content-Type:** `multipart/form-data`
- **Request Param:** `file` (MultipartFile)
- **Response (200 OK):** Returns the path to the uploaded cover image.

### 5. Upload App Media
- **Endpoint:** `POST /backend/person/app/upload`
- **Auth Required:** Yes (JWT)
- **Description:** Uploads media files to a user-specific folder using their alias.
- **Request Param:** `file` (MultipartFile)
- **Response (200 OK):** Returns the storage path.

### 6. Get Extended Details
- **Endpoint:** `GET /backend/information/details`
- **Auth Required:** Yes (JWT)
- **Description:** Retrieves education, location, bio, and social links.
- **Response (200 OK):** `InformationEntity`

### 7. Edit Extended Details
- **Endpoint:** `POST /backend/information/edit`
- **Auth Required:** Yes (JWT)
- **Params:** `university`, `location`
- **Response (200 OK):** Updated `InformationEntity`

---

## 🧩 Widget Management

### 1. Get All Folders
- **Endpoint:** `GET /backend/widgets/folders`
- **Auth Required:** Yes (JWT)
- **Response (200 OK):** `List<WidgetFolderEntity>`

### 2. Get Shortcuts in Folder
- **Endpoint:** `GET /backend/widgets/folder/{folderId}/shortcuts`
- **Response (200 OK):** `List<WidgetShortcutEntity>`

### 3. Add Widget Folder
- **Endpoint:** `POST /backend/widgets/widget/folder/add`
- **Request Body:**
  ```json
  {
    "folderName": "string"
  }
  ```

---

## 📦 Object Storage (Generic)

### 1. Upload File
- **Endpoint:** `POST /backend/object/add`
- **Request Param:** `file` (MultipartFile)
- **Description:** Generic file upload service (Admin/Internal use).

---

## 🔗 Webhooks

### 1. Supabase Person Sync Webhook
- **Endpoint:** `POST /backend/webhook/supabase/sync-person`
- **Description:** Called by Supabase DB Webhooks to sync person data changes back to the JavaBackend.
- **Payload:** `SupabaseWebhookPayload`

---

## 🏛️ Entity Structures (Data Models)

### `PersonEntity`
The primary identity object returned by `/backend/person/information`.
```json
{
  "id": "integer",
  "firstName": "string",
  "lastName": "string",
  "dateOfBirth": "iso-date",
  "gender": "string",
  "phoneNumber": "string",
  "profileImageUrl": "string",
  "coverImageUrl": "string",
  "alias": "string (unique)",
  "isActive": "boolean"
}
```

### `AccountEntity`
The authentication account details.
```json
{
  "username": "string",
  "role": "USER | ADMIN | HUNTER",
  "isLocked": "boolean",
  "lastLoginAt": "iso-date"
}
```

### `InformationEntity`
Extended personal/professional data.
```json
{
  "university": "string",
  "location": "string",
  "country": "string",
  "occupation": "string",
  "educationLevel": "string",
  "bio": "string",
  "githubUrl": "string",
  "linkedinUrl": "string",
  "websiteUrl": "string"
}
```

---

## 🚧 Draft / Experimental Endpoints

### 1. IOT Approval (Draft)
- **Path:** `POST /backend/iot/{logid}/action?step={n}&status=APPROVE|REJECT`
- **Status:** Currently commented out in the backend. Intended for IOT service interaction.
