package library.com.Entity.User;

import library.com.Entity.Helper.Trading;
import library.com.Entity.Library;
import org.jetbrains.annotations.NotNull;

public class User {
    private String userName;
    private String emailId;
    private String customerId;

    public User(){

    }
    public User(@NotNull String userName,@NotNull String emailId) {
        this.userName = userName;
        this.emailId = emailId;
        this.customerId = userName + "$" + emailId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getCustomerId() {
        return customerId;
    }







}
