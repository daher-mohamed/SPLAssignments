#ifndef TRAINER_H_
#define TRAINER_H_

#include <vector>
#include "Customer.h"
#include "Workout.h"
typedef std::pair<int, Workout> OrderPair;
using namespace std;
class Trainer {
public:

    ///////////////////
    Trainer(int t_capacity);
    ~Trainer();
    Trainer(Trainer&& other);
    Trainer& operator=(Trainer&& other);
    void copy_Trainer(const Trainer& other);
    Trainer(const Trainer& other);
    void clear();
    Trainer& operator=(const Trainer& other);
    int getCapacity() const;
    void addCustomer(Customer* customer);
    void removeCustomer(int id);
    Customer* getCustomer(int id);
    std::vector<Customer*>& getCustomers();
    std::vector<OrderPair>& getOrders();
    void order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options);
    void openTrainer();
    void closeTrainer();
    int getSalary();
    string StringWriting();//This method I add it.
    bool isOpen();
    int NumberOfCustomers();
  int salary;
    //For Implement Rule 5:


private:
    int capacity;
    bool open;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order for the trainer - (customer_id, Workout)


};


#endif
