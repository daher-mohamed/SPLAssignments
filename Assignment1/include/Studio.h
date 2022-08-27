#ifndef STUDIO_H_
#define STUDIO_H_

#include <vector>
#include <string>
#include "Workout.h"
#include "Trainer.h"
#include "Action.h"


class Studio {
public:
    //Rule of 5-----------------------------------------------------------------------------------
    Studio();
    Studio(const std::string& configFilePath);
    void copy(const Studio& other);
    void clean();
    ~Studio();
    Studio& operator=(const Studio& other);
    Studio(const Studio& other);
    Studio(Studio&& other);
    Studio& operator=(Studio&& other);
    //////////////////////////////////////////////////////
    void start();
    int getNumOfTrainers() const;
    Trainer* getTrainer(int tid);
    vector<Trainer*>& getTrainers();
    int Customer_ID();
void decreaseId();
    const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Workout>& getWorkoutOptions();

private:
    int customerID;
    bool open;
    std::vector<Trainer*> trainers;
    std::vector<Workout> workout_options;
    std::vector<BaseAction*> actionsLog;
};

#endif
