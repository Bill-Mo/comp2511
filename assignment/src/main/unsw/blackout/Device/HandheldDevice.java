package unsw.blackout.Device;

import unsw.utils.Angle;

public class HandheldDevice extends Device{

    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, "HandheldDevice", position, 50000);
    }
}
