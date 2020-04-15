package com.example.no_more_trash.services;


import java.util.ArrayList;
import java.util.List;
import com.example.no_more_trash.models.User;


public class StaticContentUsers {

    private static List<User> registeredUsers = new ArrayList<>();


    public static List<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public static User getUser(String username) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    public static long createUserInDataBase(User user, DatabaseHelper db) {
        registeredUsers.add(user);
        return db.addUser(user);
    }

    public static void addUserFromDataBase(User user) {
        registeredUsers.add(user);
    }
}
