
public class FileIdentifier {
    private int mediaId;
    private String fileLocation;

    public FileIdentifier(int mediaId, String fileLocation) {
        this.mediaId = mediaId;
        this.fileLocation = fileLocation;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
