import java.util.ArrayList;
import java.util.Date;

public class Admin extends User {
    String adminId;
    ArrayList<AdminAction> adminActions;

    public Admin(String userName, String password, String bio, BioType bioType, AccStatus accStatus, String adminId) {
        super(userName, password, bio, bioType, accStatus);
        this.adminId = adminId;
        this.adminActions = new ArrayList<>();
    }

}
