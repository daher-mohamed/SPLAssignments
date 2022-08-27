#ifndef ActionImplement
#define ActionImplement
#include"Action.h"
#include"Studio.h"
using namespace std;
extern Studio* backup;
///Base Action Implement
BaseAction::BaseAction():errorMsg(""),status(ERROR) {}
BaseAction::~BaseAction(){

}
ActionStatus BaseAction::getStatus() const
{
    return status;

}
void BaseAction::complete() {
    status = COMPLETED;
}
void BaseAction::error(std::string errorMsg) {

    status = ERROR;
    this->errorMsg = errorMsg;


}
std::string BaseAction::getErrorMsg() const {
    return this->errorMsg;
}

//Implement OpenTrainer---------------------------------------------------------------------------
//Using Rule of 5
OpenTrainer::OpenTrainer(const OpenTrainer& other):trainerId(other.trainerId) {
    customers=vector<Customer*>();
    this->copy(other);
}
OpenTrainer::OpenTrainer(OpenTrainer&& other):trainerId(other.trainerId){
customers=other.customers;
other.customers=vector<Customer*>();
}
void OpenTrainer::copy(const OpenTrainer& other){
    if (other.getStatus() == COMPLETED)
        complete();
    else {
        error(other.getErrorMsg());
    }
    /// <summary>
    /// Copy All customer from other to new Customers.
    /// </summary>
    /// <param name="other"></param>
    int size = other.customers.size();
    for (int i = 0; i <size; i = i + 1) {
        Customer* customer = other.customers[i];
        if (customer->toString() == "swt") {
            customers.push_back(new SweatyCustomer(customer->getName(), customer->getId()));
        }
        else  if (customer->toString() == "chp") {
            customers.push_back(new CheapCustomer(customer->getName(), customer->getId()));
        }
        else if (customer->toString() == "mcl") {
            customers.push_back(new HeavyMuscleCustomer(customer->getName(), customer->getId()));
        }
        else
            customers.push_back(new FullBodyCustomer(customer->getName(), customer->getId()));
    }
}
void OpenTrainer::clean() {

    //Delete the customers from the Heap.
    int sizeCustomer = customers.size();
   
    for (int i = sizeCustomer-1 ; i >= 0; i = i - 1) {
        delete customers[i];
        customers[i]=nullptr;
    }
    customers.clear();
}
OpenTrainer::~OpenTrainer() {
    this->clean();
}


///////////////////////////////////////
OpenTrainer::OpenTrainer(int id, std::vector<Customer*>& customersList):BaseAction() ,trainerId(id){
    for (size_t i = 0; i < customersList.size(); i++) {
        Customer* customer = customersList[i];
        customers.push_back(customer->clone());
    }
}
void OpenTrainer::act(Studio& studio) {

    if (trainerId < studio.getNumOfTrainers())//check if the trainer is exist.
    {

        Trainer* trainer = studio.getTrainer(trainerId);//take the trainer that have the specific ID.
        int capacity = trainer->getCapacity();//take the capacity of the customer
          if (!trainer->isOpen()) //There is no open session with this trainer
            {

                trainer->openTrainer();

                //Enter All the Customer to this trainer
                 size_t i;
                for (i=0; i < customers.size()&&capacity>=1;i++) {
                    trainer->addCustomer(customers[i]->clone());
                    capacity-=1;
                }
             
                for (size_t j=i; j < customers.size(); j++) {

                studio.decreaseId();
                    }
             
                complete();//call to complete to chang the status of the object.
            }
            else //There is a open session with this trainer.
            {
                  for(size_t i=0;i<customers.size();i=i+1){
                    studio.decreaseId();
                 
                }
                error("Error: Workout session does not exist or is already open");
                cout << getErrorMsg() << endl;

            }
    }

    else //The case there is No trainer exist with this Id.
    {
       for(size_t i=0;i<customers.size();i=i+1){
                    studio.decreaseId();
                 
                }

        error("Error: Workout session does not exist or is already open");
        cout << getErrorMsg() << endl;
    }
}
std::string OpenTrainer::toString() const {
    string str = "open " + to_string(trainerId);
    for (size_t i = 0; i < customers.size(); i = i + 1) {
        str = str + " " + customers[i]->getName()+","+customers[i]->toString();
    }
    if (getStatus() == COMPLETED) {
        str = str + " Completed";
    }
    else str = str + " " + getErrorMsg();
    return str;
}
  BaseAction* OpenTrainer::clone(){
     OpenTrainer* open=new OpenTrainer(*this);
     return  open;
 }
//Implement Order---------------------------------------------------------------------------
Order::Order(int id):trainerId(id) {}
void Order::act(Studio& studio) {
    if (trainerId < studio.getNumOfTrainers())//The trainer is exist
    {

        Trainer *trainer = studio.getTrainer(trainerId);

        if (trainer->isOpen()) //The trainer session is open
        {vector<Customer*> customers=trainer->getCustomers();
         for(size_t i=0;i<customers.size();i++) {
             vector<int> workout_ids = customers[i]->order(studio.getWorkoutOptions());
             trainer->order(customers[i]->getId(), workout_ids, studio.getWorkoutOptions());
             }
            //  workout_ids.clear();

            vector<Customer *> customer_list = trainer->getCustomers();

             vector<Workout> workout_list = studio.getWorkoutOptions();
            vector<OrderPair> order_list = trainer->getOrders();
            for (size_t i = 0; i < order_list.size(); i++) {
                int j = order_list[i].second.getId();

                cout << trainer->getCustomer(order_list[i].first)->getName() << " Is Doing " << workout_list[j].getName()
                     << endl;
            }
            complete();//call to complete to chang the status of the object.
        } else {
            error("Error: Trainer does not exist or is not open");
            cout << getErrorMsg() << endl;
        }
    }
    else//The trainer is not Exist.
    {
        error("Error: Trainer does not exist or is not open");
        cout << getErrorMsg() << endl;
    }
}
std::string Order::toString() const {
    string str = "order "+to_string(trainerId);
    if (getStatus() == COMPLETED) {
        str = str + " Completed";
    }
    else str = str + " " + getErrorMsg();
    return str;

}
BaseAction* Order::clone(){
     Order* open=new Order(*this);
     return open;
 }
Order::~Order(){

}


//Implement Move----------------------------------------------------------------------------------
MoveCustomer::MoveCustomer(int src, int dst, int customerId) :srcTrainer(src),dstTrainer(dst),id(customerId) {}
void MoveCustomer::act(Studio& studio) {
    if (srcTrainer < studio.getNumOfTrainers() && dstTrainer < studio.getNumOfTrainers())//The Two Trainer are exist.
    {
        Trainer* Trainer_src = studio.getTrainer(srcTrainer);
        Trainer* Trainer_dst = studio.getTrainer(dstTrainer);
        if (Trainer_src->isOpen() && Trainer_dst->isOpen())//The two trainers sessions are opens.
        {
            if (Trainer_src->getCustomer(id) != nullptr && Trainer_dst->getCapacity() > Trainer_dst->NumberOfCustomers())//There is please to add the customer to the destination trainer,The Customer is Exist.
            {
                Customer* customer = Trainer_src->getCustomer(id);

                Trainer_dst->addCustomer(customer);
                vector<int> order = customer->order(studio.getWorkoutOptions());
                Trainer_src->removeCustomer(id);
                Trainer_dst->order(id, order, studio.getWorkoutOptions());
                

                if(Trainer_src->getCustomers().size()==0){
                    Trainer_src->closeTrainer();
                }



                complete();//call to complete to chang the status of the object.
            }//The customer not exist or  we can't Add him because of the capacity.
            else
            {
                error("Error: Trainer does not exist or is not open");
                cout << getErrorMsg() << endl;
            }
        }
        else //At least on Trainer not exist.
        {
            error("Error: Trainer does not exist or is not open");
            cout << getErrorMsg() << endl;
        }


    }
    else//At least on Trainer not Exist.
    {
        error("Error: Trainer does not exist or is not open");
        cout << getErrorMsg() << endl;
    }
}
std::string MoveCustomer::toString() const {
    string str = "move "+to_string(srcTrainer) + " " + to_string(dstTrainer) + " " + to_string(id);
    if (getStatus() == COMPLETED) {
        str = str + " Completed";
    }
    else str = str + " " + getErrorMsg();
    return str;

}

 BaseAction* MoveCustomer::clone(){
     MoveCustomer* open=new MoveCustomer(*this);
     return open;
 }
MoveCustomer::~MoveCustomer()  {}
MoveCustomer::MoveCustomer(const MoveCustomer& other):srcTrainer(other.srcTrainer),dstTrainer(other.dstTrainer),id(other.id){

}

//Implememt Close---------------------------------------------------------------------------------
Close::Close(int id):trainerId(id) {}
void Close::act(Studio& studio) {
    Trainer* trainer = studio.getTrainer(trainerId);
    if(trainer!=nullptr)//The trainer is Exist.
    {
        if (trainer->isOpen())//The trainer session is open.
        {
            int Salary = trainer->getSalary();
            trainer->closeTrainer();///Close the trainer.
            cout << "Trainer " << trainerId << " closed. " << "Salary " << Salary << "NIS" << endl;
            complete();//call to complete to chang the status of the object.
        }
        else//The trainer not opn
        {
            error("Error: Trainer does not exist or is not open");
            cout << getErrorMsg() << endl;
        }
    }
    else//The trainer not exist;
    {
        error("Error: Trainer does not exist or is not open");
        cout << getErrorMsg() << endl;
    }
}
std::string Close::toString() const {
    string str= "close "+to_string(trainerId);
    if (getStatus() == COMPLETED) {
        str = str + " Completed";
    }
    else str = str + " " + getErrorMsg();
    return str;
}
 BaseAction* Close::clone(){
     Close* open=new Close(*this);
     return open;
 }
Close::~Close(){}

//Implement CloseAll---------------------------------------------------------------------------------------
CloseAll::CloseAll(){}
void CloseAll::act(Studio& studio)//I add a new method in Studio that give the trainer vector.
{
    vector<Trainer*>trainers = studio.getTrainers();
    for (size_t i = 0; i < trainers.size(); i = i + 1) {
        if (trainers[i]->isOpen()) {
            cout<<"Trainer "<<i<<" closed. "<<"Salary "<<trainers[i]->salary<<"NIS"<<endl;
        }
    }
    complete();//call to complete to chang the status of the object.
}
std::string CloseAll::toString() const {

    return "";
}
BaseAction* CloseAll::clone(){
    return new CloseAll();
}
CloseAll::~CloseAll() {}
//++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++
//Implement PrintWorkoutOptions----------------------------------------
PrintWorkoutOptions::PrintWorkoutOptions(){}
void PrintWorkoutOptions::act(Studio& studio) {
    vector<Workout> options = studio.getWorkoutOptions();
    for (size_t i = 0; i < options.size(); i = i + 1) {
        if (options[i].getType() == ANAEROBIC)
            cout << options[i].getName() << ", " << "Anaerobic" << ", " << options[i].getPrice() << endl;
        else if (options[i].getType() == CARDIO)
            cout << options[i].getName() << ", " << "Cardio" << ", " << options[i].getPrice() << endl;
        else
            cout << options[i].getName() << ", " << "Mixed" << ", " << options[i].getPrice() << endl;
    }
    complete();//call to complete to chang the status of the object.
}
std::string PrintWorkoutOptions::toString() const {
    string str="workout_options ";
    return str;
}
BaseAction* PrintWorkoutOptions::clone(){
     PrintWorkoutOptions* open=new PrintWorkoutOptions(*this);
     return open;
 }
PrintWorkoutOptions::~PrintWorkoutOptions() {}
//+++++++++++++++++++++++++++++++++++++++=
//+++++++++++++++++++++++++++++++++++++++
//Implement PrintTrainerStatus-----------------------------------------------
PrintTrainerStatus::PrintTrainerStatus(int id) :trainerId(id) {}
void PrintTrainerStatus::act(Studio& studio){
    Trainer* trainer = studio.getTrainer(trainerId);
    if (trainer != nullptr)//The trainer is Exist
    {
        if (trainer->isOpen())//Trainer is open.
        {
            cout << "Trainer " << trainerId << " status: open " << endl;
            cout << "Customers: " << endl;
            vector<Customer*> customer_list = trainer->getCustomers();
            for (size_t i = 0; i < customer_list.size(); i = i + 1) {
                cout << customer_list[i]->getId() << " " << customer_list[i]->getName() << endl;
            }
            vector<OrderPair> order_list = trainer->getOrders();
            cout << "Orders: " << endl;
            for (size_t i = 0; i < order_list.size(); i = i + 1) {

                cout << order_list[i].second.getName() << " " << order_list[i].second.getPrice() << "NIS " << order_list[i].first << endl;
            }
            cout << "current Trainer's Salary: " << trainer->getSalary() << "NIS" << endl;
            complete();
        }
        else//Trainer is not open.
        {
            cout << "Trainer " << trainerId << " status: closed" << endl;
            complete();
        }
    }
    else//The trainer not exist
    {
        error("Error: Trainer does not exist");
        cout << getErrorMsg() << endl;
    }
}
std::string PrintTrainerStatus::toString() const {
    string str= "status "+to_string(trainerId);
    if (getStatus() == COMPLETED) {
        str = str + " Completed";
    }
    else str = str + " " + getErrorMsg();
    return str;
}
 BaseAction* PrintTrainerStatus::clone(){
     PrintTrainerStatus* open=new PrintTrainerStatus(*this);
     return open;
 }
PrintTrainerStatus::~PrintTrainerStatus() {}
//Implement PrintActionLog--------------------------------------------------------------------------------------
PrintActionsLog::PrintActionsLog():BaseAction() {}
void PrintActionsLog::act(Studio& studio) {
    for (size_t i = 0; i < studio.getActionsLog().size(); i = i + 1) {
        cout << studio.getActionsLog()[i]->toString() << endl;
    }
    complete();
}
std::string PrintActionsLog::toString() const {
    return "log Completed";
}
 BaseAction* PrintActionsLog::clone(){
     PrintActionsLog* open=new PrintActionsLog(*this);
     return open;
 }
PrintActionsLog::~PrintActionsLog() {}
//Implement BackupStudio:-------------------------------------------------------------------------
BackupStudio::BackupStudio():BaseAction() {

}
void BackupStudio::act(Studio& studio) {
    if (backup == nullptr) {
        backup = new Studio(studio);
    }
    else {
        backup->operator=(studio);
    }
    complete();
}
std::string BackupStudio::toString() const {
    return "backup Completed";
}
BaseAction* BackupStudio::clone(){
     BackupStudio* open=new BackupStudio(*this);
     return open;
 }
BackupStudio::~BackupStudio()  {}
//Implement RestoreStudio:---------------------------------------------------------------------------
RestoreStudio::RestoreStudio():BaseAction() {}
void RestoreStudio::act(Studio& studio) {
    if (backup == nullptr) {
        error("Error: No backup available");
        cout << getErrorMsg() << endl;
    }
    else {
        studio.operator=(*backup);
        complete();
    }
}
std::string RestoreStudio::toString() const {
     if (getStatus() == COMPLETED) {
       return "restore Completed";
    }
    return "restore "+getErrorMsg();
}
BaseAction* RestoreStudio::clone(){
     RestoreStudio* open=new RestoreStudio(*this);
     return open;
 }
RestoreStudio::~RestoreStudio(){}
#endif // !ActionImplement
