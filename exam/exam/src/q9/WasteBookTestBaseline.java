package q9;

import java.util.Iterator;

public class WasteBookTestBaseline {
    public static void main(String[] args) {
        testBasic();
        testIteratorPopularity();
        testIteratorFriends();
    }

    public static void testBasic() {
        WasteBookController<String> controller = new WasteBookController<String>();

        controller.addPersonToNetwork("Nathan");
        controller.addPersonToNetwork("Evanlyn");
        controller.addPersonToNetwork("Amelia");
        controller.addPersonToNetwork("Hamish");

        controller.follow("Nathan", "Evanlyn");
        controller.follow("Amelia", "Evanlyn");
        controller.follow("Amelia", "Hamish");
        controller.follow("Hamish", "Amelia");
        controller.follow("Nathan", "Amelia");
        controller.follow("Hamish", "Evanlyn");

        assertEquals(0, controller.getPopularity("Nathan"));
        assertEquals(3, controller.getPopularity("Evanlyn"));
        assertEquals(1, controller.getPopularity("Hamish"));
        assertEquals(2, controller.getPopularity("Amelia"));

        assertEquals(0, controller.getFriends("Nathan"));
        assertEquals(0, controller.getFriends("Evanlyn"));
        assertEquals(1, controller.getFriends("Hamish"));
        assertEquals(1, controller.getFriends("Amelia"));

        

    }

    public static void testIteratorPopularity() {
        WasteBookController<String> controller = new WasteBookController<String>();

        controller.addPersonToNetwork("Nathan");
        controller.addPersonToNetwork("Evanlyn");
        controller.addPersonToNetwork("Amelia");
        controller.addPersonToNetwork("Hamish");

        controller.follow("Nathan", "Evanlyn");
        controller.follow("Amelia", "Evanlyn");
        controller.follow("Amelia", "Hamish");
        controller.follow("Hamish", "Amelia");
        controller.follow("Nathan", "Amelia");
        controller.follow("Hamish", "Evanlyn");

        Iterator<String> iter = controller.getIterator("popularity");
        assertEquals("Evanlyn", iter.next());
        assertEquals("Amelia", iter.next());
        assertEquals("Hamish", iter.next());
        assertEquals("Nathan", iter.next());
        assertEquals(false, iter.hasNext());
    }

    public static void testIteratorFriends() { 
        WasteBookController<String> controller = new WasteBookController<String>();

        controller.addPersonToNetwork("Nathan");
        controller.addPersonToNetwork("Evanlyn");
        controller.addPersonToNetwork("Amelia");
        controller.addPersonToNetwork("Hamish");

        controller.follow("Nathan", "Evanlyn");
        controller.follow("Amelia", "Evanlyn");
        controller.follow("Amelia", "Hamish");
        controller.follow("Hamish", "Amelia");
        controller.follow("Nathan", "Amelia");
        controller.follow("Hamish", "Evanlyn");
        controller.follow("Evanlyn", "Amelia");
        controller.follow("Evanlyn", "Nathan");
        controller.follow("Amelia", "Nathan");

        Iterator<String> iter = controller.getIterator("friends");
        assertEquals("Amelia", iter.next());
        assertEquals("Evanlyn", iter.next());
        assertEquals("Nathan", iter.next());
        assertEquals("Hamish", iter.next());
        assertEquals(false, iter.hasNext());
    }

    public static void assertEquals(Object a, Object b) {
        if (!a.equals(b)) {
            throw new RuntimeException(a + " and " + b + " are not equal");
        }
    }
}