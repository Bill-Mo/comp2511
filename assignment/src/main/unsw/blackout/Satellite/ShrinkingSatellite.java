package unsw.blackout.Satellite;

import unsw.utils.Angle;

public class ShrinkingSatellite extends Satellite{
    
    public ShrinkingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, "ShrinkingSatellite", height, position, 1000, 200000, 150, 10, 15);
    }

    @Override
    public boolean isStorageFull(int fileSize) {
        if ((getAvailableStorage() - fileSize) >= 0) {
            return false;
        }
        return true;
    }
    @Override
    public String storageFull() {
        return "Max Storage Reached";
    }
    
}
