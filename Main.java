import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Platform platform = new Platform();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // ---------------- CREATE ADMIN AND USERS ----------------
        try {
            Admin admin = new Admin(
                    "admin", "admin", "system admin", BioType.PUBLIC,
                    AccStatus.PUBLIC, "1313"
            );
            platform.addAdmin(admin);
            platform.users.put("prs", new User("prs", "prs", "parsa", BioType.PUBLIC,
                    AccStatus.PUBLIC));
            platform.users.put("arm", new User("arm", "arm", "test", BioType.PUBLIC,
                    AccStatus.PUBLIC));
        } catch (SocialMediaException ignored) {}

        // ---------------- MAIN LOOP ----------------
        while (true) {
            System.out.println("\n=== SOCIAL MEDIA PLATFORM ===");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> signup();
                case 3 -> {
                    System.out.println("Program terminated.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ================= AUTH =================

    static void login() {
        try {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = platform.login(username, password);
            userMenu(user);

        } catch (SocialMediaException e) {
            System.out.println(e.getMessage());
        }
    }

    static void signup() {
        try {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Bio: ");
            String bio = sc.nextLine();

            platform.signUp(username, password, bio, BioType.PUBLIC, AccStatus.PUBLIC);

        } catch (SocialMediaException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= USER MENU =================

    static void userMenu(User user) {

        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("User: " + user.getUserName());
            System.out.println("Bio: " + user.getBio());
            if (platform.getGlobalAnnouncementsSize() != 0)
                System.out.println("\nAnnouncements: ");
            platform.displayAnnouncement();
            System.out.println("\n1. Create Post");
            System.out.println("2. Interact With Post");
            System.out.println("3. Follow User");
            System.out.println("4. Unfollow User");
            System.out.println("5. Remove Follower");
            System.out.println("6. View Messages");
            System.out.println("7. Send Message");
            System.out.println("8. Reply in Chat");
            System.out.println("9. View Feed");
            System.out.println("10. Posts List");
            System.out.println("11. Followers List");
            System.out.println("12. Followings List");
            System.out.println("13. Change Bio");
            if (user instanceof Admin) {
                System.out.println("14. Ban User");
                System.out.println("15. Remove Post");
            }

            System.out.println("0. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
//                    case 911 -> platform.displayThisUserChats(user);
                    case 1 -> createPost(user);
                    case 2 -> interactPost(user);
                    case 3 -> follow(user);
                    case 4 -> unfollow(user);
                    case 5 -> removeFollower(user);
                    case 6 -> platform.displayMessages(user);
                    case 7 -> sendMessage(user);
                    case 8 -> enterChatId(user);
                    case 9 -> viewFeed(user);
                    case 10 -> platform.listOfPosts(user);
                    case 11 -> platform.listOfFollowers(user);
                    case 12 -> platform.listOfFollowings(user);
                    case 13 -> bio(user);
                    case 14 -> {
                        if (user instanceof Admin)
                            banUser((Admin) user);
                    }
                    case 15 -> {
                        if (user instanceof Admin)
                            removePost((Admin) user);
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SocialMediaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // ================= FEATURES =================

    private static void createPost(User user) throws PermissionDeniedException {
        try {
            System.out.print("Post content: ");
            String content = sc.nextLine();

            System.out.print("Post status (PUBLIC / PRIVATE): ");
            String statusStr = sc.nextLine().toUpperCase();

            // Convert string to PostStatus enum
            PostStatus status;
            try {
                status = PostStatus.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid post status. Defaulting to PUBLIC.");
                status = PostStatus.PUBLIC;
            }

            Post p = platform.userCreatePost(user, content, status, Duration.ZERO);
            System.out.println("Post created successfully. Post ID: " + p.getPostID());

        } catch (PermissionDeniedException e) {
            System.out.println("Error: Only admins can create ANNOUNCEMENTS.");
        }
    }

    private static void interactPost(User user) throws SocialMediaException {
        System.out.print("Post ID: ");
        String postId = sc.nextLine();

        try {
            System.out.print("Like? (true/false): ");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        boolean like = sc.nextBoolean();
        sc.nextLine();

        System.out.print("Comment: ");
        String comment = sc.nextLine();

        platform.interactWithPost(user, postId, like, comment);
    }

    private static void follow(User user) throws SocialMediaException {
        System.out.print("Username to follow: ");
        platform.followUser(user, sc.nextLine());
    }

    private static void unfollow(User user) throws SocialMediaException {
        System.out.print("Username to unfollow: ");
        platform.unfollowUser(user, sc.nextLine());
    }

    private static void removeFollower(User user) throws SocialMediaException {
        System.out.print("Username to remove follower: ");
        String username = sc.nextLine();
        platform.removeFollowers(user, username);
    }

    private static void sendMessage(User sender) throws SocialMediaException {

        try {
            System.out.print("Number of users to message: ");
            int count = sc.nextInt();
            sc.nextLine();

            ArrayList<User> members = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                System.out.print("Username: ");
                String username = sc.nextLine();
                members.add(platform.getUserByUsername(username));
            }

            System.out.print("Message content: ");
            String msg = sc.nextLine();

            platform.sendMessage(sender, members, msg);

        } catch (Exception e) {
            throw new WrongEntryNumberException("Something went wrong while sending message.");
        }

    }

    private static void enterChatId(User user) throws SocialMediaException {
        System.out.print("Enter Chat ID: ");
        String chatId = sc.nextLine();
        platform.replyChat(user, chatId);
    }

    private static void viewFeed(User user) {
        for (Post p : platform.getPersonalizedFeed(user)) {
            System.out.println(p.getPostID() + " : " + p.getContent());
        }
    }

    // ================= ADMIN =================

    private static void banUser(Admin admin) throws SocialMediaException {
        System.out.print("Username to ban: ");
        platform.adminBanUser(admin, sc.nextLine());
    }

    private static void removePost(Admin admin) throws SocialMediaException {
        System.out.print("Post ID to remove: ");
        platform.adminActionOnPost(admin, sc.nextLine(), AdminActionType.REMOVED_POST);
    }

    private static void bio(User user) {
        System.out.print("Bio content: ");
        String bio = sc.nextLine();
        platform.changeBio(user, bio);
    }
}
