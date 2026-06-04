from flask import Blueprint, render_template, request, session, redirect, url_for, flash
from database import select

public = Blueprint("public", __name__)


@public.route("/")
def public_home():
    return render_template("public_home.html")


@public.route("/public_login", methods=["GET", "POST"])
def public_login():
    if request.method == "POST" and "submit" in request.form:
        uname = request.form["uname"]
        pword = request.form["pword"]

        # FIXED: parameterized query — no more SQL injection
        res = select("SELECT * FROM login WHERE username=%s AND password=%s", (uname, pword))

        if res:
            login_id = res[0]["login_id"]
            session["login_id"] = login_id

            if res[0]["usertype"] == "admin":
                flash("WELCOME ADMIN")
                return redirect(url_for("admin.admin_home"))

            if res[0]["usertype"] == "staff":
                staff_res = select("SELECT * FROM staffs WHERE login_id=%s", (login_id,))
                if staff_res:
                    session["sid"] = staff_res[0]["staff_id"]
                    flash("WELCOME STAFF")
                    return redirect(url_for("staff.staff_home"))
        else:
            flash("INVALID USERNAME OR PASSWORD")
            return redirect(url_for("public.public_login"))

    return render_template("public_login.html")
