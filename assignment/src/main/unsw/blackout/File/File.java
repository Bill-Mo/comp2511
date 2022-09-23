package unsw.blackout.File;

public class File {
    private String filename;
    private String content;
    private String availableContent;
    private int size;
    private int availableSize;
    private String from;
    private String to;
    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.availableContent = "";
        this.size = content.length();
        this.availableSize = 0;
        this.from = "";
        this.to = "";
    }
    public String getFilename() {
        return filename;
    }
    public String getContent() {
        return content;
    }
    public int getSize() {
        return size;
    }
    public String getAvailableContent() {
        return availableContent;
    }
    public void setAvailableContent() {
        availableContent = content.substring(0, availableSize);
    }
    public int getAvailableSize() {
        return availableContent.length();
    }
    public void setAvailableSize(int availableSize) {
        if (availableSize > content.length()) {
            availableSize = content.length();
        }
        this.availableSize = availableSize;
        setAvailableContent();
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public boolean hasTransferCompleted() {
        return content.equals(availableContent);
    }
}
