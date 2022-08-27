#ifndef  Customer_Implement
#define Customer_Implement
#include"Customer.h"
#include "Workout.h"
#include<iostream>
#include<algorithm>

using namespace std;

//constructure
Customer::Customer(std::string c_name, int c_id):name(c_name),id(c_id) {}
Customer::~Customer() {}
std::string Customer::getName() const {
    return name;
}
int Customer::getId() const {
    return id;
}



//------------------------------------------------------------------
//Sweaty_Customer:

SweatyCustomer::SweatyCustomer(std::string name, int id) :Customer(name, id){}
SweatyCustomer::~SweatyCustomer() {}

std::vector<int> SweatyCustomer::order(const std::vector<Workout>& workout_options) {
    vector<int> swt;
    for (size_t i = 0; i < workout_options.size(); i++) {
        if (workout_options[i].getType() == CARDIO) //***********************Check it
        {
            swt.push_back(workout_options[i].getId());
        }
    }

    return swt;
}
std::string SweatyCustomer::toString() const {
    string str ="swt";
    return str;
}
Customer* SweatyCustomer::clone(){
    return new SweatyCustomer(this->getName(),this->getId());
}


//-------------------------------------------------------------------
//Cheap_Customer:
//constructor
CheapCustomer::CheapCustomer(std::string name, int id) :Customer(name, id) {}
CheapCustomer::~CheapCustomer() {}


std::vector<int> CheapCustomer::order(const std::vector<Workout>& workout_options) {
    vector<int> cheap;
    int min = workout_options[0].getPrice();
    int ID = workout_options[0].getId();
    for (size_t i = 1; i < workout_options.size(); i += 1) {
        if (workout_options[i].getPrice() < min) {
            min = workout_options[i].getPrice();
            ID = workout_options[i].getId();

        }
    }
    cheap.push_back(ID);
    return cheap;
}
std::string CheapCustomer::toString() const {
    string str ="chp";
    return str;

}
Customer* CheapCustomer::clone(){
    return new CheapCustomer(this->getName(),this->getId());
}


//-------------------------------------------------------------------
//HeavyMuscleCustomer:
//constructor
HeavyMuscleCustomer::HeavyMuscleCustomer(std::string name, int id) :Customer(name, id) {}
HeavyMuscleCustomer::~HeavyMuscleCustomer(){}


std::vector<int> HeavyMuscleCustomer::order(const std::vector<Workout>& workout_options) {

    vector<int> heavey;
    int count = 0;
    for (size_t i = 0; i < workout_options.size(); i++) {
        if (workout_options[i].getType() == ANAEROBIC) {
            count++;
        }
    }
    int arr[count];
    int temp;
    int place = 0;
    for (size_t i = 0; i < workout_options.size(); i++) {
        if (workout_options[i].getType() == ANAEROBIC) {
            arr[place] = workout_options[i].getId();
            temp = workout_options[i].getPrice();
            int j = place;
            int index = place - 1;
            while (index >= 0) {
                if (temp < workout_options[arr[index]].getPrice()) {
                    arr[j] = workout_options[arr[index]].getId();
                    arr[index] = workout_options[i].getId();
                    index--;
                    j--;
                }
                else
                    break;
            }

            place++;
        }
    }
    for (int i =count-1; i >=0; i--) {
        heavey.push_back( workout_options[arr[i]].getId());
    }
    return heavey;
}
std::string HeavyMuscleCustomer::toString() const {
    string str = "mcl";
    return str;
}
Customer* HeavyMuscleCustomer::clone(){
    return new HeavyMuscleCustomer(this->getName(),this->getId());
}

//------------------------------------------------------------------------------------------
//Full_Body_Customer:
//Constructor:
FullBodyCustomer::FullBodyCustomer(std::string name, int id) :Customer(name, id){}
FullBodyCustomer::~FullBodyCustomer() {}
std::vector<int> FullBodyCustomer::order(const std::vector<Workout>& workout_options) {


    vector<int> full;
    int minCardio = -1;
    int CardioId=0;
    int	max_mix = -1;
    int mixId = 0;
    int minAnaerobic = -1;
    int AnaID;
    for (size_t  i = 0; i < workout_options.size(); i++) {
        if (workout_options[i].getType() == CARDIO) {
            if (workout_options[i].getPrice() < minCardio||minCardio==-1) {
                minCardio = workout_options[i].getPrice();
                CardioId = workout_options[i].getId();
            }
        }
        if (workout_options[i].getType() == MIXED) {
            if (workout_options[i].getPrice() > max_mix) {
                max_mix = workout_options[i].getPrice();
                mixId = workout_options[i].getId();

            }
        }
        if (workout_options[i].getType() == ANAEROBIC) {
            if (workout_options[i].getPrice() < minAnaerobic||minAnaerobic==-1) {
                minAnaerobic = workout_options[i].getPrice();
                AnaID = workout_options[i].getId();
            }
        }
    }
    if (minCardio != -1 && max_mix != -1 && minAnaerobic != -1) {
        full.push_back(CardioId);
        full.push_back(mixId);
        full.push_back(AnaID);
        return full;
    }
    return full;

}
std::string FullBodyCustomer::toString() const {
    string str = "fbd";
    return str;
}
Customer* FullBodyCustomer::clone(){
    return new FullBodyCustomer(this->getName(),this->getId());
}


#endif // ! Customer_Implement