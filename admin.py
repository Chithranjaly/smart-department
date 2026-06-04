import os
import smtplib
from email.mime.text import MIMEText

from flask import Blueprint, render_template, request, session, redirect, url_for, flash
from database import select, insert, update, delete

admin = Blueprint("admin", __name__)

MAIL_FROM = os.environ.get("MAIL_USERNAME")
MAIL_PASS = os.environ.get("MAIL_PASSWORD")


def send_email(to_address, subject, body):
    """Send an email via Gmail SMTP. Silently logs errors."""
    try:
        with smtplib.SMTP("smtp.gmail.com", 587) as gmail:
            gmail.ehlo()
            gmail.starttls()
            gmail.login(MAIL_FROM, MAIL_PASS)
            msg = MIMEText(body)
            msg["Subject"] = subject
            msg["To"] = to_address
            msg["From"] = MAIL_FROM
            gmail.send_message(msg)
    except Exception as e:
        print(f"Email error: {e}")


@admin.route("/admin_home")
def admin_home():
    return render_template("admin_home.html")


@admin.route("/admin_manage_batches", methods=["GET", "POST"])
def admin_manage_batches():
    data = {}
    data["batches"] = select("SELECT * FROM batches")

    if "submit" in request.form:
        bname = request.form["bname"]
        desc = request.form["desc"]
        insert("INSERT INTO batches(batch_name, batch_description) VALUES(%s, %s)", (bname, desc))
        flash("ADDED")
        return redirect(url_for("admin.admin_manage_batches"))

    action = request.args.get("action")
    bid = request.args.get("bid")

    if action == "update":
        data["upbatch"] = select("SELECT * FROM batches WHERE batch_id=%s", (bid,))

    if "submits" in request.form:
        bname = request.form["bname"]
        desc = request.form["desc"]
        update("UPDATE batches SET batch_name=%s, batch_description=%s WHERE batch_id=%s", (bname, desc, bid))
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_batches"))

    if action == "delete":
        delete("DELETE FROM batches WHERE batch_id=%s", (bid,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_batches"))

    return render_template("admin_manage_batches.html", data=data)


@admin.route("/admin_manage_subjects", methods=["GET", "POST"])
def admin_manage_subjects():
    data = {}
    data["bnames"] = select("SELECT * FROM batches")
    data["sub"] = select("SELECT * FROM subjects INNER JOIN batches USING(batch_id)")

    if "submit" in request.form:
        bname = request.form["bname"]
        sname = request.form["sname"]
        insert("INSERT INTO subjects(batch_id, subject_name) VALUES(%s, %s)", (bname, sname))
        flash("ADDED")
        return redirect(url_for("admin.admin_manage_subjects"))

    action = request.args.get("action")
    ids = request.args.get("ids")

    if action == "delete":
        delete("DELETE FROM subjects WHERE subject_id=%s", (ids,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_subjects"))

    if action == "update":
        data["up_sub"] = select(
            "SELECT * FROM subjects INNER JOIN batches USING(batch_id) WHERE subject_id=%s", (ids,)
        )

    if "submitted" in request.form:
        bname = request.form["bname"]
        sname = request.form["sname"]
        update("UPDATE subjects SET batch_id=%s, subject_name=%s WHERE subject_id=%s", (bname, sname, ids))
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_subjects"))

    return render_template("admin_manage_subjects.html", data=data)


@admin.route("/admin_manage_timetable", methods=["GET", "POST"])
def admin_manage_timetable():
    data = {}
    data["batchess"] = select("SELECT * FROM batches")

    if "submit" in request.form:
        session["batch_timetable"] = request.form["bname"]
        return redirect(url_for("admin.admin_manage_timetable1"))

    return render_template("admin_manage_timetable.html", data=data)


@admin.route("/admin_manage_timetable1", methods=["GET", "POST"])
def admin_manage_timetable1():
    data = {}
    batch_id = session["batch_timetable"]
    data["subj_tt"] = select("SELECT * FROM subjects WHERE batch_id=%s", (batch_id,))
    data["batchh"] = select("SELECT * FROM batches WHERE batch_id=%s", (batch_id,))

    if "submit" in request.form:
        day = request.form["day"]
        subject = request.form["subject"]
        hour = request.form["hour"]
        insert(
            "INSERT INTO time_table(subject_id, day, session, batch_id) VALUES(%s, %s, %s, %s)",
            (subject, day, hour, batch_id),
        )
        flash("ADDED")
        return redirect(url_for("admin.admin_manage_timetable1"))

    data["tt"] = select("SELECT * FROM time_table INNER JOIN subjects USING(subject_id)")

    action = request.args.get("action")
    ids = request.args.get("ids")

    if action == "delete":
        delete("DELETE FROM time_table WHERE table_id=%s", (ids,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_timetable1"))

    if action == "update":
        data["up_tt"] = select(
            "SELECT * FROM time_table INNER JOIN subjects USING(subject_id) WHERE table_id=%s", (ids,)
        )

    if "submitte" in request.form:
        day = request.form["day"]
        subject = request.form["subject"]
        hour = request.form["hour"]
        update(
            "UPDATE time_table SET subject_id=%s, day=%s, session=%s WHERE table_id=%s",
            (subject, day, hour, ids),
        )
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_timetable1"))

    return render_template("admin_manage_timetable1.html", data=data)


@admin.route("/admin_manage_staff", methods=["GET", "POST"])
def admin_manage_staff():
    data = {}
    data["staff"] = select("SELECT * FROM staffs")
    data["batches"] = select("SELECT * FROM batches")

    if "submit" in request.form:
        uname = request.form["uname"]
        pword = request.form["pword"]
        bid = request.form["bid"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        quali = request.form["quali"]
        phone = request.form["phone"]
        email = request.form["email"]

        login_id = insert(
            "INSERT INTO login(username, password, usertype) VALUES(%s, %s, 'staff')",
            (uname, pword),
        )
        insert(
            "INSERT INTO staffs(login_id, batch_id, first_name, last_name, qualification, phone, email) VALUES(%s, %s, %s, %s, %s, %s, %s)",
            (login_id, bid, fname, lname, quali, phone, email),
        )
        flash("REGISTRATION SUCCESSFUL")
        send_email(email, "Your Login Credentials", f"Your username is {uname} and password is {pword}")
        return redirect(url_for("admin.admin_manage_staff"))

    action = request.args.get("action")
    sid = request.args.get("sid")

    if action == "update":
        data["upstaff"] = select(
            "SELECT * FROM staffs INNER JOIN batches USING(batch_id) WHERE staff_id=%s", (sid,)
        )

    if "submits" in request.form:
        bid = request.form["bid"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        quali = request.form["quali"]
        phone = request.form["phone"]
        email = request.form["email"]
        update(
            "UPDATE staffs SET batch_id=%s, first_name=%s, last_name=%s, qualification=%s, phone=%s, email=%s WHERE staff_id=%s",
            (bid, fname, lname, quali, phone, email, sid),
        )
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_staff"))

    if action == "delete":
        lid = request.args.get("lid")
        delete("DELETE FROM staffs WHERE staff_id=%s", (sid,))
        delete("DELETE FROM login WHERE login_id=%s", (lid,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_staff"))

    return render_template("admin_manage_staff.html", data=data)


@admin.route("/admin_view_students1", methods=["GET", "POST"])
def admin_view_students1():
    data = {}
    data["batch"] = select("SELECT * FROM batches")
    if "submit" in request.form:
        session["batch_id"] = request.form["bname"]
        return redirect(url_for("admin.admin_view_students"))
    return render_template("admin_view_students1.html", data=data)


@admin.route("/admin_view_students")
def admin_view_students():
    data = {}
    data["students"] = select(
        """SELECT *, CONCAT(parent.first_name,' ',parent.last_name) AS parent_name,
           students.first_name AS sfname, students.last_name AS slname
           FROM students
           INNER JOIN parent USING(parent_id)
           INNER JOIN batches USING(batch_id)
           WHERE batch_id=%s""",
        (session["batch_id"],),
    )
    return render_template("admin_view_students.html", data=data)


@admin.route("/admin_view_marklist")
def admin_view_marklist():
    sid = request.args.get("sid")
    data = {}
    data["marklist"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS student_name FROM marklist INNER JOIN students USING(student_id) WHERE student_id=%s",
        (sid,),
    )
    return render_template("admin_view_marklist.html", data=data)


@admin.route("/admin_view_attendance")
def admin_view_attendance():
    sid = request.args.get("sid")
    data = {}
    data["attendance"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS sname FROM attendance INNER JOIN students USING(student_id) WHERE student_id=%s",
        (sid,),
    )
    return render_template("admin_view_attendance.html", data=data)


@admin.route("/admin_manage_fees", methods=["GET", "POST"])
def admin_manage_fees():
    data = {}
    data["batchu"] = select("SELECT * FROM batches")

    if "submit" in request.form:
        famount = request.form["famount"]
        cname = request.form["cname"]
        ddate = request.form["ddate"]
        insert("INSERT INTO fees(fee_amount, course_name, due_date) VALUES(%s, %s, %s)", (famount, cname, ddate))
        flash("ADDED")
        return redirect(url_for("admin.admin_manage_fees"))

    data["fees"] = select("SELECT * FROM fees INNER JOIN batches ON fees.course_name=batches.batch_id")

    action = request.args.get("action")
    fid = request.args.get("fid")

    if action == "update":
        data["upfees"] = select(
            "SELECT * FROM fees INNER JOIN batches ON fees.course_name=batches.batch_id WHERE fee_id=%s", (fid,)
        )

    if "submits" in request.form:
        famount = request.form["famount"]
        cname = request.form["cname"]
        ddate = request.form["ddate"]
        update("UPDATE fees SET fee_amount=%s, course_name=%s, due_date=%s WHERE fee_id=%s", (famount, cname, ddate, fid))
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_fees"))

    if action == "delete":
        delete("DELETE FROM fees WHERE fee_id=%s", (fid,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_fees"))

    return render_template("admin_manage_fees.html", data=data)


@admin.route("/admin_view_reports")
def admin_view_reports():
    return render_template("admin_view_reports.html")


@admin.route("/admin_view_attendance_report")
def admin_view_attendance_report():
    data = {}
    data["attendance"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS student_name FROM attendance INNER JOIN students USING(student_id)"
    )
    return render_template("admin_view_attendance_report.html", data=data)


@admin.route("/admin_view_mark_report")
def admin_view_mark_report():
    data = {}
    data["marklist"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS student_name FROM marklist INNER JOIN students USING(student_id) INNER JOIN exams USING(exam_id)"
    )
    return render_template("admin_view_mark_report.html", data=data)


@admin.route("/admin_view_payments_report")
def admin_view_payments_report():
    data = {}
    data["payments"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS parent_name FROM payments INNER JOIN parent USING(parent_id) INNER JOIN fees USING(fee_id)"
    )
    return render_template("admin_view_payments_report.html", data=data)


@admin.route("/admin_view_students_report")
def admin_view_students_report():
    data = {}
    data["students"] = select("SELECT * FROM students")
    return render_template("admin_view_students_report.html", data=data)


@admin.route("/admin_manage_notification", methods=["GET", "POST"])
def admin_manage_notification():
    data = {}
    data["notification"] = select("SELECT * FROM notification")

    if "submit" in request.form:
        tname = request.form["tname"]
        desct = request.form["desct"]
        insert("INSERT INTO notification(title, description, date_time) VALUES(%s, %s, NOW())", (tname, desct))
        flash("ADDED")
        return redirect(url_for("admin.admin_manage_notification"))

    action = request.args.get("action")
    bid = request.args.get("bid")

    if action == "update":
        data["upnoti"] = select("SELECT * FROM notification WHERE notification_id=%s", (bid,))

    if "submits" in request.form:
        tname = request.form["tname"]
        desct = request.form["desct"]
        update("UPDATE notification SET title=%s, description=%s WHERE notification_id=%s", (tname, desct, bid))
        flash("UPDATED")
        return redirect(url_for("admin.admin_manage_notification"))

    if action == "delete":
        delete("DELETE FROM notification WHERE notification_id=%s", (bid,))
        flash("DELETED")
        return redirect(url_for("admin.admin_manage_notification"))

    return render_template("admin_manage_notification.html", data=data)
