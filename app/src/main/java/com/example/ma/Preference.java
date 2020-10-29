package com.example.ma;

public class Preference {
    private String userid;
    private int placeid;
    private boolean liked;
    private boolean disliked;
    private String sex;
    private int age;


    Preference (String userid, String placeid, String liked, String disliked, String sex, String age){
        this.userid = userid;
        this.placeid = Integer.parseInt(placeid);
        this.liked = Boolean.parseBoolean(liked);
        this.disliked = Boolean.parseBoolean(disliked);
        //this.sex = sex;
        //this.age = Integer.parseInt(age);
    }

    String get_userid(){
        return userid;
    }
    int get_placeid(){ return placeid; }
    boolean get_liked(){
        return liked;
    }
    boolean get_disliked(){
        return disliked;
    }
}
