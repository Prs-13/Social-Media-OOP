import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;

public class Post {
    private String postID;
    private Timestamp postTime;
    private String content;
    PostType postType;
    PostStatus postStatus;
    Duration duration;
    ArrayList<UserAction> userActions;
    ArrayList<AdminAction> adminActions;

    public Post(String postID, String content, PostStatus postStatus, Duration duration) {
        this.postID = postID;
        this.content = content;
        this.postStatus = postStatus;
        this.duration = duration;
        if (this.duration == null && this.postStatus != PostStatus.ANNOUNCEMENT) {
            this.duration = Duration.ZERO;
            this.postType = PostType.IMAGE;
        }
        this.postTime = new Timestamp(System.currentTimeMillis());
        this.adminActions = new ArrayList<>();
        this.userActions = new ArrayList<>();
    }

    public String getPostID() {
        return postID;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void addUserAction(UserAction userAction) {
        this.userActions.add(userAction);
    }

    public void addAdminAction(AdminAction adminAction) {
        this.adminActions.add(adminAction);
    }

    public ArrayList<UserAction> getUserActions() {
        return userActions;
    }

    public String getContent() {
        return content;
    }


}
