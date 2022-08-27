#ifndef Implement_Workout
#define Implement_Workout
#include<iostream>
#include"Workout.h"

using namespace std;
//constructor



Workout::Workout(int w_id, string w_name, int w_price, WorkoutType w_type):id(w_id),name(w_name),price(w_price),type(w_type) {}
Workout::~Workout() {}
Workout& Workout::operator=(const Workout& other){
return *this;
}
int Workout::getId() const{
    return id;
}

std::string Workout::getName() const {
    return name;
}
int Workout::getPrice() const {
    return price;
}
WorkoutType Workout::getType() const {
    return type;
}

#endif // !Implement_Workout
