# jp_scheduler
JP Morgan work scheduler example solution


This is a JDK8 project.

The idea behind the project is to start a ResourceManager which is an implementation of a Java Executor and keep it constantly busy until all Messages are processed. 

The ResourceManager is instatiated with an internal BlockingQueue. This queue is fed with Task objects which encapsulate dummy Messages created in the Test class. 

The enquing of the Messages is done using the ResourceManager's super class execute() method from the Scheduler class. The Scheduler implements both IGateway and IScheduler interfaces. 

An Exception is thrown whenever the Scheduler tries to put another messages onto the ResourceManager queue which is already full. 

In such a case the Thread running the Scheduler will sleep for a while an retry. The Scheduler keeps SchedulerState object as an instance variable which stores some information across different calls to the gateway. 

This information is used by the Prioritiser which depending on the order the Messeges arrived in the last batch will prioritise the current batch accordingly. 

The completed() method in the Messages calls a synchronized printCompleted() method to do a simple check if the ResourceManager processed as many Messages as the Schduler submitted. This check will not work for terminated or cancelled Messages. The method prints the SchedulerState which presents the number of Messages processed sorted by their Groups. 

No 3rd libraries were used and the tests were written in Java.

*Please note the project was implemented in only few hours and although the thread synchronisation is solid and the main objective has been achieved, more work should be done to improve testing and prioritisation algorithms.*


