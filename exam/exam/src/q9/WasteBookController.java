package q9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WasteBookController<P extends Comparable<P>> {
    
    List<Person<P>> people = new ArrayList<Person<P>>();
    IterObserver<P> observer = null;
    /**
     * Adds a new member with the given name to the network. 
     */
    public void addPersonToNetwork(P name) {
        people.add(new Person<P>(name));
    }

    /**
     * @preconditions person1 and person2 already exist in the social media network.
     * person1 follows person2 in the social media network.
     */
    public void follow(P person1, P person2) {
        if (person1.equals(person2)) {
            return;
        }

        Person<P> member1 = people.stream().filter(p -> p.getId().equals(person1)).findFirst().get();
        Person<P> member2 = people.stream().filter(p -> p.getId().equals(person2)).findFirst().get();
        member1.getFollowing().add(member2);
        addFriend(member1, member2);
        if (observer != null) {
            observer.update(this);
        }
    }

    public void addFriend(Person<P> member1, Person<P> member2) {
        if (member2.isFollowing(member1)) {
            member1.addFriend(member2);
            member2.addFriend(member1);
        }
    }

    public int getPopularity(P person) {
        return (int) people.stream().filter(p -> p.getFollowing().contains(getPerson(person))).count();
    }

    public int getFriends(P person) {
        return getPerson(person).getFriends().size();
    }

    /**
     * Returns an iterator to the network (each member)
     * ordered by the given parameter.
     */
    public NetworkIterator<P> getIterator(String orderBy) {
        Comparator<Person<P>> comparator = null;
        if (orderBy.equals("popularity")) {
            comparator = Comparator.comparing(p -> -getPopularity(p.getId()));
        } else if (orderBy.equals("friends")) {
            comparator = Comparator.comparing(p -> -getFriends(p.getId()));
        }

        comparator = comparator.thenComparing(Person::getId);

        NetworkIterator<P> iter =  new NetworkIterator<P>(people.stream()
                     .sorted(comparator)
                     .map(Person::getId)
                     .collect(Collectors.toList())
                     .iterator());
        
        observer = new IterObserver<P>(iter, orderBy);

        return iter;
    }

    public void switchIteratorComparisonMethod(NetworkIterator<P> iter, String orderBy) {
        // TODO Part d)
    }

    public Person<P> getPerson(P person) {
        return people.stream().filter(p -> p.getId().equals(person)).findFirst().get();
    }
}