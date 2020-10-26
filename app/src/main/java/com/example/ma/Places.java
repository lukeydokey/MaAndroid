package com.example.ma;

public class Places {
    private int id;
    private int placeid;
    private String placename;
    private String category;
    private int like;
    private int dislike;
    private double recomrate;

    Places (String id, String placeid, String placename, String category, String like, String dislike, String recomrate){
        this.id = Integer.parseInt(id);
        this.placeid = Integer.parseInt(placeid);
        this.placename = placename;
        this.category = category;
        this.like = Integer.parseInt(like);
        this.dislike = Integer.parseInt(dislike);
        this.recomrate = Double.parseDouble(recomrate);
    }

    String get_placename(){
        return placename;
    }
}
