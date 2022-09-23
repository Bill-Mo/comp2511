package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExtraTests {
    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(180));

        // moves in positive direction
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(181.23), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(182.46), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(183.69), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        
        // edge case
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.82), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(191.05), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        
        // goes back down
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.82), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(183.69), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testQuantumBehaviour() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        controller.createSatellite("Satellite1", "ShrinkingSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(315));

        // uploads at a rate of 15 per minute so we'll give it 21 bytes which when compressed is 14
        String msg = "hello quantum how are";
        controller.addFileToDevice("DeviceA", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceA", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        // we still should have 6 bytes to send
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", "hello quantum h", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        // now that we are done we should see shrinkage
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", msg, 14, true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        // sending file back down to other device it needs to send full 21 bytes, bandwidth out is 10 so it should take 3 ticks
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // we still should have 11 bytes to send
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", "hello quan", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // and still 1 more byte to send
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", "hello quantum how ar", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // done! and file size should not be shrunk, we aren't on the shrinking satellite
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", "hello quantum how are", msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));
    }
}
