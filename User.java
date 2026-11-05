import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private String bio;
    BioType bioType;
    AccStatus accStatus;
    ArrayList<User> followers;
    ArrayList<User> followings;
    ArrayList<UserAction> userActions;
    ArrayList<Post> posts;
    private ArrayList<Participants> participants;
    ArrayList<AdminAction> adminActions;

    public User(String userName, String password, String bio, BioType bioType, AccStatus accStatus) {
        this.userName = userName;
        this.password = password;
        this.bio = bio;
        this.bioType = bioType;
        this.accStatus = accStatus;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.userActions = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.adminActions = new ArrayList<>();
    }
    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }
    public String getUserName() {
            return userName;
    }
    public void addParticipant(Participants participant) {
        this.participants.add(participant);
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }


}
