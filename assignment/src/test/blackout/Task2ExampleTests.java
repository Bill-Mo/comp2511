package blackout;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite2"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(342.05), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }
}
