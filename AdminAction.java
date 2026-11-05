import java.sql.Timestamp;

public class AdminAction {
    private Timestamp actionTime;
    private AdminActionType adminActionType;
    public  AdminAction(AdminActionType adminActionType) {
        this.actionTime = new Timestamp(System.currentTimeMillis());
        this.adminActionType = adminActionType;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }
    public AdminActionType getAdminActionType() {
        return adminActionType;
    }
    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }
}
