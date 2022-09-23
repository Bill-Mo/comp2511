package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.Device.DesktopDevice;
import unsw.blackout.Device.Device;
import unsw.blackout.Device.HandheldDevice;
import unsw.blackout.Device.LaptopDevice;
import unsw.blackout.File.File;
import unsw.blackout.Satellite.RelaySatellite;
import unsw.blackout.Satellite.Satellite;
import unsw.blackout.Satellite.ShrinkingSatellite;
import unsw.blackout.Satellite.StandardSatellite;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private ArrayList<Device> devices = new ArrayList<Device>();
    private ArrayList<Satellite> satellites = new ArrayList<Satellite>();

    public Device findDevice(String deviceId) {
        for (Device d : devices) {
            if (d.getDeviceId() == deviceId) {
                return d;
            }
        }
        return null;
    }

    public Satellite findSatellite(String satelliteId) {
        for (Satellite s : satellites) {
            if (s.getSatelliteId() == satelliteId) {
                return s;
            }
        }
        return null;
    }

    public void createDevice(String deviceId, String type, Angle position) {
        if (type == "HandheldDevice") {
            HandheldDevice newDevice = new HandheldDevice(deviceId, position);
            devices.add(newDevice);
        } else if (type == "LaptopDevice") {
            LaptopDevice newDevice = new LaptopDevice(deviceId, position);
            devices.add(newDevice);
        } else if (type == "DesktopDevice") {
            DesktopDevice newDevice = new DesktopDevice(deviceId, position);
            devices.add(newDevice);
        }
    }

    public void removeDevice(String deviceId) {
        devices.remove(findDevice(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        if (type == "StandardSatellite") {
            StandardSatellite newSatellite = new StandardSatellite(satelliteId, height, position);
            satellites.add(newSatellite);
        } else if (type == "ShrinkingSatellite") {
            ShrinkingSatellite newSatellite = new ShrinkingSatellite(satelliteId, height, position);
            satellites.add(newSatellite);
        } else if (type == "RelaySatellite") {
            RelaySatellite newSatellite = new RelaySatellite(satelliteId, height, position);
            satellites.add(newSatellite);
        }
    }

    public void removeSatellite(String satelliteId) {
        satellites.remove(findSatellite(satelliteId));
    }

    public List<String> listDeviceIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Device d : devices) {
            ids.add(d.getDeviceId());
        }
        return ids;
    }

    public List<String> listSatelliteIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Satellite s : satellites) {
            ids.add(s.getSatelliteId());
        }
        return ids;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        File f = new File(filename, content);
        f.setAvailableSize(content.length());
        findDevice(deviceId).addFile(f);
    }

    public EntityInfoResponse getInfo(String id) {
        Device d = findDevice(id);
        Satellite s = findSatellite(id);
        if (d != null) {
            return d.deviceInfo();
        } else if (s != null) {
            return s.satelliteInfo();
        }
        return null;
    }

    public void simulate() {
        for (Satellite s : satellites) {
            s.move();
        }
        for (Device d : devices) {
            transfer(d);
        }
        for (Satellite s : satellites) {
            transfer(s);
        }
    }

    public void transfer(Device d) {
        for  (File f : d.getFiles().values()) {
            if (!f.hasTransferCompleted()) {
                Satellite s = findSatellite(f.getFrom());
                f.setAvailableSize(f.getAvailableSize() + s.uploadSpeed());
                if (f.hasTransferCompleted()) {
                    s.setUploadingNum(s.getUploadingNum() - 1);
                }
            }
        }
    }

    public void transfer(Satellite sa) {
        for  (File f : sa.getFiles().values()) {
            if (!f.hasTransferCompleted()) {
                Device d = findDevice(f.getFrom());
                Satellite s = findSatellite(f.getFrom());
                if (d != null) {
                    f.setAvailableSize(f.getAvailableSize() + sa.downloadSpeed());
                    if (f.hasTransferCompleted()) {
                        sa.setDownloadingNum(sa.getDownloadingNum() - 1);
                    }
                }
                if (s != null) {
                    int limitation;
                    if (sa.downloadSpeed() > s.uploadSpeed()) {
                        limitation = s.uploadSpeed();
                    } else {
                        limitation = sa.downloadSpeed();
                    }
                    f.setAvailableSize(f.getAvailableSize() + limitation);
                    if (f.hasTransferCompleted()) {
                        sa.setDownloadingNum(sa.getDownloadingNum() - 1);
                        s.setUploadingNum(s.getUploadingNum() - 1);
                    }
                }
            }
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        ArrayList<String> eInRange = new ArrayList<>();
        Device device = findDevice(id);
        Satellite satellite = findSatellite(id);
        if (device != null) {
            for (Satellite s : satellites) {
                if (device.isInRange(s)) {
                    eInRange.add(s.getSatelliteId());
                    if (s.getType() == "RelaySatellite") {
                        ArrayList<String> oldEntities = new ArrayList<>();
                        oldEntities.add(s.getSatelliteId());
                        eInRange.addAll(entitiesInRelaysRange(s, oldEntities));
                    }
                }
            }
        } else if (satellite != null) {
            for (Satellite s : satellites) {
                if (satellite.isInRange(s) && satellite != s) {
                    eInRange.add(s.getSatelliteId());
                    if (s.getType() == "RelaySatellite") {
                        ArrayList<String> oldEntities = new ArrayList<>();
                        oldEntities.add(s.getSatelliteId());
                        eInRange.addAll(entitiesInRelaysRange(s, oldEntities));
                    }
                }
            }
            for (Device d : devices) {
                if (satellite.isInRange(d)) {
                    eInRange.add(d.getDeviceId());
                }
            }
        }
        HashSet<String> nonRepeat = new HashSet<String>(eInRange);
        ArrayList<String> result = new ArrayList<String>(nonRepeat);
        result.remove(id);
        return result;
    }

    public ArrayList<String> entitiesInRelaysRange(Satellite relay, ArrayList<String> oldEntities) {
        ArrayList<String> newEntities = new ArrayList<>();
        // Add all non-repeated communicable entities into l
        for (Satellite s : satellites) {
            if (relay.isInRange(s) && !oldEntities.contains(s)) {
                newEntities.add(s.getSatelliteId());
            }
        }
        for (Device d : devices) {
            if (relay.isInRange(d)) {
                newEntities.add(d.getDeviceId());
            }
        }

        // Add entities that are in the range of relay satellites that in the range of origin relay satellite 
        for (Satellite s : satellites) {
            if (s.getType() == "RelaySatellite" && newEntities.contains(s.getSatelliteId()) && !oldEntities.contains(s.getSatelliteId())) {
                newEntities.addAll(entitiesInRelaysRange(s, newEntities));
            }
        }
        return newEntities;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Device sourceD = findDevice(fromId);
        Satellite sourceS = findSatellite(fromId);
        Device destD = findDevice(toId);
        Satellite destS = findSatellite(toId);
        // If the file is from a device, the destination should be a satellite
        if (sourceD != null) {

            // Check Is the destination satellite in range
            if (!sourceD.isInRange(destS)) {
                return;
            }
            // If the file does not exist on the source device or it is a partial file on the source device
            if (sourceD.getFiles().get(fileName) == null || sourceD.getFiles().get(fileName).hasTransferCompleted() == false) {
                throw new VirtualFileNotFoundException(fileName);
            }

            // If the file already exist on the destination satellite
            if (destS.getFiles().get(fileName) != null) {
                throw new VirtualFileAlreadyExistsException(fileName);
            }

            // If the destiantion setallite cannot download
            if (!destS.canDownload()) {
                throw new VirtualFileNoBandwidthException(destS.getSatelliteId());
            }

            // If the destinaton setallite is full
            if (destS.isStorageFull(sourceD.getFileContent(fileName).length())) {
                throw new VirtualFileNoStorageSpaceException(destS.storageFull());
            }

            File target = new File(fileName, sourceD.getFileContent(fileName));
            target.setFrom(fromId);
            target.setTo(toId);
            destS.addFile(target);
            destS.setAvailableStorage(destS.getAvailableStorage() - target.getSize());
            destS.setDownloadingNum(destS.getDownloadingNum() + 1);
        // If the file is from a satellite
        } else if (sourceS != null) {

            // If the file does not exist on the source satellite or it is a partial file on the source satellite
            if (sourceS.getFiles().get(fileName) == null || sourceS.getFiles().get(fileName).hasTransferCompleted() == false) {
                throw new VirtualFileNotFoundException(fileName);
            }

            // If the source setallite cannot upload
            if (!sourceS.canUpload()) {
                throw new VirtualFileNoBandwidthException(sourceS.getSatelliteId());
            }

            // If the file is sent to a satellite
            if (destS != null) {

                // Check Is the destination satellite in range
                if (!sourceS.isInRange(destS)) {
                    return;
                }

                // If the file already exist on the destination satellite
                if (destS.getFiles().get(fileName) != null) {
                    throw new VirtualFileAlreadyExistsException(fileName);
                }
                
                // If the destination satellite cannot download
                if (!destS.canDownload()) {
                    throw new VirtualFileNoBandwidthException(destS.getSatelliteId());
                }

                // If the destinaton setallite is full
                if (destS.isStorageFull(sourceS.getFileContent(fileName).length())) {
                    throw new VirtualFileNoStorageSpaceException(destS.storageFull());
                }

                File target = new File(fileName, sourceS.getFileContent(fileName));
                target.setFrom(fromId);
                target.setTo(toId);
                destS.addFile(target);
                destS.setAvailableStorage(destS.getAvailableStorage() - target.getSize());
                destS.setDownloadingNum(destS.getDownloadingNum() + 1);

            // If the file is sent to a device
            } else if (destD != null) {

                // Check Is the destination device in range
                if (!sourceS.isInRange(destD)) {
                    return;
                }
                
                // If the file already exist on the destination device
                if (destD.getFiles().get(fileName) != null) {
                    throw new VirtualFileAlreadyExistsException(fileName);
                }

                File target = new File(fileName, sourceS.getFileContent(fileName));
                target.setFrom(fromId);
                target.setTo(toId);
                destD.addFile(target);
            }
            
            sourceS.setUploadingNum(sourceS.getUploadingNum() + 1);
        }
    }
}
