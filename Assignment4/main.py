import sqlite3
import sys
import persistence
from persistence import repo

output_list=[]
def in_output(topping,supplier,location):
  output_list.append(topping + ","+ supplier + ","+location + "\n" )

def make_file(f):
    file = open(f, "w")
    for line in output_list:
       file.write(line)
    file.close()

def make_delivered(args,count):
        hatid = repo.hats.search(args[1])
        order = repo.orders.insert(persistence.Order(count,args[0],hatid))
        s = "SELECT suppliers.name FROM hats JOIN suppliers ON hats.supplier=suppliers.id WHERE hats.id={}".format(hatid)
        all = repo.conn.cursor().execute(s).fetchone()
        #repo.orders.insert(order)
        in_output(args[1],all[0],args[0])
        repo.hats.update(hatid)

def orders(argss):
   inputfilename = argss[2]
   inputfile = open(inputfilename,'r')
   count = 1
   for line in inputfile:
       args=line.replace('\n', '').split(',')
       make_delivered(args,count)
       count+=1
   make_file(argss[3])

def config(args):
    input_file_name = args[1]
    with open(input_file_name) as input_file:
        first_line = input_file.readline().replace('\n', '').split(',')
        number = int(first_line[0])
        while number > 0:
            line = input_file.readline().replace('\n', '').split(',')
            repo.hats.insert(persistence.Hat(int(line[0]), line[1], int(line[2]), int(line[3])))
            number = number-1
        number = int(first_line[1])
        while number > 0:
            line = input_file.readline().replace('\n', '').split(',')
            repo.suppliers.insert(persistence.Supplier(int(line[0]), line[1]))
            number = number-1

if __name__ == '__main__':
    repo.start(sys.argv[4])
    repo.create_tables()
    config(sys.argv)
    orders(sys.argv)
