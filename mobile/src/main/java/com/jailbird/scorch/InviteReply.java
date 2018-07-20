package com.jailbird.scorch;

/**
 * Created by adria on 2/9/2017.
 */
public class InviteReply {
   public String userId,reply,userName;
    public boolean seen;

    public InviteReply(String userId,String reply,String userName,boolean seen){
        this.reply=reply;
        this.userId=userId;
        this.userName=userName;
        this.seen=seen;
    }
    public InviteReply(){

    }
}
