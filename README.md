# Smart Department — College Management System

A full-stack web application built with Python Flask and PostgreSQL for managing a college's departments, staff, students, and parents. Includes a companion Android mobile app.

![Python](https://img.shields.io/badge/Python-3.10+-blue?logo=python)
![Flask](https://img.shields.io/badge/Flask-3.0-black?logo=flask)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Android](https://img.shields.io/badge/Android-Java-green?logo=android)

---

## Features

### Admin Portal
- Manage batches, subjects, and timetables
- Register and manage staff accounts
- View student lists, attendance reports, mark reports, and payment reports
- Send notifications to all users

### Staff Portal
- Register parents and students (auto-sends login credentials via email)
- Manage and view student attendance (parents are notified by email when a student is absent)
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
| Email       | Gmail SMTP (via smtplib)     |
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
git clone https://github.com/YOUR_USERNAME/smart-department.git
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

---

## Deployment (Railway)

1. Push this repo to GitHub
2. Create a new project on [Railway](https://railway.app)
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
