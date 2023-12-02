import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    // Types of Messages
    public enum MessageType {
        LOGIN,
        LOGOUT,
        FIND_TABLE,
        BANK_DETAILS,
        SETTINGS,
        DEPOSIT,
        WITHDRAW,
        CHANGE_NAME,
        CHANGE_PASSWORD,
        DELETE_ACCOUNT
    }

    // Common Fields
    private MessageType type;
    private String userId;
    private String password;

    // Fields specific to certain message types
    private String newUserName;
    private String newPassword;
    private double depositAmount;
    private double withdrawAmount;

    // Constructors for different message types
    public Message(MessageType type, String userId, String password) {
        this.type = type;
        this.userId = userId;
        this.password = password;
    }

    public Message(MessageType type, String userId, String password, String newUserName, String newPassword) {
        this(type, userId, password);
        this.newUserName = newUserName;
        this.newPassword = newPassword;
    }

    public Message(MessageType type, String userId, String password, double depositAmount) {
        this(type, userId, password);
        this.depositAmount = depositAmount;
    }

    public Message(MessageType type, String userId, String password, double withdrawAmount, boolean isWithdraw) {
        this(type, userId, password);
        if (isWithdraw) {
            this.withdrawAmount = withdrawAmount;
        }
    }

    // Getters for fields
    public MessageType getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public double getWithdrawAmount() {
        return withdrawAmount;
    }
}
