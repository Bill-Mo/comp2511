package unsw.blackout.Satellite;

import java.util.HashMap;
import java.util.Map;

import unsw.blackout.Device.Device;
import unsw.blackout.File.File;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Satellite {
    protected String satelliteId;
    protected String type;
    protected double height;
    protected Angle position;
    protected int speed;
    protected int maxRange;
    protected Map<String, File> files;
    protected int availableStorage;
    protected int uploadingNum;
    protected int downloadingNum;
    protected int uploadBandwidth;
    protected int downloadBandwidth;
    public Satellite(String satelliteId, String type, double height, Angle position, int speed, int maxRange, int availableStorage, int uploadBandwidth, int downloadBandwidth) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
        this.speed = speed;
        this.maxRange = maxRange;
        this.files = new HashMap<String, File>();
        this.availableStorage = availableStorage;
        this.uploadingNum = 0;
        this.downloadingNum = 0;
        this.uploadBandwidth = uploadBandwidth;
        this.downloadBandwidth = downloadBandwidth;
    }
    public String getSatelliteId() {
        return satelliteId;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return position;
    }
    public void setPosition(Angle position) {
        this.position = position;
    }

    public Map<String, File> getFiles() {
        return files;
    }
    
    public String getFileContent(String fileName) {
        return files.get(fileName).getContent();
    }

    public void addFile(File f) {
        files.put(f.getFilename(), f);
    }

    public int getAvailableStorage() {
        return availableStorage;
    }

    public void setAvailableStorage(int availableStorage) {
        this.availableStorage = availableStorage;
    }

    public int getUploadingNum() {
        return uploadingNum;
    }

    public void setUploadingNum(int uploadingNum) {
        this.uploadingNum = uploadingNum;
    }

    public int getDownloadingNum() {
        return downloadingNum;
    }

    public void setDownloadingNum(int downloadingNum) {
        this.downloadingNum = downloadingNum;
    }

    public EntityInfoResponse satelliteInfo() {
        Map<String, FileInfoResponse> fInfo = new HashMap<>();
            if (type == "ShrinkingSatellite") {
                for (File f : files.values()) {
                    if (f.getContent().contains("quantum") && f.hasTransferCompleted()) {
                        fInfo.put(f.getFilename(), new FileInfoResponse(f.getFilename(), f.getAvailableContent(), (int)Math.ceil(f.getSize() * 2 / 3), f.hasTransferCompleted()));
                    } else {
                        fInfo.put(f.getFilename(), new FileInfoResponse(f.getFilename(), f.getAvailableContent(), f.getSize(), f.hasTransferCompleted()));
                    }
                }
                EntityInfoResponse e = new EntityInfoResponse(satelliteId, position, height, type, fInfo);
                return e;
            } else {
                for (File f : files.values()) {
                    fInfo.put(f.getFilename(), new FileInfoResponse(f.getFilename(), f.getAvailableContent(), f.getSize(), f.hasTransferCompleted()));
                }
                EntityInfoResponse e = new EntityInfoResponse(satelliteId, position, height, type, fInfo);
                return e;
            }
    }
    
    public void move() {
        double r = height;
        int v = speed;
        double degree = v / (2 * Math.PI * r) * 360;
        Angle a = Angle.fromDegrees(degree);
        setPosition(getPosition().add(a));
    }

    public boolean isInRange(Device d) {
        if (getType() == "StandardSatellite") {
            if (d.getType() == "DesktopDevice") {
                return false;
            }
        }
        if (MathsHelper.isVisible(height, position, d.getPosition())) {
            if (MathsHelper.getDistance(height, position, d.getPosition()) <= maxRange) {
                return true;
            }
        }
        return false;
    }

    public boolean isInRange(Satellite s) {
        if (MathsHelper.isVisible(height, position, s.getHeight(), s.getPosition())) {
            if (MathsHelper.getDistance(height, position, s.getHeight(), s.getPosition()) <= maxRange) {
                return true;
            }
        }
        return false;
    }

    public boolean isStorageFull(int fileSize) {
        return false;
    }

    public String storageFull() {
        return "";
    }

    public boolean canUpload() {
        if (uploadingNum >= uploadBandwidth) {
            return false;
        }
        return true;
    }

    public boolean canDownload() {
        if (downloadingNum >= downloadBandwidth) {
            return false;
        }
        return true;
    }

    public int downloadSpeed() {
        return downloadBandwidth / downloadingNum;
    }

    public int uploadSpeed() {
        return uploadBandwidth / uploadingNum;
    }

}
