package com.example.ma;

public class Places {
    private int id;
    private int placeid;
    private String placename;
    private String category;
    private int like;
    private int dislike;
    private double recomrate;
    private String address;

    Places (String id, String placeid, String placename, String category, String like, String dislike, String recomrate, String address){
        this.id = Integer.parseInt(id);
        this.placeid = Integer.parseInt(placeid);
        this.placename = placename;
        this.category = category;
        this.like = Integer.parseInt(like);
        this.dislike = Integer.parseInt(dislike);
        this.recomrate = Double.parseDouble(recomrate);
        this.address = address;
    }

    int get_id(){ return id; }
    String get_placename(){
        return placename;
    }
    int get_like(){
        return like;
    }
    int get_dislike(){
        return dislike;
    }
    double get_recomrate(){ return recomrate; }
    String get_address(){
        return address;
    }

}
