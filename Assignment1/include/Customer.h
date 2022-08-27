#ifndef CUSTOMER_H_
#define CUSTOMER_H_

#include <vector>
#include <string>
#include "Workout.h"
typedef std::pair<int, int> Id_Praise_Pair;
class Customer {
public:
    Customer(std::string c_name, int c_id);
    virtual ~Customer() ;
    virtual std::vector<int> order(const std::vector<Workout>& workout_options) = 0;
    virtual std::string toString() const = 0;
    virtual Customer* clone()=0;
    std::string getName() const;
    int getId() const;



private:
    const std::string name;
    const int id;
};


class SweatyCustomer : public Customer {
public:
    SweatyCustomer(std::string name, int id);
    virtual ~SweatyCustomer();
    std::vector<int> order(const std::vector<Workout>& workout_options);
    std::string toString() const;
    virtual Customer* clone();
private:
};


class CheapCustomer : public Customer {
public:
    CheapCustomer(std::string name, int id);
    virtual ~CheapCustomer();
    std::vector<int> order(const std::vector<Workout>& workout_options);
    std::string toString() const;
    virtual Customer* clone();
private:
};


class HeavyMuscleCustomer : public Customer {
public:
    HeavyMuscleCustomer(std::string name, int id);
    virtual ~HeavyMuscleCustomer ();
    std::vector<int> order(const std::vector<Workout>& workout_options);
    std::string toString() const;
    virtual Customer* clone();
private:
};


class FullBodyCustomer : public Customer {
public:
    FullBodyCustomer(std::string name, int id);
    virtual ~FullBodyCustomer();
    std::vector<int> order(const std::vector<Workout>& workout_options);
    std::string toString() const;
    virtual Customer* clone();

private:
};

#endif
