import os
import psycopg2
import psycopg2.extras
from dotenv import load_dotenv

load_dotenv()

DATABASE_URL = os.environ.get("DATABASE_URL")


def get_connection():
    """Create and return a new database connection."""
    return psycopg2.connect(DATABASE_URL)


def select(query, params=None):
    """Execute a SELECT query and return all rows as dicts."""
    con = get_connection()
    cur = con.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
    cur.execute(query, params or ())
    result = cur.fetchall()
    cur.close()
    con.close()
    return result


def insert(query, params=None):
    """Execute an INSERT and return the new row's id."""
    con = get_connection()
    cur = con.cursor()
    # RETURNING id is the PostgreSQL way — replaces MySQL's lastrowid
    cur.execute(query + " RETURNING id", params or ())
    row_id = cur.fetchone()[0]
    con.commit()
    cur.close()
    con.close()
    return row_id


def update(query, params=None):
    """Execute an UPDATE and return the number of affected rows."""
    con = get_connection()
    cur = con.cursor()
    cur.execute(query, params or ())
    con.commit()
    affected = cur.rowcount
    cur.close()
    con.close()
    return affected


def delete(query, params=None):
    """Execute a DELETE and return the number of affected rows."""
    con = get_connection()
    cur = con.cursor()
    cur.execute(query, params or ())
    con.commit()
    affected = cur.rowcount
    cur.close()
    con.close()
    return affected
