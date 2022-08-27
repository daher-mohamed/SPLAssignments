#ifndef Implement_Trainer
#define Implement_Trainer
using namespace std;
#include"Trainer.h"
#include <iostream>


//Rule 5-------------------------------------------------------------------------------------------------
Trainer::Trainer(int t_capacity): salary(0),capacity(t_capacity), open(false){
    customersList=vector<Customer*>();
    orderList=vector<OrderPair>();
}
Trainer:: ~Trainer() {

    if(customersList.empty()==1)
        return;

    this->clear();

}
Trainer::Trainer(Trainer&& other){
    capacity = other.getCapacity();
    open = other.open;
    salary=other.salary;
    customersList=other.customersList;
    other.customersList=vector<Customer*>();
    orderList=other.orderList;
    other.orderList=vector<OrderPair>();
}
Trainer& Trainer::operator=(Trainer&& other){
    clear();
    capacity = other.getCapacity();
    open = other.open;
    salary=other.salary;
    customersList=other.customersList;
    other.customersList=vector<Customer*>();
    orderList=other.orderList;
    other.orderList=vector<OrderPair>();
    return *this;
}
void Trainer::copy_Trainer(const Trainer& other) {
    capacity = other.getCapacity();
    open = other.open;
salary=other.salary;
    //copy the other trainer orderList:
    for (size_t i = 0; i < other.orderList.size(); i = i + 1) {
        orderList.push_back(other.orderList[i]);//Orderpair have No resources for this we can copy it by there value;
    }
    //copy the other trainer customerList:
    for (size_t i = 0; i < other.customersList.size(); i = i + 1) {
        Customer* customer = other.customersList[i];
        if (customer->toString() == "swt") {
            customersList.push_back(new SweatyCustomer(customer->getName(), customer->getId()));
        }
        else  if (customer->toString() == "chp") {
            customersList.push_back(new CheapCustomer(customer->getName(), customer->getId()));
        }
        else if (customer->toString() == "mcl") {
            customersList.push_back(new HeavyMuscleCustomer(customer->getName(), customer->getId()));
        }
        else
            customersList.push_back(new FullBodyCustomer(customer->getName(), customer->getId()));
    }
}
Trainer::Trainer(const Trainer & other){
    customersList=vector<Customer*>();
    orderList=vector<OrderPair>();
    this->copy_Trainer(other);
}
void Trainer::clear() {
open=false;
orderList.clear();
    //Delete the customers from the Heap.
    for (size_t i=0; i<customersList.size(); i =i + 1) {
        delete customersList[i];
    }
    customersList.clear();
}
Trainer& Trainer::operator=(const Trainer& other) {
    if(this!=&other){
    this->clear();
    this->copy_Trainer(other);
}
    return *this;
}
/////////////////////////////////
void Trainer::order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options) {
    Customer* customer = getCustomer(customer_id);
    if (customer!=nullptr) //Tne customer is exist on order
    {
        for (size_t i = 0; i < workout_ids.size(); i = i + 1) {
            Workout work = workout_options[workout_ids[i]];
            OrderPair pair(customer_id, work);
            orderList.push_back(pair);
            salary=salary+work.getPrice();
        }
    }
}
void Trainer::openTrainer() {
    open = true;
}
bool Trainer::isOpen(){
    return open;
}
int Trainer::getCapacity() const {
    return capacity;
}
void Trainer::addCustomer(Customer* customer) {
    customersList.push_back(customer);
}
void Trainer::removeCustomer(int id) {
    Customer* p = getCustomer(id);

for(size_t j=0;j<orderList.size();j++){
if(orderList[j].first==id){
salary=salary-orderList[j].second.getPrice();
}
}
    for (size_t i= 0; i < customersList.size(); i = i + 1) //Delete the adress of the customer from the vector.
    {
        if (customersList[i] == p) {
            customersList.erase(customersList.begin() + i);
        }
    }
    vector<OrderPair> orderListNew;
    for (size_t i = 0; i < orderList.size(); i = i + 1) {
        if (orderList[i].first != id)
        {
            orderListNew.push_back(orderList[i]);
        }
    }
    orderList.clear();
    size_t j = orderListNew.size();
    for (size_t i = 0; i < j; i++) {
        orderList.push_back(orderListNew[i]);

    }
}
std::vector<OrderPair>& Trainer::getOrders() {
    return orderList;
}
Customer* Trainer::getCustomer(int id) {
    for (vector<Customer*>::iterator it =customersList.begin(); it != this->customersList.end(); ++it) {
        if ((**it).getId() == id) {
            return *it;

        }
    }
    return nullptr;//if there is no customer with such id;
}
int Trainer::NumberOfCustomers() {
    return customersList.size();
}

std::vector<Customer*>& Trainer::getCustomers() {
    return customersList;
}
void Trainer::closeTrainer() {
    this->clear();
open= false;
}
int Trainer::getSalary() {
    return salary;
}

//In the bottom I add for checking the code.


string Trainer::StringWriting()
{
    string str = "";
    for (size_t i = 0; i < customersList.size(); i++) {
        str = str + "\n Customer Name: " + customersList[i]->getName() + " ID: ";
    }
    return str;
}

#endif // !Implement_Trainer
