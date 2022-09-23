package q9;

import java.util.Iterator;
import static q9.WasteBookTestBaseline.assertEquals;

public class WasteBookTestExtra {
    public static void main(String[] args) {
        testIteratorAddPersonToNetwork();
    }

    public static void testIteratorAddPersonToNetwork() {
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
        controller.addPersonToNetwork("Darcy");
        controller.follow("Nathan", "Darcy");
        controller.follow("Amelia", "Darcy");

        assertEquals("Amelia", iter.next());
        assertEquals("Darcy", iter.next());
        assertEquals("Hamish", iter.next());
        assertEquals("Nathan", iter.next());
        assertEquals(false, iter.hasNext());
    }
}