#ifndef ACTION_H_
#define ACTION_H_

#include <string>
#include <iostream>
#include "Customer.h"

enum ActionStatus {
    COMPLETED, ERROR
};

//Forward declaration
class Studio;

class BaseAction {
public:
    BaseAction();
    virtual ~BaseAction() ;
    ActionStatus getStatus() const;
    virtual void act(Studio& studio) = 0;
    virtual std::string toString() const = 0;
    virtual BaseAction* clone()=0;


protected:
    void complete();
    void error(std::string errorMsg);
    std::string getErrorMsg() const;
private:
    std::string errorMsg;
    ActionStatus status;
};


class OpenTrainer : public BaseAction {
public:
    OpenTrainer(int id, std::vector<Customer*>& customersList);
    ~OpenTrainer();
    void act(Studio& studio);
    std::string toString() const;
     BaseAction* clone();
    

/// <summary>
/// In this Class we have to Implement Rule of 5.
/// </summary>
///
    OpenTrainer(const OpenTrainer& other);
    OpenTrainer(OpenTrainer&& other);
    void copy(const OpenTrainer& other);
    void clean();


   

    /* OpenTrainer& operator=(const OpenTrainer& other);*/

////////////////////////////////////////////////////////////////////////////////
private:
    const int trainerId;
    std::vector<Customer*> customers;
};


class Order : public BaseAction {
public:
    Order(int id);
    ~Order();
    void act(Studio& studio);
    std::string toString() const;
         BaseAction* clone();

private:
    const int trainerId;
};


class MoveCustomer : public BaseAction {
public:
    MoveCustomer(int src, int dst, int customerId);
    ~MoveCustomer();
    void act(Studio& studio);
    std::string toString() const;
     BaseAction* clone();
    MoveCustomer( const MoveCustomer& other);
private:
    const int srcTrainer;
    const int dstTrainer;
    const int id;
};


class Close : public BaseAction {
public:
    Close(int id);
    ~Close();
    void act(Studio& studio);
    std::string toString() const;
     BaseAction* clone();
private:
    const int trainerId;
};

class CloseAll : public BaseAction {
public:
    CloseAll();
    ~CloseAll();
    void act(Studio& studio);
    std::string toString() const;
         BaseAction* clone();
    
private:
};

class PrintWorkoutOptions : public BaseAction {
public:
    PrintWorkoutOptions();
    ~PrintWorkoutOptions();
    void act(Studio& studio);
    std::string toString() const;
     BaseAction* clone();
private:
};


class PrintTrainerStatus : public BaseAction {
public:
    PrintTrainerStatus(int id);
    ~PrintTrainerStatus();
    void act(Studio& studio);
    std::string toString() const;
     BaseAction* clone();
private:
    const int trainerId;
};

class PrintActionsLog : public BaseAction {
public:
    PrintActionsLog();
    ~PrintActionsLog();
    void act(Studio& studio);
    std::string toString() const;
    BaseAction* clone();

private:
};


class BackupStudio : public BaseAction {
public:
    BackupStudio();
    ~BackupStudio();
    void act(Studio& studio);
    std::string toString() const;
    BaseAction* clone();

private:
};


class RestoreStudio : public BaseAction {
public:
    RestoreStudio();
    ~RestoreStudio();
    void act(Studio& studio);
    std::string Act_Name() const;
    std::string toString() const;
    BaseAction* clone();


};


#endif
