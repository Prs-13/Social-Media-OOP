import java.sql.Timestamp;

public class UserAction {
    private boolean like;
    private String comment;
    Timestamp likeTime;
    Timestamp commentTime;

    public UserAction(boolean like, String comment) {
        this.like = like;
        this.comment = comment;
        if (this.like) {
            this.likeTime = new  Timestamp(System.currentTimeMillis());
        }
        if (this.comment != null) {
            this.commentTime = new Timestamp(System.currentTimeMillis());
        }
    }
    public String getComment() {
        return comment;
    }
    public boolean isLike() {
        return like;
    }


}
