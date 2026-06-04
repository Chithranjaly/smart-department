CREATE TABLE IF NOT EXISTS department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(111)
);

INSERT INTO department (department_id, department_name) VALUES (3, 'BCA.');
SELECT setval('department_department_id_seq', 4);

CREATE TABLE IF NOT EXISTS batches (
    batch_id SERIAL PRIMARY KEY,
    department_id INTEGER,
    batch_name VARCHAR(111),
    batch_description VARCHAR(111)
);

INSERT INTO batches (batch_id, department_id, batch_name, batch_description) VALUES (4, 3, '2017-20', '....');
SELECT setval('batches_batch_id_seq', 5);

CREATE TABLE IF NOT EXISTS login (
    login_id SERIAL PRIMARY KEY,
    username VARCHAR(111),
    password VARCHAR(111),
    usertype VARCHAR(111)
);

INSERT INTO login (login_id, username, password, usertype) VALUES
(1,  'admin',      'admin',      'admin'),
(2,  'Damon Lynn', 'Pa$$w0rd!',  'staff'),
(7,  'staff',      'staff',      'staff'),
(8,  'staff1',     'staff1',     'staff'),
(9,  'p1',         'p1',         'parent'),
(10, 'p2',         'p2',         'parent'),
(11, 's1',         's1',         'student'),
(12, 's2',         's2',         'student');
SELECT setval('login_login_id_seq', 13);

CREATE TABLE IF NOT EXISTS staffs (
    staff_id SERIAL PRIMARY KEY,
    login_id INTEGER,
    batch_id INTEGER,
    first_name VARCHAR(111),
    last_name VARCHAR(111),
    qualification VARCHAR(111),
    phone VARCHAR(111),
    email VARCHAR(111)
);

INSERT INTO staffs (staff_id, login_id, batch_id, first_name, last_name, qualification, phone, email) VALUES
(1, 2, NULL, 'aaa', 'Boris Bird', 'Necessitatibus tempo', '+1 (741) 954-3711', 'pirise@mailinator.com'),
(6, 7, NULL, 'Maya Fisher', 'Jocelyn Cervantes', 'Eligendi sequi sit', '+1 (927) 546-5107', 'fywatu@mailinator.com'),
(7, 8, 4, 'Amir Blake', 'Jermaine Guzman', 'Autem placeat ea ad', '+1 (258) 574-6752', 'kewomagy@mailinator.com');
SELECT setval('staffs_staff_id_seq', 8);

CREATE TABLE IF NOT EXISTS parent (
    parent_id SERIAL PRIMARY KEY,
    login_id INTEGER,
    relation_with_student VARCHAR(111),
    first_name VARCHAR(111),
    last_name VARCHAR(111),
    house_name VARCHAR(111),
    place VARCHAR(111),
    pincode VARCHAR(111),
    phone VARCHAR(111),
    email VARCHAR(111)
);

INSERT INTO parent (parent_id, login_id, relation_with_student, first_name, last_name, house_name, place, pincode, phone, email) VALUES
(1, 9, 'Guardian', 'p1', 'p1', 'Exercitationem quasi', 'Exercitationem quasi', 'Voluptatem voluptas', '+1 (375) 427-9142', 'nebagesu@mailinator.com'),
(2, 10, 'Brother', 'p2', 'p2', 'Sint esse esse enim', 'Sint esse esse enim', 'Nulla ut repellendus', '+1 (739) 335-6692', 'nucecatawa@mailinator.com');
SELECT setval('parent_parent_id_seq', 3);

CREATE TABLE IF NOT EXISTS students (
    student_id SERIAL PRIMARY KEY,
    login_id INTEGER,
    parent_id INTEGER,
    batch_id INTEGER,
    first_name VARCHAR(111),
    last_name VARCHAR(111),
    gender VARCHAR(111),
    dob VARCHAR(111),
    phone VARCHAR(111),
    email VARCHAR(111)
);

INSERT INTO students (student_id, login_id, parent_id, batch_id, first_name, last_name, gender, dob, phone, email) VALUES
(1, 11, 1, 4, 's1', 's1', 'female', '2002-11-02', '+1 (513) 299-4953', 'wemesiwis@mailinator.com'),
(2, 12, 2, 4, 's2', 's2', 'male', '1993-09-21', '+1 (686) 337-2817', 'wopi@mailinator.com');
SELECT setval('students_student_id_seq', 3);

CREATE TABLE IF NOT EXISTS subjects (
    subject_id SERIAL PRIMARY KEY,
    batch_id INTEGER,
    subject_name VARCHAR(222)
);

INSERT INTO subjects (subject_id, batch_id, subject_name) VALUES
(2, 4, 'science'),
(3, 4, 'maths');
SELECT setval('subjects_subject_id_seq', 4);

CREATE TABLE IF NOT EXISTS exams (
    exam_id SERIAL PRIMARY KEY,
    course_name VARCHAR(111),
    subject_name VARCHAR(111),
    exam_type VARCHAR(111),
    exam_date VARCHAR(111),
    exam_time VARCHAR(111)
);

CREATE TABLE IF NOT EXISTS fees (
    fee_id SERIAL PRIMARY KEY,
    fee_amount VARCHAR(111),
    course_name VARCHAR(111),
    due_date VARCHAR(111)
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id SERIAL PRIMARY KEY,
    fee_id INTEGER,
    parent_id INTEGER,
    payment_date VARCHAR(111),
    amount_paid VARCHAR(111)
);

CREATE TABLE IF NOT EXISTS attendance (
    att_id SERIAL PRIMARY KEY,
    student_id INTEGER,
    att_date VARCHAR(111),
    att_hour VARCHAR(111),
    att_status VARCHAR(111)
);

INSERT INTO attendance (att_id, student_id, att_date, att_hour, att_status) VALUES
(1, 1, '2007-02-22', '4th hour', 'present'),
(2, 1, '2007-02-22', '4th hour', 'present'),
(3, 1, '2010-06-06', '4th hour', 'present');
SELECT setval('attendance_att_id_seq', 4);

CREATE TABLE IF NOT EXISTS marklist (
    mark_id SERIAL PRIMARY KEY,
    exam_id INTEGER,
    student_id INTEGER,
    internal_mark VARCHAR(111),
    mark_awarded VARCHAR(111)
);

CREATE TABLE IF NOT EXISTS message (
    message_id SERIAL PRIMARY KEY,
    student_id INTEGER,
    staff_id INTEGER,
    message VARCHAR(111),
    reply VARCHAR(111),
    message_date VARCHAR(111)
);

CREATE TABLE IF NOT EXISTS notification (
    notification_id SERIAL PRIMARY KEY,
    title VARCHAR(222),
    description VARCHAR(222),
    date_time VARCHAR(222)
);

INSERT INTO notification (notification_id, title, description, date_time) VALUES (2, '1', '1', '21323');
SELECT setval('notification_notification_id_seq', 3);

CREATE TABLE IF NOT EXISTS time_table (
    table_id SERIAL PRIMARY KEY,
    subject_id INTEGER,
    day VARCHAR(222),
    session VARCHAR(222),
    batch_id INTEGER
);

CREATE TABLE IF NOT EXISTS leave_request (
    leave_id SERIAL PRIMARY KEY,
    student_id INTEGER,
    reason VARCHAR(222),
    leave_date VARCHAR(222),
    no_of_days VARCHAR(222),
    date_time VARCHAR(222),
    status VARCHAR(222)
);

CREATE TABLE IF NOT EXISTS study_material (
    material_id SERIAL PRIMARY KEY,
    title VARCHAR(111),
    material_path VARCHAR(111),
    staff_id VARCHAR(111)
);