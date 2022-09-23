package unsw.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class SimpleTableView implements TableView {
    private Iterator<User> it;

    public SimpleTableView() {
        this(Arrays.<User>asList().iterator());
    }

    public SimpleTableView(Iterator<User> it) {
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public User next() {
        return it.next();
    }

    @Override
    public TableView take(int numberOfItems) {
        SimpleTableView parent = this;

        return new SimpleTableView() {
            private int itemsLeft = numberOfItems;

            @Override
            public boolean hasNext() {
                return itemsLeft > 0 && parent.hasNext();
            }

            @Override
            public User next() {
                if (hasNext()) {
                    itemsLeft--;
                    return parent.next();
                } else
                    throw new NoSuchElementException();
            }
        };
    }

    @Override
    public TableView skip(int numberOfItems) {
        while (numberOfItems > 0 && hasNext()) {
            numberOfItems--;
            next();
        }
        return this;
    }

    @Override
    public Table toTable() {
        List<User> list = new ArrayList<User>();
        it.forEachRemaining(list::add);
        return new Table(list);
    }

    @Override
    public Iterator<User> iterator() {
        // *technically* this is non standard
        // since this should reproduce a unique iterator each time
        // but for our sakes it's fine, since any operation on an
        // iterator will implicitly invalidate the inner iterators
        // invalidating it's original context anyways.
        return this;
    }

    @Override
    public int count() {
        // TODO: Task 2
        return 0;
    }

    @Override
    public<R> TableView select(Function<User, R> selector) {
        // TODO: Task 2
        return null;
    }

    @Override
    public <R> R reduce(BiFunction<R, User, R> reducer, R initial) {
        // TODO: Task 2
        return null;
    }

    @Override
    public <R> R parallelReduce(BiFunction<R, User, R> reducer, BinaryOperator<R> combiner, R initial, int numberOfThreads) throws InterruptedException, ExecutionException {
        // you don't have to change this function at all.

        // create a pool of threads to pick jobs.
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        Callable<R> reductionOperation = () -> reduce((acc, cur) -> reducer.apply(acc, cur), initial);

        // fully exhaust pool
        List<Callable<R>> callables = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) callables.add(reductionOperation);

        // execute all the operations, this is the concurrency part (this single function call)
        List<Future<R>> results = pool.invokeAll(callables);
        
        // at this point we are single threaded and can just accumulate the left over values
        R accValue = initial;
        for (Future<R> result : results) {
            accValue = combiner.apply(accValue, result.get());
        }

        return accValue;
    }
}
