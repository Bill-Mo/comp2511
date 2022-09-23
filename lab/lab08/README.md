# Lab 08

### Due: Week 9 Monday, 5pm

### Value: 2 marks towards the Class Mark

## Aims

- Understand and apply the Decorator Patterns
- Learn about a complex problem by breaking it down into steps
- Utilise generic programming to generalise data structures
- Write thread safe code by understanding a simple concurrency exmaple

## Setup

**REMEMBER** to replace the zID below with your own.

```
git clone gitlab@gitlab.cse.unsw.EDU.AU:COMP2511/21T3/students/z555555/lab08.git
```

## Introduction

This lab attempts to bring you through the thought process behind query styled language extensions (i.e. Streams in Java/LINQ in C#), and their relevance to modern technologies such as SQL.

The attempt is for this lab to be more educational than _hard_, so don't overcomplicate each of the sections.

### Query Languages

You've already dealt with a query language in this course. Streams!  An example is below that does the following;

1. Defines a user with 3 properties; isActive, userId, jobTitle.
2. Defines a some users
3. Then creates a stream that groups each active person by their job then prints out the job statistics
    - i.e. `{Business Analysts=1, CEO=1, Devs=2}` is the example output
    - Filter you would have seen previously, and is simply just removing all inactive users
    - Sorting by the group id just makes it so the jobs appear in alphabetical order
    - GroupingBy is probably a new thing for you, it allows you to change how your data is structured and in this case creates a mapping between each job and the count of each person
        - The first argument is what you are grouping by
        - The second argument in this case means it's going to use a LinkedHashMap to create the mapping structure.  This means it preserves the sorting order that was specified above.
        - The last argument means the value for each record will be the count.

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class User {
    private boolean isActive;
    private String userId;
    private String jobTitle;

    public User(boolean isActive, String userId, String jobTitle) {
        this.isActive = isActive;
        this.userId = userId;
        this.jobTitle = jobTitle;
    }

    public boolean isActive() {
        return isActive;
    }

    public String userId() {
        return userId;
    }

    public String jobTitle() {
        return jobTitle;
    }

    @Override
    public String toString() {
        return userId;
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        // {Business Analysts=1, CEO=1, Devs=2}
        System.out.println(
            users.stream()
            .filter(x -> x.isActive())
            .sorted(Comparator.comparing(User::groupId))
            .collect(Collectors.groupingBy(User::jobTitle, () -> new LinkedHashMap(), Collectors.counting())));
    }
}
```

This looks eerily similar to a more *declarative* language like SQL (purely as an example, you don't have to understand the following)

```sql
SELECT JobTitle, COUNT(1)
FROM Users
WHERE IsActive = 1
GROUP BY JobTitle
ORDER BY JobTitle ASC
```

- `select` is similar to `map`
- `where` is similar to `filter`

> This particular *flavour* is called MS-SQL (Microsoft SQL)

Your task is to write your own stream classes to accomplish something similar to Java Streams.

#### Task 1 - Generic Table

> For this entire lab you are prohibited from using any Java streams APIs.  You can also presume that tables won't be modified during iteration.

The first task is to make a generic table class.  You've been provided with a sample table in `src/unsw/stream/Table.java`.

```java
package unsw.stream;

import java.util.List;

public class Table {
    private List<User> records;

    public Table(List<User> records) {
        this.records = records;
    }

    public TableView toView() {
        return new SimpleTableView(records.iterator());
    }
}
```

This should be `Table<E>` (make sure to name the generic argument `E`) after making it generic.  This will require the conversion of the `TableView` and `SimpleTableView` classes as well.  This should be mostly just changing generic arguments to Iterators/ArrayLists.

Just to ensure this is very clear here are some example signatures that you should end up with, for some of the more complicated functions in `TableView`;

```java
public<R> TableView<R> select(Function<E, R> selector);
public <R> R reduce(BiFunction<R, E, R> reducer, R initial);
```

What sort of design pattern does `SimpleTableView` follow?

> After doing this comment the first test in `Tests.java` and uncomment the rest.

#### Task 2 - Count & Reduce & Select

Your next task is to implement the `count`, `select`, and `reduce` functions in `SimpleTableView`.  An explanation of the functions is listed in the interface with an example.  Make sure to update the interface and implementation with a generic instead of using `User`.

This is a relatively simple exercise, just make sure you don't just call the Java Streams functions.  Look at the given take/skip functions for two approaches, try to pick an approach that means you do the least amount of work possible, for example if you do `select(x -> x.y).take(10)` it should only process the selection function 10 times.

Write some tests that test the various cases here, there are some simple tests to help you get started.  Just one or two extra should be fine.

#### Task 3 - Parallelism

Currently our reduce function just operates on one item at a time, we want to be fancier here and operate on multiple at the same time!  Parallelism/Threading can help us here.  `parallelReduce` will operate on the stream from multiple threads at the same time!  However, iterators aren't thread-safe and require synchronisation to ensure you don't cause data races and other sorts of threading issues.  Effectively, every thread will be using the same iterator allowing it to iterate through multiple items at the same time.

`parallelReduce` is already written for you and you don't have to modify it.  The problem however, is that your reduce function most likely is not thread-safe, so you need to figure out a way to synchronise it.

The key points to think about here are;
- How are you going to guarantee no item gets processed twice (or more)
- How are you going to guarantee no item gets skipped
- Think about the following case in particular... (this is called a 'race')

```java
if (iterator.hasNext()) {
    // can anything occur between above
    // and the line below that means it's no longer
    // valid to call `next`, causing an exception to be thrown?
    iterator.next();
}
```

Write your reasonings to how your code protects against these 3 cases in `answers.md`.

Could you write a test case to demonstrate the issues at play here?  Try creating a test case with a lot of data to demonstrate the issue (no tests cases are provided for this task).  Document in answers.md why you think your test case shows off the issue.

Finally, fix your code in reduce to handle this parallelism;
- Think about reducing the critical section, do as little work as you can in a lock
- Does your lock actually encourage parallelism?

Finally, is this actually faster?  Try running it on 1000 items and [using a timer](https://docs.oracle.com/javase/7/docs/api/java/util/Timer.html).  Write your conclusions in `answers.md`

## Lab 08 - Bonus Exercise - Tribute 🎸

Consider the song [Tribute](https://www.youtube.com/watch?v=_lK4cX5xGiQ). What Object Oriented Design Principle does the song embody? Write it down inside `tribute.md`.

## Submission

To submit, make a tag to show that your code at the current commit is ready for your submission using the command:

```bash
$ git tag -fa submission -m "Submission for Lab-08"
$ git push -f origin submission
```

Or, you can create one via the GitLab website by going to **Repository > Tags > New Tag**.
We will take the last commit on your master branch before the deadline for your submission.
