import java.util.ArrayList;
import java.util.Collection;

public class Chat {
    private String chatID;
    ChatType chatType;
    ArrayList<Participants> participants;

    public Chat(String chatID, ChatType chatType) {
        this.chatID = chatID;
        this.chatType = chatType;
        this.participants =  new ArrayList<>();
    }
    public String getChatID() {
        return chatID;
    }
    public void addPrticipant(Participants participant) { // add exception
        this.participants.add(participant);
    }
    public void removeParticipant(Participants participant) { // add exception
        this.participants.remove(participant);
    }
}
