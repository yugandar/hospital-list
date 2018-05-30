package com.example.goa.hospitallist;

/**
 * Created by goa on 20/1/18.
 */




public class pojo {

    String id;
    String user;



    public pojo(String id, String user) {
        this.id=id;
        this.user = user;
    }
    public String getId(){
        return id;
    }

    public String getUser() {
        return user;
    }

}
