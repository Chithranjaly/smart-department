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

        res = select("SELECT * FROM login WHERE username=%s AND password=%s", (uname, pword))

        if res:
            login_id = res[0]["login_id"]
            usertype = res[0]["usertype"]
            session["login_id"] = login_id

            if usertype == "admin":
                flash("WELCOME ADMIN")
                return redirect(url_for("admin.admin_home"))

            elif usertype == "staff":
                staff_res = select("SELECT * FROM staffs WHERE login_id=%s", (login_id,))
                if staff_res:
                    session["sid"] = staff_res[0]["staff_id"]
                    flash("WELCOME STAFF")
                    return redirect(url_for("staff.staff_home"))
                else:
                    flash("STAFF RECORD NOT FOUND")
                    return redirect(url_for("public.public_login"))

            elif usertype == "student":
                student_res = select("SELECT * FROM students WHERE login_id=%s", (login_id,))
                if student_res:
                    session["student_id"] = student_res[0]["student_id"]
                    flash("WELCOME STUDENT")
                    # Students use the Android app — show a message for now
                    return render_template("student_info.html", student=student_res[0])
                else:
                    flash("STUDENT RECORD NOT FOUND")
                    return redirect(url_for("public.public_login"))

            elif usertype == "parent":
                parent_res = select("SELECT * FROM parent WHERE login_id=%s", (login_id,))
                if parent_res:
                    session["parent_id"] = parent_res[0]["parent_id"]
                    flash("WELCOME")
                    # Parents use the Android app — show a message for now
                    return render_template("parent_info.html", parent=parent_res[0])
                else:
                    flash("PARENT RECORD NOT FOUND")
                    return redirect(url_for("public.public_login"))
        else:
            flash("INVALID USERNAME OR PASSWORD")
            return redirect(url_for("public.public_login"))

    return render_template("public_login.html")
