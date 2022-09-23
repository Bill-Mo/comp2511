package unsw.stream.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import unsw.stream.Table;
import unsw.stream.User;

public class Tests {
    // After doing Task 1 update this test.
    @Test
    public void SimpleTest() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        assertIterableEquals(Arrays.asList(), table.toView().skip(5));
        assertIterableEquals(users, table.toView().skip(0));
        assertIterableEquals(Arrays.asList(), table.toView().take(0));
        assertIterableEquals(users, table.toView().take(5));

        // it's okay to use streams in tests only
        assertIterableEquals(users.stream().skip(2).collect(Collectors.toList()), table.toView().skip(2));
        // there is no take(int) in java so we'll just make do with a more 'awful' styled variant here
        assertIterableEquals(users.stream().takeWhile(x -> x.userId().equals("C") == false).collect(Collectors.toList()), table.toView().take(2));

        assertIterableEquals(users.stream().skip(2).takeWhile(x -> x.userId().equals("D") == false).collect(Collectors.toList()), table.toView().skip(2).take(1));
    }

    // After doing Task 1 uncomment all tests below this line
    /*
    @Test
    public void Task2_Count() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        assertEquals(5, table.toView().count());
        assertEquals(0, table.toView().take(0).count());
        assertEquals(0, table.toView().skip(5).count());
        assertEquals(1, table.toView().skip(4).take(1).count());
    }

    @Test
    public void Task2_Select() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        assertIterableEquals(Arrays.asList("Devs", "Devs", "Testers", "Business Analysts", "CEO"), table.toView().select(x -> x.jobTitle()));
        assertIterableEquals(Arrays.asList("Devs"), table.toView().select(x -> x.jobTitle()).take(1));
    }

    @Test
    public void Task2_Reduce() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        
        assertEquals(false, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalAnd, true));
        assertEquals(true, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalOr, true));
        assertEquals("Devs, Devs, Testers, Business Analysts, CEO", table.toView().select(x -> x.jobTitle()).reduce((acc, cur) -> acc.isEmpty() ? cur : acc + ", " + cur, ""));
    }

    @Test
    public void Task3_ParallelReduce() {
        List<User> users = new ArrayList<User>();
        users.add(new User(true, "A", "Devs"));
        users.add(new User(true, "B", "Devs"));
        users.add(new User(false, "C", "Testers"));
        users.add(new User(true, "D", "Business Analysts"));
        users.add(new User(true, "E", "CEO"));

        Table<User> table = new Table<User>(users);
        
        assertEquals(false, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalAnd, true));
        assertEquals(true, table.toView().select(x -> x.isActive()).reduce(Boolean::logicalOr, true));
        assertEquals(false, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalAnd, Boolean::logicalAnd, true, 16));
        assertEquals(true, table.toView().select(x -> x.isActive()).parallelReduce(Boolean::logicalOr, Boolean::logicalOr, true, 16));
    }
    */
}
