#ifndef ImplementationStudio
#define IMplementationStudio
#include"Studio.h"
using namespace std;
#include <fstream>
#include<sstream>
#include<iostream>
using namespace std;
extern Studio* backup;
//Implement Studio:
// Studio(const std::string& configFilePath);--will implement it near.
//Defination of function
string reading_function(string& str);//reading one word and change the str.
//Is a input for a list customer for the active openTrainer.
Studio::Studio():customerID(-1),open(true) {
 workout_options=vector<Workout>();
    trainers=vector<Trainer*>();
    actionsLog=vector<BaseAction*>();
}
Studio::Studio(const std::string& configFilePath):customerID(-1) {
    workout_options=vector<Workout>();
    trainers=vector<Trainer*>();
    actionsLog=vector<BaseAction*>();
    ifstream f(configFilePath.c_str());
    string line;
    getline(f, line);
    getline(f, line);//now line content the number of the trainers in the studio.
    //This will convert from string to int.
    stringstream intValue(line);
    int number = 0;
    intValue >> number;
    ///////////////////////this is for check opentrainer implementation
    string str = "";
    getline(f, line);
    getline(f, line);
    getline(f, line);
    for (size_t i = 0; i < line.size(); i++) {
        if (line[i] == ',')
        {
            stringstream intValue(str);
            int number = 0;
            intValue >> number;
            trainers.push_back(new Trainer(number));
            str = "";

        }

        else {
            str = str + line[i];
        }
    }
    stringstream ntValue(str);
    int s = 0;
    ntValue >> s;
    trainers.push_back(new Trainer(s));

    int id = 0;

    getline(f, line);
    getline(f, line);
    getline(f, line);

    while (f) {

        int point1_place = 0;
        int   point2_place = 0;
        for (size_t i = 0; i < line.length(); i++) {
            if (line.at(i) == ',') {
                point2_place = point1_place;
                point1_place = i;

            }
        }
        if(point1_place==0&&point2_place==0)
            break;

        string str1 = line.substr(0, point2_place);
        string str2 = line.substr(point2_place + 1, point1_place - point2_place - 1);
        string str3 = line.substr(point1_place + 1, line.length() - point1_place);
        stringstream intValue(str3);
       int number = 0;
       intValue >> number;
        switch (str2[1]) {
            case 'A': {

                WorkoutType type = ANAEROBIC;
                Workout w1(id, str1, number, type);
                workout_options.push_back(w1);
                break;
            }
            case 'M': {

                WorkoutType type = MIXED;
                Workout w1(id, str1, number, type);
                workout_options.push_back(w1);
                break;
            }
            case 'C': {

                WorkoutType type = CARDIO;
                Workout w1(id, str1, number, type);
                workout_options.push_back(w1);
                break;
            }
            default: {
                break;
            }

        }

        id += 1;//increase the id.

        getline(f, line);
    }

}

///Rule 5----------------------------------------------------------------------------------------------
void Studio::copy(const Studio& other)
{
    open = other.open;
    //Copy Workout_options:
    size_t sizeWork = other.workout_options.size();
    for (size_t i = 0; i <sizeWork ; i = i + 1) {
        workout_options.push_back(other.workout_options[i]);
    }
    //copy Trainers
    size_t sizeTrainer = other.trainers.size();
    for (size_t i= 0; i < sizeTrainer; i = i + 1) {
        Trainer* t1 = new Trainer(*(other.trainers[i]));
        trainers.push_back(t1);
    }
    //copy BaseAction
    size_t sizeBaseAction = other.actionsLog.size();

    for (size_t i = 0; i < sizeBaseAction; i = i + 1) {
         actionsLog.push_back(other.actionsLog[i]->clone());
    }

}
void Studio::clean() {
    size_t size = trainers.size();
   for(size_t i=0;i<size;i=i+1){
       delete trainers[i];
       trainers[i]= nullptr;
   }
   trainers.clear();
    //DELETE workout options
    workout_options.clear();
    if(actionsLog.empty())
        return;
    size = actionsLog.size();
    //Delete BaseAction

    for (size_t i = 0; i <size; i = i +1) {
        delete actionsLog[i];//Delete th trainers.
       actionsLog[i]= nullptr;
    }
    actionsLog.clear();
}
Studio::~Studio() {
    this->clean();
}
Studio& Studio::operator=(const Studio& other) {
    if(this!=&other){
    this->clean();
    this->copy(other);
}
    return *this;
}
Studio::Studio(const Studio& other) {
trainers=vector<Trainer*>();
     workout_options=vector<Workout>();
     actionsLog=vector<BaseAction*>();

    this->copy(other);
}
Studio::Studio(Studio&& other){
  trainers=other.trainers;
    actionsLog=other.actionsLog;
   workout_options=other.workout_options;
    open=other.open;
    customerID=other.customerID;
   other.workout_options=vector<Workout>();
    other.actionsLog=vector<BaseAction*>();
    other.trainers=vector<Trainer*>();
}
Studio& Studio::operator=(Studio&& other){
    this->clean();
    trainers=other.trainers;
    actionsLog=other.actionsLog;
    workout_options=other.workout_options;
    open=other.open;
    customerID=other.customerID;
    other.workout_options=vector<Workout>();
    other.actionsLog=vector<BaseAction*>();
    other.trainers=vector<Trainer*>();
    return *this;
}

void Studio::start() {
    cout << "Studio is now open!" << endl;
    ////Case the user enter open:
    string input = "";
    while (input != "closeall") {
        getline(cin, input);
        //Know the name of the act.
        string act_Name = reading_function(input);
        vector<Customer*> customerList;
        if (act_Name == "open")//User enter open.
        {

            stringstream intValue(reading_function(input));
            int IDD = 0;
            intValue >> IDD;
            //**************here you have to check if the capacity of the trainer is bigger than the number of the customer.=>will use function capacity.
            //check if this trainer is exist and if there is no session that open.

            string c_name;
            string c_type;
            while (input != "FINISH") {

                c_name = reading_function(input);
                c_type = reading_function(input);
                if (c_type == "swt") {
                    Customer* customer = new SweatyCustomer(c_name,Customer_ID());
                    customerList.push_back(customer);
                }
                else if (c_type == "chp") {
                    Customer* customer = new CheapCustomer(c_name, Customer_ID());
                    customerList.push_back(customer);
                }
                else if (c_type == "mcl") {
                    Customer* customer = new HeavyMuscleCustomer(c_name, Customer_ID());
                    customerList.push_back(customer);
                }
                else if(c_type=="fbd") {
                    Customer* customer = new FullBodyCustomer(c_name, Customer_ID());
                    customerList.push_back(customer);
                }

            }

            OpenTrainer* open = new OpenTrainer(IDD, customerList);
            actionsLog.push_back(open);
            open->act(*this);
            int size=customerList.size();
            for(int i=size-1;i>=0;i=i-1){
                delete customerList[i];
                customerList.pop_back();
            }

        }
        ///---------------------------- The case that is order
        else if (act_Name == "order") {

            stringstream intValue(reading_function(input));
            int IDD = 0;
            intValue >> IDD;

            Order* order = new Order(IDD);
            actionsLog.push_back(order);
            order->act(*this);

        }
            ///---------------------------- The case that is move
        else if (act_Name == "move") {


            stringstream intValue(reading_function(input));
            int srcTrainer = 0;
            intValue >> srcTrainer;
            stringstream intValu(reading_function(input));
            int dstTrainer = 0;
            intValu >> dstTrainer;
            stringstream intValuet(reading_function(input));
            int customer = 0;
            intValuet >> customer;
            MoveCustomer* move = new MoveCustomer(srcTrainer, dstTrainer, customer);

            actionsLog.push_back(move);
            move->act(*this);
        }
            ///---------------------------- The case that is close
        else if (act_Name == "close") {
            stringstream intValue(reading_function(input));
            int IDD = 0;
            intValue >> IDD;
            Close* close = new Close(IDD);
            actionsLog.push_back(close);
            close->act(*this);
        }
            ///---------------------------- The case that is workout_options
        else if (act_Name == "workout_options") {
            PrintWorkoutOptions* print = new PrintWorkoutOptions();
            actionsLog.push_back(print);
            print->act(*this);
        }
            ///---------------------------- The case that is status
        else if (act_Name == "status") {
            stringstream intValue(reading_function(input));
            int IDD = 0;
            intValue >> IDD;
            PrintTrainerStatus* status = new PrintTrainerStatus(IDD);
            actionsLog.push_back(status);
            status->act(*this);
        }
            ///---------------------------- The case that is log
        else if (act_Name == "log") {
            PrintActionsLog* log = new PrintActionsLog();
            log->act(*this);
            actionsLog.push_back(log);
        }
            ///---------------------------- The case that is backup
        else if (act_Name == "backup") {
            BackupStudio* backup = new BackupStudio();
            actionsLog.push_back(backup);
            backup->act(*this);
        }
            ///---------------------------- The case that is restore
        else if (act_Name=="restore") {
            RestoreStudio* restore = new RestoreStudio();
            restore->act(*this);
            actionsLog.push_back(restore);
        }

        else if(act_Name=="closeall"){
          CloseAll* closeall=new CloseAll();
          closeall->act(*this);
          actionsLog.push_back(closeall);
          break;
      }

}

}
///--------------Finish Start
vector<Trainer*>& Studio::getTrainers() {
    return trainers;
}
int Studio::Customer_ID() {
    customerID += 1;
    return customerID;
}
void Studio::decreaseId(){
  customerID-=1;
}
int Studio::getNumOfTrainers() const {
    return trainers.size();
}
Trainer* Studio::getTrainer(int tid) {
    if ((size_t )tid < trainers.size())
        return trainers[tid];
    else return nullptr;
}
std::vector<Workout>& Studio::getWorkoutOptions() {
    return workout_options;
}

const std::vector<BaseAction*>& Studio::getActionsLog() const {
    return actionsLog;
}

//Implement Function That using:---------------------------------------------------------------------------------
string   reading_function(string& str) {
    bool reach = false;
    string value = "";
    int size = (int)str.length();
    for (int i = 0; i < size && !reach; i++) {
        if (str[i] == ' ' || str[i] == ',') {
            str = str.substr(i + 1, size - i + 1);
            reach = true;
        }
        else {
            value = value + str[i];
        }
    }
    if (!reach) {
        str = "FINISH";
    }
    return value;
}
#endif // !ImplementationStudio
