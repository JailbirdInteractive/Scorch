package com.jailbird.scorch;

/**
 * Created by adria on 1/29/2017.
 */

public class FriendRequest {
   public String id;
    public boolean status;
    public String name;
    public FriendRequest(String id,boolean status,String name){
        this.id=id;
        this.status=status;
        this.name=name;
    }
    public FriendRequest(){

    }
    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof FriendRequest){
            FriendRequest hp=(FriendRequest)obj;
            isMatch=hp.id.equalsIgnoreCase(this.id);
        }
        return isMatch;    }

    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());

        return result;    }
}
