# Smart Department — College Management System

A full-stack web application built with Python Flask and PostgreSQL for managing a college's departments, staff, students, and parents. Includes a companion Android mobile app.

![Python](https://img.shields.io/badge/Python-3.10+-blue?logo=python)
![Flask](https://img.shields.io/badge/Flask-3.0-black?logo=flask)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Android](https://img.shields.io/badge/Android-Java-green?logo=android)

🌐 **Live Demo:** [https://smart-department.onrender.com](https://smart-department.onrender.com)

> **Note:** The app is hosted on Render's free tier and may take 30–60 seconds to wake up on the first visit.

---

## Demo Credentials & Endpoints

Use these to explore the live demo without setting anything up locally.

### Admin Portal
| | |
|---|---|
| **Login URL** | [/public_login](https://smart-department.onrender.com/public_login) |
| **Username** | `admin` |
| **Password** | `admin` |

| Endpoint | Description |
|----------|-------------|
| `/admin/admin_home` | Admin dashboard |
| `/admin/admin_manage_batches` | Add, edit, delete batches |
| `/admin/admin_manage_subjects` | Manage subjects per batch |
| `/admin/admin_manage_timetable` | Build timetables |
| `/admin/admin_manage_staff` | Register and manage staff |
| `/admin/admin_manage_fees` | Set fee amounts and due dates |
| `/admin/admin_manage_notification` | Send notifications |
| `/admin/admin_view_students1` | Browse students by batch |
| `/admin/admin_view_reports` | Reports overview |
| `/admin/admin_view_attendance_report` | Full attendance report |
| `/admin/admin_view_mark_report` | Full marks report |
| `/admin/admin_view_payments_report` | Full payments report |
| `/admin/admin_view_students_report` | Full student list report |

---

### Staff Portal
| | |
|---|---|
| **Login URL** | [/public_login](https://smart-department.onrender.com/public_login) |
| **Username** | `staff` |
| **Password** | `staff` |

| Endpoint | Description |
|----------|-------------|
| `/staff/staff_home` | Staff dashboard |
| `/staff/staff_manage_parents` | Register and manage parents |
| `/staff/staff_manage_students` | Register and manage students |
| `/staff/staff_manage_attendance` | Mark and update attendance |
| `/staff/staff_scheduling_exams1` | Schedule exams |
| `/staff/staff_updating_marks` | Enter and update student marks |
| `/staff/staff_view_messages` | View and reply to student messages |
| `/staff/staff_view_notifications` | View admin notifications |

---

### Student & Parent (Android API)
Students and parents access the system via the Android companion app, which connects to the REST API below. They log in with the credentials created by staff.

| Sample credentials | Username | Password |
|---|---|---|
| Parent | `p1` | `p1` |
| Student | `s1` | `s1` |

---

### REST API Endpoints
All endpoints accept GET or POST. Parameters are passed as query strings.

| Endpoint | Parameters | Description |
|----------|------------|-------------|
| `/api/login` | `username`, `password` | Authenticate any user |
| `/api/parent_view_students` | `login_id` | Get children linked to a parent |
| `/api/parent_view_attendance` | `stid` | Get a student's attendance |
| `/api/parent_view_marklist` | `stid` | Get a student's marks |
| `/api/parent_view_exam_dates` | — | Get all exam schedules |
| `/api/parent_view_fees` | — | Get fee details |
| `/api/parent_make_payment` | `login_id`, `fee_ids`, `fee_amounts` | Record a fee payment |
| `/api/student_view_attendance` | `login_id` | Get own attendance |
| `/api/student_view_marklist` | `login_id` | Get own marks |
| `/api/student_view_exam_details` | — | Get exam schedule |
| `/api/student_view_timetable` | `login_id` | Get own timetable |
| `/api/student_view_stdy_materials` | — | Get study materials |
| `/api/student_message_staff` | `loginid`, `staff_id`, `messages` | Send a message to staff |
| `/api/student_view_message_staff` | `loginid`, `staff_id` | View message thread |

---

## Features

### Admin Portal
- Manage batches, subjects, and timetables
- Register and manage staff accounts
- View student lists, attendance reports, mark reports, and payment reports
- Send notifications to all users

### Staff Portal
- Register parents and students (auto-sends login credentials via email — requires SMTP credentials in environment variables)
- Manage and view student attendance (parents are notified by email when a student is absent — requires SMTP credentials in environment variables)
- Schedule exams and update student marks
- View and reply to messages from students

### Android App (Student & Parent)
- Students: view attendance, exam details, marks, timetable, study materials, and message staff
- Parents: view their children's attendance, marks, exam dates, fees, and make payments

### REST API
- All Android app functionality is served by a JSON API (`/api/...`)
- Replaces the original `demjson` dependency with Flask's native `jsonify`

---

## Tech Stack

| Layer       | Technology                   |
|-------------|------------------------------|
| Backend     | Python 3.10+, Flask 3.0      |
| Database    | PostgreSQL (via psycopg2)    |
| Email       | Gmail SMTP (via smtplib) — disabled on free tier     |
| Deployment  | Railway / Render (Gunicorn)  |
| Mobile      | Android (Java), Volley HTTP  |
| Frontend    | Jinja2 templates, Bootstrap  |

---

## Security Improvements (Compared to Original)

This project was originally built during my bachelor's degree. When revisiting it, I identified and fixed several security issues:

- **SQL Injection (OWASP A03:2021):** All raw string-formatted queries were replaced with parameterised queries using `psycopg2`'s `%s` placeholders.
- **Hardcoded credentials:** Gmail passwords and database credentials were removed from source code and moved to environment variables via `python-dotenv`.
- **Secret key:** Flask's `secret_key` is now loaded from the environment, never committed to Git.
- **Debug mode:** `debug=True` is now only enabled when `FLASK_ENV=development` is set.

---

## Local Setup

### Prerequisites
- Python 3.10+
- PostgreSQL running locally (or a free cloud PostgreSQL from [Railway](https://railway.app) or [Neon](https://neon.tech))

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/Chithranjaly/smart-department.git
cd smart-department

# 2. Create and activate a virtual environment
python -m venv venv
source venv/bin/activate        # Windows: venv\Scripts\activate

# 3. Install dependencies
pip install -r requirements.txt

# 4. Set up environment variables
cp .env.example .env
# Edit .env with your database URL, secret key, and Gmail app password

# 5. Create the database schema
psql $DATABASE_URL < schema.sql

# 6. Run the application
python main.py
```

Visit `http://localhost:5000`

### Environment Variables

| Variable       | Description                                         |
|----------------|-----------------------------------------------------|
| `SECRET_KEY`   | Random secret for Flask session signing             |
| `DATABASE_URL` | PostgreSQL connection string                        |
| `MAIL_USERNAME`| Gmail address used to send credential emails        |
| `MAIL_PASSWORD`| Gmail App Password (not your regular password)      |

> **Note on email in the live demo:** Render's free tier blocks outbound SMTP connections. Email notifications are fully implemented in the code but disabled on the live demo. To enable locally, configure `MAIL_USERNAME` and `MAIL_PASSWORD` in your `.env` file. In a production environment, a transactional email service such as SendGrid would be used instead.

---

## Deployment (Render)

1. Push this repo to GitHub
2. Go to [Render](https://render.com) and create a PostgreSQL database
3. Add a **PostgreSQL** plugin — Railway auto-sets `DATABASE_URL`
4. Add your other environment variables (`SECRET_KEY`, `MAIL_USERNAME`, `MAIL_PASSWORD`)
5. Deploy — Railway reads the `Procfile` and runs `gunicorn main:app`

---

## Project Structure

```
smart-department/
├── main.py              # App entry point, blueprint registration
├── database.py          # PostgreSQL connection helpers (select/insert/update/delete)
├── public.py            # Public routes: home page, login
├── admin.py             # Admin blueprint
├── staff.py             # Staff blueprint
├── api.py               # REST API for the Android app
├── templates/           # Jinja2 HTML templates
├── static/              # CSS, JS, fonts, images
├── schema.sql           # Database schema (import this to set up PostgreSQL)
├── requirements.txt
├── Procfile             # For Railway/Render deployment
├── .env.example         # Template for environment variables
└── .gitignore
```

---

## Android App

The Android app (in the `/Android` folder of the original project) connects to the Flask REST API. It supports:
- Configurable server IP/port from within the app settings
- Student and parent login flows
- All read and write operations listed in the API section above

---

## License

This project was developed as an academic final-year project. Feel free to use it as a reference.

---

## Android App Setup

The Android companion app is for **students and parents**. It connects to the live Flask API.

### Download & Install
1. Download the APK from the [`android/app-debug.apk`](android/app-debug.apk) file in this repo
2. Transfer it to your Android device
3. Enable **"Install from unknown sources"** in your device settings
4. Install the APK

### Connect to the Live Server
1. Open the app — it will show an **IP Settings** screen first
2. Enter the live server URL:
   ```
   https://smart-department.onrender.com
   ```
3. Tap **Save** and proceed to login

### Login as Student or Parent
Use the credentials created by staff via the Staff Portal. Sample credentials from the demo database:

| Role | Username | Password |
|------|----------|----------|
| Student | `s1` | `s1` |
| Parent | `p1` | `p1` |

### Features Available in the App

**Students:**
- View attendance records
- View exam schedule and marks
- View timetable
- Download study materials
- Message staff and view replies

**Parents:**
- View their children's profiles
- View attendance and marks
- View exam dates and fee details
- Make fee payments

### Source Code
The full Android source code (Java) is in the [`android/`](android/) folder of this repository.
