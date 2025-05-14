package org.restoranproje.model;
import java.time.LocalDateTime;
import java.time.LocalDate;


public abstract class User implements Observer {
    protected String name;

    protected UserType userType;
    protected String password;

    public User(String name,  UserType userType, String password) {
        this.name = name;

        this.userType = userType;
        this.password = password;
    }

    public abstract void update(Order order);  // Observer interface'inden gelir

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }
    public String getPassword() {
        return password;
    }

}

