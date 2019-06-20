package com.robot.asus.MusicBuddy;

public class Users {
    private String Work, Relax, Depress, Sleep, userId, userName;

    public Users(){}

    public Users(String work, String relax, String depress, String sleep, String userId, String userName) {
        Work = work;
        Relax = relax;
        Depress = depress;
        Sleep = sleep;
        this.userId = userId;
        this.userName = userName;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getRelax() {
        return Relax;
    }

    public void setRelax(String relax) {
        Relax = relax;
    }

    public String getDepress() {
        return Depress;
    }

    public void setDepress(String depress) {
        Depress = depress;
    }

    public String getSleep() {
        return Sleep;
    }

    public void setSleep(String sleep) {
        Sleep = sleep;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getListId(String situation){
        String listId = "";
        switch (situation){
            case "Work":
                listId = Work;
                break;
            case "Relax":
                listId = Relax;
                break;
            case "Depress":
                listId = Depress;
                break;
            case "Sleep":
                listId = Sleep;
                break;
        }
        return listId;
    }
}
