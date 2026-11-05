import java.sql.Timestamp;

public class Message {
    private String message;
    private String messageID;
    Timestamp timestamp;

    public Message(String message, String messageID) {
        this.message = message;
        this.messageID = messageID;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    public String getMessage() {
        return message;
    }
    public void editMessage(String message) {
        this.message = message;
    }
    public String getMessageID() {
        return messageID;
    }
}
