## Authentication

1. Why do we need authentication/a login for applications such as gmail/myunsw/facebook?
To prevent other users from accessing our own recourse.
> a sentence at most

## SSO

1. What patterns can you see in the flow.
I saw state pattern. For example, if the current state is haven't login, and the input is valid credentials, then state should be switch to login successfully. Besides, I saw strategy pattern. For example, for login, there should be two strategies, user login and provider login.
## State Pattern

1. What could you represent as a state system in this example?
If the current state is haven't login, and the input is valid credentials, then state should be switch to login successfully.
2. Fill in the following state table

> Note: the cleaner way to represent the below table is through the use of a 'sub-state' (to handle locking/invalid login) but that just makes the below table even longer and we don't really talk about sub-states in enough detail in this course.  Also we could make the table a lot more compact but for your first table separating it out like this will be easier to think about.

For details on how InstaHam works check out Task 3)'s description.

```
+----------------------+----------------------+---------------------+---------------------+---------------------+-----------------+
|       Action         |   Condition          |      Home Page      |   Provider Select   |   Provider Login    |      Locked     |
+----------------------+----------------------+---------------------+---------------------+---------------------+-----------------+
| Visits App           | Cache Token Valid    |  Home (no movement) |                     |                     |                 |
|                      | Cache Token Invalid  |    Go to Locked     |                     |                     |                 |
|                      | Cache Token Not Exist|    Go to Locked     |                     |                     |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
| 'Back Button' (null) |     Used Cache       |    No page (null)   |                     |                     |                 |
| 'Back Button' (null) |                      |                     |    No page (null)   |                     |                 |
| 'Back Button' (null) |                      |                     |                     |   Provider Select   |                 |
| 'Back Button' (null) |     Used Login       |    Provider Login   |                     |                     |                 |
| 'Back Button' (null) |                      |                     |                     |                     | Provider Select |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
|   ?      ?        ?  |                      |                     |  ?       ?      ?   |                     |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
|login with email and password|Using Hoogle   |                     |                     |Home Page for Hoogle |                 |
|login with email and browser|Using InstaHam  |                     |                     |Home Page for InstaHam|                 |
|  Code from email     |  ?        ?     ?    |                     |                     |  ?       ?      ?   |                 |
|  Invalid login used  | Third time incorrect |                     |                     |    Go to Locked     |                 |
|  Correct login used  | *but* user is locked |                     |                     |     Go to Locked    |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
```

## Strategy Pattern

1. How could you use the strategy pattern in this example?
For example, for login, there should be two strategies, user login and provider login.
> Just a small sentence / word is fine here

2. Give an example of why that's useful here, and the benefits of the strategy pattern over simply using `if `statements.
If we do not use strategy pattern, we may need to modify the original code and increase the risk of having bugs.
> A very simple example, think about how the code 'grows' over-time

## Task 1) Cache

1. What did you modify to implement this?
I implemented an array of token as cache. Everytime I get a new token I append the token into cache.
> Just keep this explanation short, maybe write this before you start coding.  Try to think about what files you want to change and why.

## Task 2) Providers/Pages

1. What pattern/approach did you do to refactor `Hoogle` and providers in general.

> Again keep this short, and try to write it before you start coding.

2. What pattern/approach did you do to refactor how pages are managed / transitioned from/to.

> Again keep this short, and try to write it before you start coding.

## Task 3) Testing your design 1: InstaHam

1. What pattern/approach did you do to finish the implementation of `InstaHam` and link it up to Browser.

> Again keep this short, and try to write it before you start coding.

## Task 4) Testing your design 2: Locking

1. What approach did you take here to implement locking, did you modify an existing pattern, did you use a new one?

> Again keep this short, and try to write it before you start coding.  Don't overcomplicate this!!  If the pattern is significantly longer than the simpler version of the code it's probably not the right fit.

2. How did you change your code to handle the complex case.

> Again keep this short, and try to write it before you start coding.
