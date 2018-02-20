package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public class User extends Entity implements Comparable<User> {

    //@SerializedName("UserName")
    @DatabaseField
    private String username;
    //@SerializedName("Password")
    @DatabaseField
    private String password;
    //@SerializedName("CounsellorCode")
    @DatabaseField
    private String counsellorcode = "";

    private int strategyId;

    public User() {
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String uuid) {
        this.username = username;
        this.password = password;
        setUuid(uuid);
    }


    public User(String username, String password, int iqcareid, String counsellorcode) {
        this.username = username;
        this.password = password;
        setIqcareid(iqcareid);
        this.counsellorcode = counsellorcode;
    }

    public User getLoginUser(String password) {
        User user = new User(username, password);

        return user;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCounsellorcode() {
        return counsellorcode;
    }

    public void setCounsellorcode(String counsellorcode) {
        this.counsellorcode = counsellorcode;
    }

    public String getCode() {

        if (counsellorcode.length() > 0) {//check
            return " [" + counsellorcode + "]";
        }
        return counsellorcode;
    }

    @Override
    public String toString() {
        //check
        return username + getCode();
    }

    @Override
    public int compareTo(User lookup) {
        return this.username.compareTo(lookup.username);
    }

    public void setStrategy(int id) {
        strategyId = id;
    }

    public int getStrategy() {
        return strategyId;
    }
}

