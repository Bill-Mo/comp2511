# Synchronisation

How does your code protect against the following cases;

## Double Processing Items


## Skipping Items


## Races (the third case)


# Test Case 

How does your test case show off the bug.

# Performance

Is it faster?  If so explain why, if it isn't faster explain why it isn't.

In both cases consider cheap reduction functions like add (`x, y -> x + y`) and more complicated reduction functions that might take 1-2s to complete each time (i.e. a sleep inside the reduction function).
