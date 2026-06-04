from flask import Blueprint, request, jsonify
from database import select, insert

api = Blueprint("api", __name__)


@api.route("/login", methods=["GET", "POST"])
def login():
    username = request.args.get("username")
    password = request.args.get("password")

    # FIXED: parameterized query
    res = select("SELECT * FROM login WHERE username=%s AND password=%s", (username, password))

    if res:
        # Convert RealDictRow to a plain dict so it's JSON-serialisable
        return jsonify(status="success", method="login", data=[dict(r) for r in res])
    return jsonify(status="failed", method="login")


@api.route("/parent_view_students", methods=["GET", "POST"])
def parent_view_students():
    login_id = request.args.get("login_id")
    res = select(
        """SELECT *, CONCAT(first_name,' ',last_name) AS st_name
           FROM students INNER JOIN batches USING(batch_id)
           WHERE parent_id=(SELECT parent_id FROM parent WHERE login_id=%s)""",
        (login_id,),
    )
    if res:
        return jsonify(status="success", method="parent_view_students", data=[dict(r) for r in res])
    return jsonify(status="failed", method="parent_view_students")


@api.route("/parent_view_exam_dates", methods=["GET", "POST"])
def parent_view_exam_dates():
    res = select("SELECT * FROM exams")
    if res:
        return jsonify(status="success", method="parent_view_exam_dates", data=[dict(r) for r in res])
    return jsonify(status="failed", method="parent_view_exam_dates")


@api.route("/parent_view_attendance", methods=["GET", "POST"])
def parent_view_attendance():
    stid = request.args.get("stid")
    res = select("SELECT * FROM attendance WHERE student_id=%s", (stid,))
    if res:
        return jsonify(status="success", method="parent_view_attendance", data=[dict(r) for r in res])
    return jsonify(status="failed", method="parent_view_attendance")


@api.route("/parent_view_marklist", methods=["GET", "POST"])
def parent_view_marklist():
    stid = request.args.get("stid")
    res = select(
        "SELECT * FROM marklist INNER JOIN exams USING(exam_id) WHERE student_id=%s", (stid,)
    )
    if res:
        return jsonify(status="success", method="parent_view_marklist", data=[dict(r) for r in res])
    return jsonify(status="failed", method="parent_view_marklist")


@api.route("/parent_view_fees", methods=["GET", "POST"])
def parent_view_fees():
    res = select("SELECT * FROM fees")
    if res:
        return jsonify(status="success", method="parent_view_fees", data=[dict(r) for r in res])
    return jsonify(status="failed", method="parent_view_fees")


@api.route("/parent_make_payment", methods=["GET", "POST"])
def parent_make_payment():
    login_id = request.args.get("login_id")
    fee_ids = request.args.get("fee_ids")
    fee_amounts = request.args.get("fee_amounts")

    row_id = insert(
        "INSERT INTO payments VALUES(DEFAULT, %s, (SELECT parent_id FROM parent WHERE login_id=%s), CURRENT_DATE, %s)",
        (fee_ids, login_id, fee_amounts),
    )
    status = "success" if row_id > 0 else "failed"
    return jsonify(status=status, method="parent_make_payment")


@api.route("/student_view_exam_details", methods=["GET", "POST"])
def student_view_exam_details():
    res = select("SELECT * FROM exams")
    if res:
        return jsonify(status="success", method="student_view_exam_details", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_exam_details")


@api.route("/student_view_attendance", methods=["GET", "POST"])
def student_view_attendance():
    login_id = request.args.get("login_id")
    res = select(
        "SELECT * FROM attendance WHERE student_id=(SELECT student_id FROM students WHERE login_id=%s)",
        (login_id,),
    )
    if res:
        return jsonify(status="success", method="student_view_attendance", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_attendance")


@api.route("/student_view_marklist", methods=["GET", "POST"])
def student_view_marklist():
    login_id = request.args.get("login_id")
    res = select(
        "SELECT * FROM marklist INNER JOIN exams USING(exam_id) WHERE student_id=(SELECT student_id FROM students WHERE login_id=%s)",
        (login_id,),
    )
    if res:
        return jsonify(status="success", method="student_view_marklist", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_marklist")


@api.route("/student_view_stdy_materials", methods=["GET", "POST"])
def student_view_stdy_materials():
    res = select(
        "SELECT *, CONCAT(first_name,' ',last_name) AS staff_name FROM study_material INNER JOIN staffs USING(staff_id)"
    )
    if res:
        return jsonify(status="success", method="student_view_stdy_materials", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_stdy_materials")


@api.route("/student_message_staff", methods=["GET", "POST"])
def student_message_staff():
    loginid = request.args.get("loginid")
    messages = request.args.get("messages")
    staff_id = request.args.get("staff_id")

    row_id = insert(
        "INSERT INTO message VALUES(DEFAULT, (SELECT student_id FROM students WHERE login_id=%s), %s, %s, 'Pending', CURRENT_DATE)",
        (loginid, staff_id, messages),
    )
    status = "success" if row_id > 0 else "failed"
    return jsonify(status=status, method="student_message_staff")


@api.route("/student_view_message_staff", methods=["GET", "POST"])
def student_view_message_staff():
    staff_id = request.args.get("staff_id")
    loginid = request.args.get("loginid")
    res = select(
        "SELECT * FROM message WHERE student_id=(SELECT student_id FROM students WHERE login_id=%s) AND staff_id=%s",
        (loginid, staff_id),
    )
    if res:
        return jsonify(status="success", method="student_view_message_staff", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_message_staff")


@api.route("/student_view_timetable", methods=["GET", "POST"])
def student_view_timetable():
    login_id = request.args.get("login_id")
    res = select(
        """SELECT * FROM time_table
           INNER JOIN subjects USING(subject_id)
           INNER JOIN students USING(batch_id)
           WHERE login_id=%s""",
        (login_id,),
    )
    if res:
        return jsonify(status="success", method="student_view_timetable", data=[dict(r) for r in res])
    return jsonify(status="failed", method="student_view_timetable")
