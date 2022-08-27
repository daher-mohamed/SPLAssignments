import atexit
import sqlite3

class Hat:
    def  __init__(self,id,topping,supplier,quantity):
        self.id=id
        self.topping=topping
        self.supplier=supplier
        self.quantity=quantity

class Supplier:
    def __init__(self,id,name):
        self.id=id
        self.name=name

class Order:
    def __init__(self,id,location,hat):
        self.id=id
        self.location=location
        self.hat=hat

#DAO
class Hats:
    def __init__(self,conn):
        self.conn=conn
    def insert(self,hat):
        self.conn.execute("""INSERT INTO hats (id,topping,supplier,quantity)  VALUES(?,?,?,?) """,[hat.id,hat.topping,hat.supplier,hat.quantity])
    def update(self,hattid):
        self.conn.execute("UPDATE hats SET quantity=quantity - 1 WHERE id={}".format(hattid))
        cursor=self.conn.cursor()
        cursor.execute("SELECT quantity FROM hats WHERE id={}".format(hattid))
        if(cursor.fetchone()[0]==0):
            self.conn.execute("DELETE FROM hats WHERE id={}".format(hattid))
    def search(self,topp):
        cursor=self.conn.cursor()
        cursor.execute("""SELECT id FROM hats WHERE topping=? ORDER BY supplier ASC""",[topp])
        one=cursor.fetchone()
        return one[0]
class Suppliers:
    def __init__(self,conn):
        self.conn=conn
    def insert(self,supplier):
        self.conn.execute(""" INSERT INTO suppliers ( id, name) VALUES(?,?)""", [supplier.id,supplier.name])

class Orders:
    def __init__(self,conn):
        self.conn=conn
    def insert(self,order):
        self.conn.execute("""INSERT INTO orders(id,location,hat) VALUES(?,?,?) """,[order.id,order.location,order.hat])
#Repository
class Repository:
    def __init__(self):
        self.conn=None
        self.hats=None
        self.suppliers=None
        self.orders=None
    def start(self,db):
        self.conn = sqlite3.connect(db)
        self.hats = Hats(self.conn)
        self.suppliers = Suppliers(self.conn)
        self.orders = Orders(self.conn)
    def close(self):
        self.conn.commit()
        self.conn.close()
    def create_tables(self):
        self.conn.executescript("""
        CREATE TABLE hats(
        id INT PRIMARY KEY, 
        topping TEXT NOT NULL,
        supplier INT REFERENCES suppliers(id),
        quantity INT NOT NULL);
        
        CREATE TABLE suppliers(
        id INT PRIMARY KEY,
        name TEXT NOT NULL);
         
        CREATE TABLE orders(
        id INT PRIMARY KEY,
        location TEXT NOT NULL,
        hat INT REFERENCES hats(id));
        """)
        
repo=Repository()
atexit.register(repo.close)
