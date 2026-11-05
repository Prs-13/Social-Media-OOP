import java.time.Duration;
import java.util.*;

 public interface UserInteraction {
     void addAdmin(Admin admin) throws SocialMediaException;
     void signUp(String userName, String password, String bio, BioType bioType, AccStatus accStatus) throws SocialMediaException;
     User login(String username, String password) throws SocialMediaException;
     public void changeBio(User user, String bio);

    // --- Connections ---
     void followUser(User u, String targetUserName) throws SocialMediaException;
     void unfollowUser(User u, String targetUserName) throws SocialMediaException;
     void removeFollowers(User u, String targetUserName) throws SocialMediaException;

    // --- Post ---
     Post userCreatePost(User user, String content, PostStatus status, Duration duration) throws PermissionDeniedException;
     void interactWithPost(User user, String postId, boolean like, String comment) throws SocialMediaException;

    // --- Feed ---
     ArrayList<Post> getPersonalizedFeed(User user);

    // --- Messaging ---
     void displayMessages(User user) throws SocialMediaException;
     void sendMessage(User sender, ArrayList<User> members, String messageContent) throws SocialMediaException;
     void replyChat(User user, String chatId) throws SocialMediaException;

    // --- Admin ---
     void adminActionOnPost(User admin, String postId, AdminActionType type) throws SocialMediaException;
     void adminBanUser(User admin, String targetUserName) throws SocialMediaException;
     // -- Log Display --
      void displayAnnouncement();
      void listOfPosts(User user);
      void listOfFollowers(User user);
      void listOfFollowings(User user);
}
