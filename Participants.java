import java.util.ArrayList;

public class Participants {
    private String participantID;
    private String participantName;
    private boolean isOwner;
    private ArrayList<Message> messages;

    public Participants(String participantName, String participantsID) {
        this.participantID = participantID;
        this.participantName = participantName;
        this.isOwner = false;
        this.messages = new ArrayList<>();
    }
    public String getParticipantID() {
        return participantID;
    }
    public String getParticipantName() {
        return participantName;
    }
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }
    public boolean isOwner() {
        return isOwner;
    }
    public void addMessage(Message message) {
        this.messages.add(message);
    }

}
