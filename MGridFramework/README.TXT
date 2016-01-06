_____________________________________________________________________________
v0.1

How does it works:
- Resource is a representation from a network resource (Android device) it waits for requests by default on port 12399
- Broker send a GridJob object to be executed
- Resource executes and replies
- 1 Connection Broker sends a list of jobs to be executed
- 1 Connection Resource sends a list of replies ordered for each job

How to usethe Middleware:
- Implement GridJobs objects to be executed
- Use method dispatchJobs from Broker to send Jobs and waits for an answer
- If there are no exceptions use getReplies to get the ordered answer list
_____________________________________________________________________________
