import os
from flask import Flask
from dotenv import load_dotenv

from public import public
from admin import admin
from staff import staff
from api import api

load_dotenv()

app = Flask(__name__)
app.secret_key = os.environ.get("SECRET_KEY", "change-me-in-production")

# Email config — values come from .env, never hardcoded
app.config["MAIL_SERVER"] = "smtp.gmail.com"
app.config["MAIL_PORT"] = 587
app.config["MAIL_USE_TLS"] = True
app.config["MAIL_USE_SSL"] = False
app.config["MAIL_USERNAME"] = os.environ.get("MAIL_USERNAME")
app.config["MAIL_PASSWORD"] = os.environ.get("MAIL_PASSWORD")

app.register_blueprint(public)
app.register_blueprint(admin, url_prefix="/admin")
app.register_blueprint(staff, url_prefix="/staff")
app.register_blueprint(api, url_prefix="/api")

if __name__ == "__main__":
    # debug=False in production — Railway/Render set PORT automatically
    port = int(os.environ.get("PORT", 5000))
    app.run(debug=os.environ.get("FLASK_ENV") == "development", port=port, host="0.0.0.0")
