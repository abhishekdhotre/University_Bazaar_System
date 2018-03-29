package com.example.cse6324.university_bazaar_system;

import java.util.ArrayList;

public class User {

    private String email;
    private String name;
    private String phone;
    private String sid;
    private ArrayList<String> clubs_joined;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSid() {
        return sid;
    }

    public ArrayList<String> getClubs_joined() {
        return clubs_joined;
    }
}
