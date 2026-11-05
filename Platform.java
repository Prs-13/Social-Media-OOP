import java.time.Duration;
import java.util.*;
import java.util.Scanner;

public class Platform implements UserInteraction {
    Map<String, User> users = new HashMap<>();
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Post> globalAnnouncements = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    public int getGlobalAnnouncementsSize() {
        return globalAnnouncements.size();
    }

//    public void displayThisUserChats(User user) {
//        for  (Chat chat : chats) {
//            for (Participants p : chat.participants) {
//                if (Objects.equals(p.getParticipantName(), user.getUserName())) {
//                    System.out.println(chat.getChatID());
//                }
//            }
//        }
//    }

    // --- Auth ---
    @Override
    public void addAdmin(Admin admin) throws SocialMediaException {
        if (users.containsKey(admin.getUserName()))
            throw new SocialMediaException("Admin already exists.");

        users.put(admin.getUserName(), admin);
    }

    @Override
    public void signUp(String userName, String password, String bio, BioType bioType, AccStatus accStatus) throws SocialMediaException {
        if (users.containsKey(userName)) throw new SocialMediaException("User ID already taken.");
        users.put(userName, new User(userName, password, bio, bioType, accStatus));
        System.out.println("Success: " + userName + " signed up.");
    }

    @Override
    public User login(String username, String password) throws SocialMediaException {
        User u = users.get(username);
        if (u == null) throw new UserNotFoundException("User not found.");
        if (u.accStatus == AccStatus.BANNED) throw new PermissionDeniedException("User is BANNED.");
        if (!u.checkPassword(password)) throw new AuthException("Wrong password.");
        System.out.println("Success: " + u.getUserName() + " logged in.");
        return u;
    }

    @Override
    public void changeBio(User user, String bio) {
        user.setBio(bio);
    }

    // --- Connections ---
    @Override
    public void followUser(User u, String targetUserName) throws SocialMediaException {
        User target = users.get(targetUserName);
        if (target == null) throw new UserNotFoundException("User not found to follow.");
        if (!target.followers.contains(u)) {
            if (target.accStatus != AccStatus.BANNED) {
                target.followers.add(u);
                u.followings.add(target);
                System.out.println(u.getUserName() + " followed " + target.getUserName());
                return;
            }
            System.out.println("User " + target.getUserName() + " is banned.");
        }
        else {
            System.out.println(targetUserName + " is already followed.");
        }
    }

    @Override
    public void unfollowUser(User u, String targetUserName) throws SocialMediaException {
        User target = users.get(targetUserName);
        if (target == null) throw new UserNotFoundException("User not found to unfollow.");
        if (target.followers.contains(u)) {
            target.followers.remove(u);
            u.followings.remove(target);
            System.out.println(u.getUserName() + " unfollowed " + targetUserName);
        }
        else {
            System.out.println(targetUserName + " is not followed to unfollow.");
        }
    }

    @Override
    public void removeFollowers(User u, String targetUserName) throws SocialMediaException {
        User target = users.get(targetUserName);
        if (target == null) throw new UserNotFoundException("User not found to remove from followers.");
        if (target.followings.contains(u)) {
            target.followings.remove(u);
            u.followers.remove(target);
            System.out.println(u.getUserName() + " removed " + targetUserName + " from followers.");
        }
        else {
            System.out.println(targetUserName + " is not following you to remove it from followers.");
        }
    }

    // --- Content ---
    @Override
    public Post userCreatePost(User user, String content, PostStatus status, Duration duration) throws PermissionDeniedException {
        if (status == PostStatus.ANNOUNCEMENT) {
            try {
                verifyAdmin(user);
            } catch (PermissionDeniedException e) {
                throw e;
            }
        }
        String postId = UUID.randomUUID().toString(); // Generate random ID
        Post p = new Post(postId, content, status, duration);

        user.posts.add(p);

        if (status == PostStatus.ANNOUNCEMENT) {
            globalAnnouncements.add(p);
        }

        System.out.println("Post created: " + content);
        return p;
    }

    @Override
    public void interactWithPost(User user, String postId, boolean like, String comment) throws SocialMediaException {
        Post p = findMatchingPostID(postId);
        if (p == null) throw new SocialMediaException("Post does not exist.");
        if (p.postStatus.equals(PostStatus.REMOVED)) throw new SocialMediaException("Post has been removed.");

        UserAction action = new UserAction(like, comment);
        user.userActions.add(action);
        p.userActions.add(action);
        System.out.println(user.getUserName() + " interacted with post " + postId);
    }

    private Post findMatchingPostID(String postId) {
        for (User u : users.values()) {
            for (Post p : u.posts) {
                if (p.getPostID().equals(postId)) {
                    return p;
                }
            }
        }
        return null;
    }

    // --- Feed ---
    @Override
    public ArrayList<Post> getPersonalizedFeed(User user) {
        ArrayList<Post> feed = new ArrayList<>();

        for (User friend : user.followings) {
            for (Post p : friend.posts) {
                if (p.postStatus != PostStatus.REMOVED) {
                    feed.add(p);
                }
            }
        }
        // 3. Sort by time (Newest first)
        feed.sort((p1, p2) -> p2.getPostTime().compareTo(p1.getPostTime()));
        return feed;
    }

    // --- Messaging ---
    @Override
    public void displayMessages(User user) throws SocialMediaException {
        ArrayList<Chat> thisUserChats = findChats(user);
        if (thisUserChats.isEmpty()) throw new SocialMediaException("No chats found.");
        for (Chat chat : thisUserChats) {
            System.out.print("Chat type: " + chat.chatType + "\nChat id: " + chat.getChatID() + "\n");
            for (Participants p : chat.participants) {
                if (p.getParticipantName().equals(user.getUserName())) {
                    System.out.println("\n===YOUR=== messages in this chat: ");
                    for (Message m : p.getMessages()) {
                        System.out.println(m.getMessage() + "\t" + m.getMessageID());
                    }
                } else {
                    System.out.println("\nSender: " + p.getParticipantName() + "\nMessages: ");
                    for (Message m : p.getMessages()) {
                        System.out.println(m.getMessage() + "\t" + m.getMessageID());
                    }
                }
            }
        }
    }
    private ArrayList<Chat> findChats(User user) {
        ArrayList<Chat> joinedChats = new ArrayList<>();
        for (Chat chat : chats) {
            for (Participants p : chat.participants) {
                if (Objects.equals(p.getParticipantName(), user.getUserName())) {
                    joinedChats.add(chat);
                }
            }
        }
        return joinedChats;
    }



    @Override
    public void sendMessage(User sender, ArrayList<User> members, String messageContent) throws SocialMediaException {
        if (members.size() == 1) {
            directMessage(sender, members.getFirst().getUserName(), messageContent);
            return;
        }
        groupMessage(sender, members, messageContent);
    }

    private void directMessage(User sender, String userName, String messageContent) throws SocialMediaException {
        User receiver  = users.get(userName);
        if (receiver == null) throw new UserNotFoundException("User not found to send message.");
        Chat chat = new Chat(UUID.randomUUID().toString(), ChatType.DIRECT);
        Participants p1 = new Participants(sender.getUserName(), UUID.randomUUID().toString());
        Participants p2 = new Participants(receiver.getUserName(), UUID.randomUUID().toString());
        chat.addPrticipant(p1);
        chat.addPrticipant(p2);
        sender.addParticipant(p1);
        receiver.addParticipant(p2);
        p1.addMessage(new Message(messageContent, UUID.randomUUID().toString()));
        System.out.println("Message " + messageContent + " sent to " + receiver.getUserName() +
                "\n--with chat id: " + chat.getChatID());
        chats.add(chat);
    }

    private void groupMessage(User sender, ArrayList<User> members, String messageContent) throws SocialMediaException {
        Chat chat = new Chat(UUID.randomUUID().toString(), ChatType.GROUP);
        Participants p1 = new Participants(sender.getUserName(), UUID.randomUUID().toString());
        chat.addPrticipant(p1);
        sender.addParticipant(p1);
        p1.addMessage(new Message(messageContent, UUID.randomUUID().toString()));
        for (User u : members) {
            if (users.get(u.getUserName()) == null) throw new UserNotFoundException("User not found to add group chat.");
            Participants p = new Participants(u.getUserName(), UUID.randomUUID().toString());
            chat.addPrticipant(p);
            u.addParticipant(p);
        }
        System.out.println("Message " + messageContent + " sent to Group chat with id : " + chat.getChatID());
        chats.add(chat);
    }

    public User getUserByUsername(String username) throws SocialMediaException {
        User u = users.get(username);
        if (u == null)
            throw new UserNotFoundException("User not found.");
        return u;
    }

    // --- ReplyChat ---
    @Override
    public void replyChat(User user, String chatId) throws SocialMediaException {
        Chat chat = findChatById(chatId);
        if (chat == null) throw new UserNotFoundException("Chat not found.");
        System.out.println("Replying to " + chat.getChatID() + "\nEnter message content: ");
        String message = sc.nextLine();
        for (Participants p : chat.participants) {
            if (p.getParticipantName().equals(user.getUserName())) {
                p.addMessage(new Message(message, UUID.randomUUID().toString()));
            }
        }
    }

    private Chat findChatById(String chatId) {
        for (Chat chat : chats) {
            if (chat.getChatID().equals(chatId)) {
                return chat;
            }
        }
        return null;
    }



    // --- Admin ---
    private void verifyAdmin(User u) throws PermissionDeniedException {
        if (!(u instanceof Admin)) throw new PermissionDeniedException("Access Denied: Admin only.");
    }

    @Override
    public void adminActionOnPost(User admin, String postId, AdminActionType type) throws SocialMediaException {
        verifyAdmin(admin);
        Post p = findMatchingPostID(postId);
        if (p == null) {
            throw new SocialMediaException("Post does not exist to interact with.");
        }
        AdminAction action = new AdminAction(type);
        if (type == AdminActionType.ANNOUNCEMENT) {
            p.postStatus = PostStatus.ANNOUNCEMENT;
            globalAnnouncements.add(p);
        } else {
            if (p.postStatus == PostStatus.ANNOUNCEMENT)
                globalAnnouncements.remove(p);
            p.postStatus = PostStatus.REMOVED;
        }
        p.adminActions.add(action);
        admin.adminActions.add(action);
        System.out.println("Admin removed post " + postId);
    }

    @Override
    public void adminBanUser(User admin, String targetUserName) throws SocialMediaException {
        verifyAdmin(admin);
        User target = users.get(targetUserName);
        if (target == null) {
            throw new SocialMediaException("User does not exist to ban.");
        }
        AdminAction action = new AdminAction(AdminActionType.BANNED_USER);
        target.accStatus = AccStatus.BANNED;
        target.adminActions.add(action);
        admin.adminActions.add(action);
        target.posts = null;
        for (User u : target.followers) {
            u.followings.remove(target);
        }
        target.followers = null;
        for (User u : target.followings) {
            u.followers.remove(target);
        }
        target.followings = null;
        System.out.println("Admin banned user " + targetUserName);
    }

    @Override
    public void displayAnnouncement() {
        for (Post p :  globalAnnouncements) {
            System.out.println("\t" + p.getPostID());
            System.out.println("\t" + p.getContent());
        }
    }

    @Override
    public void listOfPosts(User user) {
        int count = 0;
        for (Post p : user.posts) {
            count++;
            System.out.println(count + ". \n\t" + p.getPostID());
            System.out.println("\t" + p.getContent());
        }
    }

    @Override
    public void listOfFollowers(User user) {
        int count = 0;
        for (User u : user.followers) {
            count++;
            System.out.println(count + ". " + u.getUserName());
        }
    }

    @Override
    public void listOfFollowings(User user) {
        int count = 0;
        for (User u : user.followings) {
            count++;
            System.out.println(count + ". " + u.getUserName());
        }
    }

}


