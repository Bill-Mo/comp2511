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

public class Task2Tests {
    @Test
    public void testExceptionsForSend() {
        BlackoutController controller = new BlackoutController();

        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(150));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        // Send a partial file from Satellite1 to Device B
        controller.simulate(1);
        assertEquals(new FileInfoResponse("FileAlpha", "H", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));

        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        // Send a large file to Satellite1
        String large = "This string has a length 90!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
        controller.addFileToDevice("DeviceC", "large", large);
        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("large", "DeviceC", "Satellite1"));
        
        String f1 = "This string has a length 76!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
        controller.addFileToDevice("DeviceC", "f1", f1);
        String f2 = "It's afternoon";
        controller.addFileToDevice("DeviceA", "f2", f2);
        controller.addFileToDevice("DeviceC", "f2", f2);
        assertDoesNotThrow(() -> controller.sendFile("f1", "DeviceC", "Satellite1"));
        controller.simulate(3);
        // Send a file when the bandwidth of the destination satellite is full
        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class, () -> controller.sendFile("f2", "DeviceC", "Satellite1"));
        controller.simulate(80);
        // Send a file when the destination satellite have no storage
        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("f2", "DeviceA", "Satellite1"));
    }

    @Test
    public void testRelaySatellite() {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(200));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(150));
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.communicableEntitiesInRange("Satellite1"));
        controller.createSatellite("SatelliteR2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(160));
        controller.createSatellite("SatelliteR3", "RelaySatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(190));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "DeviceA", "SatelliteR3"), controller.communicableEntitiesInRange("SatelliteR2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "SatelliteR2", "SatelliteR3"), controller.communicableEntitiesInRange("Satellite1"));
    }
}
