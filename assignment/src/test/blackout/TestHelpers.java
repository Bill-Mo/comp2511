package blackout;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Collections;
import java.util.List;

public class TestHelpers {
    public static<T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(List<T> a, List<T> b) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }
}
