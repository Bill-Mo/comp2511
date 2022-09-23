package unsw.blackout.Device;

import unsw.utils.Angle;

public class DesktopDevice extends Device{

    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, "DesktopDevice", position, 100000);
    }
    
}
