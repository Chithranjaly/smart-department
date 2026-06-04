import os
import smtplib
from email.mime.text import MIMEText

from flask import Blueprint, render_template, request, session, redirect, url_for, flash
from database import select, insert, update, delete

staff = Blueprint("staff", __name__)

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


@staff.route("/staff_home")
def staff_home():
    return render_template("staff_home.html")


@staff.route("/staff_manage_parents", methods=["GET", "POST"])
def staff_manage_parents():
    data = {}
    data["parents"] = select("SELECT * FROM parent")

    if "submit" in request.form:
        uname = request.form["uname"]
        pword = request.form["pword"]
        rws = request.form["relation"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        hname = request.form["place"]
        pin = request.form["pin"]
        phone = request.form["phone"]
        email = request.form["email"]
        place = request.form["place"]

        login_id = insert(
            "INSERT INTO login(username, password, usertype) VALUES(%s, %s, 'parent')",
            (uname, pword),
        )
        insert(
            "INSERT INTO parent(login_id, relation_with_student, first_name, last_name, house_name, place, pincode, phone, email) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
            (login_id, rws, fname, lname, hname, place, pin, phone, email),
        )
        flash("ADDED")
        send_email(email, "Your Login Credentials", f"Your username is {uname} and password is {pword}")
        return redirect(url_for("staff.staff_manage_parents"))

    action = request.args.get("action")
    pid = request.args.get("pid")

    if action == "update":
        data["upparent"] = select("SELECT * FROM parent WHERE parent_id=%s", (pid,))

    if "submits" in request.form:
        rws = request.form["relation"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        hname = request.form["place"]
        pin = request.form["pin"]
        phone = request.form["phone"]
        email = request.form["email"]
        place = request.form["place"]
        update(
            "UPDATE parent SET relation_with_student=%s, first_name=%s, last_name=%s, house_name=%s, place=%s, pincode=%s, phone=%s, email=%s WHERE parent_id=%s",
            (rws, fname, lname, hname, place, pin, phone, email, pid),
        )
        flash("UPDATED")
        return redirect(url_for("staff.staff_manage_parents"))

    if action == "delete":
        lid = request.args.get("lid")
        delete("DELETE FROM login WHERE login_id=%s", (lid,))
        delete("DELETE FROM parent WHERE parent_id=%s", (pid,))
        flash("DELETED")
        return redirect(url_for("staff.staff_manage_parents"))

    return render_template("staff_manage_parents.html", data=data)


@staff.route("/staff_manage_students", methods=["GET", "POST"])
def staff_manage_students():
    data = {}
    data["parent"] = select("SELECT *, CONCAT(first_name,' ',last_name) AS parent_name FROM parent")
    data["batch"] = select("SELECT * FROM batches")
    data["students"] = select(
        """SELECT *, CONCAT(parent.first_name,' ',parent.last_name) AS parent_name,
           students.first_name AS sfname, students.last_name AS slname,
           students.phone AS sphone, students.email AS semail, students.login_id AS slid
           FROM students
           INNER JOIN parent USING(parent_id)
           INNER JOIN batches USING(batch_id)"""
    )

    if "submit" in request.form:
        uname = request.form["uname"]
        pword = request.form["pword"]
        pid = request.form["parent"]
        bid = request.form["batch"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        gender = request.form["gender"]
        dob = request.form["dob"]
        phone = request.form["phone"]
        email = request.form["email"]

        login_id = insert(
            "INSERT INTO login(username, password, usertype) VALUES(%s, %s, 'student')",
            (uname, pword),
        )
        insert(
            "INSERT INTO students(login_id, parent_id, batch_id, first_name, last_name, gender, dob, phone, email) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
            (login_id, pid, bid, fname, lname, gender, dob, phone, email),
        )
        flash("ADDED")
        send_email(email, "Your Login Credentials", f"Your username is {uname} and password is {pword}")
        return redirect(url_for("staff.staff_manage_students"))

    action = request.args.get("action")
    sid = request.args.get("sid")

    if action == "update":
        data["upstud"] = select(
            """SELECT *, CONCAT(parent.first_name,' ',parent.last_name) AS parent_name
               FROM students
               INNER JOIN parent USING(parent_id)
               INNER JOIN batches USING(batch_id)
               WHERE student_id=%s""",
            (sid,),
        )

    if "submits" in request.form:
        pid = request.form["parent"]
        bid = request.form["batch"]
        fname = request.form["fname"]
        lname = request.form["lname"]
        gender = request.form["gender"]
        dob = request.form["dob"]
        phone = request.form["phone"]
        email = request.form["email"]
        update(
            "UPDATE students SET parent_id=%s, batch_id=%s, first_name=%s, last_name=%s, gender=%s, dob=%s, phone=%s, email=%s WHERE student_id=%s",
            (pid, bid, fname, lname, gender, dob, phone, email, sid),
        )
        flash("UPDATED")
        return redirect(url_for("staff.staff_manage_students"))

    if action == "delete":
        lid = request.args.get("lid")
        delete("DELETE FROM login WHERE login_id=%s", (lid,))
        delete("DELETE FROM students WHERE student_id=%s", (sid,))
        flash("DELETED")
        return redirect(url_for("staff.staff_manage_students"))

    return render_template("staff_manage_students.html", data=data)


@staff.route("/staff_scheduling_exams1", methods=["GET", "POST"])
def staff_scheduling_exams1():
    data = {}
    data["batchess"] = select("SELECT * FROM batches")
    if "submit" in request.form:
        session["batch_exam"] = request.form["bname"]
        return redirect(url_for("staff.staff_scheduling_exams"))
    return render_template("staff_scheduling_exams1.html", data=data)


@staff.route("/staff_scheduling_exams", methods=["GET", "POST"])
def staff_scheduling_exams():
    data = {}
    batch_exam = session["batch_exam"]
    data["sub_exam"] = select("SELECT * FROM subjects WHERE batch_id=%s", (batch_exam,))

    if "submit" in request.form:
        esubject = request.form["esubject"]
        etype = request.form["etype"]
        edate = request.form["edate"]
        etime = request.form["etime"]
        insert(
            "INSERT INTO exams(course_name, subject_name, exam_type, exam_date, exam_time) VALUES(%s, %s, %s, %s, %s)",
            (batch_exam, esubject, etype, edate, etime),
        )
        flash("ADDED")
        return redirect(url_for("staff.staff_scheduling_exams"))

    data["exams"] = select(
        """SELECT * FROM exams
           INNER JOIN subjects ON exams.subject_name=subjects.subject_id
           INNER JOIN batches ON exams.course_name=batches.batch_id
           WHERE exams.course_name=%s""",
        (batch_exam,),
    )

    action = request.args.get("action")
    eid = request.args.get("eid")

    if action == "update":
        data["upexams"] = select(
            "SELECT * FROM exams INNER JOIN subjects ON exams.subject_name=subjects.subject_id WHERE exam_id=%s",
            (eid,),
        )

    if "submits" in request.form:
        esubject = request.form["esubject"]
        etype = request.form["etype"]
        edate = request.form["edate"]
        etime = request.form["etime"]
        update(
            "UPDATE exams SET subject_name=%s, exam_type=%s, exam_date=%s, exam_time=%s WHERE exam_id=%s",
            (esubject, etype, edate, etime, eid),
        )
        flash("UPDATED")
        return redirect(url_for("staff.staff_scheduling_exams"))

    if action == "delete":
        delete("DELETE FROM exams WHERE exam_id=%s", (eid,))
        flash("DELETED")
        return redirect(url_for("staff.staff_scheduling_exams"))

    return render_template("staff_scheduling_exams.html", data=data)


@staff.route("/staff_updating_marks", methods=["GET", "POST"])
def staff_updating_marks():
    data = {}
    sid = session["sid"]

    data["exam_details"] = select(
        """SELECT *, CONCAT(subjects.subject_name,' ',exams.exam_type) AS examm
           FROM exams
           INNER JOIN subjects ON exams.subject_name=subjects.subject_id
           INNER JOIN staffs ON staffs.batch_id=exams.course_name
           WHERE staffs.staff_id=%s""",
        (sid,),
    )
    data["students"] = select(
        "SELECT *, CONCAT(students.first_name,' ',students.last_name) AS student_name FROM students INNER JOIN staffs USING(batch_id) WHERE staff_id=%s",
        (sid,),
    )
    data["marks"] = select(
        """SELECT *, CONCAT(subjects.subject_name,' ',exams.exam_type) AS examm
           FROM exams
           INNER JOIN subjects ON exams.subject_name=subjects.subject_id
           INNER JOIN students ON exams.course_name=students.batch_id
           INNER JOIN marklist ON exams.exam_id=marklist.exam_id
           INNER JOIN staffs ON exams.course_name=staffs.batch_id
           WHERE staffs.staff_id=%s""",
        (sid,),
    )

    if "submit" in request.form:
        eid = request.form["exam"]
        student_id = request.form["student"]
        imark = request.form["imark"]
        mawarded = request.form["mawarded"]
        insert(
            "INSERT INTO marklist(exam_id, student_id, internal_mark, mark_awarded) VALUES(%s, %s, %s, %s)",
            (eid, student_id, imark, mawarded),
        )
        flash("ADDED")
        return redirect(url_for("staff.staff_updating_marks"))

    action = request.args.get("action")
    mid = request.args.get("mid")

    if action == "update":
        data["upmark"] = select(
            "SELECT *, CONCAT(first_name,' ',last_name) AS student_name FROM marklist INNER JOIN exams USING(exam_id) INNER JOIN students USING(student_id) WHERE mark_id=%s",
            (mid,),
        )

    if "submits" in request.form:
        eid = request.form["exam"]
        student_id = request.form["student"]
        imark = request.form["imark"]
        mawarded = request.form["mawarded"]
        update(
            "UPDATE marklist SET exam_id=%s, student_id=%s, internal_mark=%s, mark_awarded=%s WHERE mark_id=%s",
            (eid, student_id, imark, mawarded, mid),
        )
        flash("UPDATED")
        return redirect(url_for("staff.staff_updating_marks"))

    if action == "delete":
        delete("DELETE FROM marklist WHERE mark_id=%s", (mid,))
        flash("DELETED")
        return redirect(url_for("staff.staff_updating_marks"))

    return render_template("staff_updating_marks.html", data=data)


@staff.route("/staff_manage_attendance", methods=["GET", "POST"])
def staff_manage_attendance():
    data = {}
    sid = session["sid"]
    data["students"] = select(
        "SELECT *, CONCAT(students.first_name,' ',students.last_name) AS student_name FROM students INNER JOIN staffs USING(batch_id) WHERE staff_id=%s",
        (sid,),
    )

    if "submit" in request.form:
        student_id = request.form["sid"]
        adate = request.form["adate"]
        aclass = request.form["aclass"]
        astatus = request.form["astatus"]

        existing = select(
            "SELECT * FROM attendance WHERE student_id=%s AND att_date=%s AND att_hour=%s",
            (student_id, adate, aclass),
        )
        if existing:
            flash("ALREADY ADDED")
        else:
            insert(
                "INSERT INTO attendance(student_id, att_date, att_hour, att_status) VALUES(%s, %s, %s, %s)",
                (student_id, adate, aclass, astatus),
            )
            if astatus == "absent":
                absent_info = select(
                    """SELECT *, CONCAT(students.first_name,' ',students.last_name) AS sname,
                       parent.email AS pemail
                       FROM students INNER JOIN parent USING(parent_id)
                       WHERE student_id=%s""",
                    (student_id,),
                )
                if absent_info:
                    send_email(
                        absent_info[0]["pemail"],
                        "Attendance Alert",
                        f"{absent_info[0]['sname']} was absent on {adate}",
                    )
            flash("ADDED")
        return redirect(url_for("staff.staff_manage_attendance"))

    data["attendance"] = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS sname FROM attendance INNER JOIN students USING(student_id)"
    )

    action = request.args.get("action")
    aid = request.args.get("aid")

    if action == "update":
        data["upatt"] = select(
            "SELECT *, CONCAT(students.first_name,' ',students.last_name) AS s_name FROM attendance INNER JOIN students USING(student_id) WHERE att_id=%s",
            (aid,),
        )

    if "submits" in request.form:
        student_id = request.form["sid"]
        adate = request.form["adate"]
        aclass = request.form["aclass"]
        astatus = request.form["astatus"]
        update(
            "UPDATE attendance SET student_id=%s, att_date=%s, att_hour=%s, att_status=%s WHERE att_id=%s",
            (student_id, adate, aclass, astatus, aid),
        )
        flash("UPDATED")
        return redirect(url_for("staff.staff_manage_attendance"))

    if action == "delete":
        delete("DELETE FROM attendance WHERE att_id=%s", (aid,))
        flash("DELETED")
        return redirect(url_for("staff.staff_manage_attendance"))

    return render_template("staff_manage_attendance.html", data=data)


@staff.route("/staff_view_messages", methods=["GET", "POST"])
def staff_view_messages():
    data = {}
    sid = session["sid"]
    res = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS student_name FROM message INNER JOIN students USING(student_id) WHERE staff_id=%s",
        (sid,),
    )
    data["messages"] = res

    for i, row in enumerate(res, start=1):
        if f"replys{i}" in request.form:
            reply = request.form[f"reply{i}"]
            msg_id = request.form[f"ids{i}"]
            update(
                "UPDATE message SET reply=%s, message_date=CURRENT_DATE WHERE message_id=%s",
                (reply, msg_id),
            )
            flash("SUCCESS")
            return redirect(url_for("staff.staff_view_messages"))

    return render_template("staff_view_messages.html", data=data)


@staff.route("/staff_view_notifications")
def staff_view_notifications():
    data = {}
    data["noti"] = select("SELECT * FROM notification")
    return render_template("staff_view_notifications.html", data=data)
