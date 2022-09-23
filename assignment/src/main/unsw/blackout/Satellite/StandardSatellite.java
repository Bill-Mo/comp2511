package unsw.blackout.Satellite;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite{

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, "StandardSatellite", height, position, 2500, 150000, 80, 1, 1);
    }

    @Override
    public boolean isStorageFull(int fileSize) {
        if (files.size() < 3 && (availableStorage - fileSize) >= 0) {
            return false;
        }
        return true;
    }
    @Override
    public String storageFull() {
        if (files.size() >= 3) {
            return "Max Files Reached";
        } else {
            return "Max Storage Reached";
        }
    }
    
}
