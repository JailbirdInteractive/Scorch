package com.jailbird.scorch;

import java.util.Date;
import java.util.List;

import noman.googleplaces.Place;

/**
 * Created by adria on 3/6/2017.
 */

public class Event {
    public String eventID;
    public Place place;
    public String imgUrl,eventName,eventDetails,eventAdmission;
    public Date startDate,endDate;
    public List<Interest> interests;
    public String eventAddress;
    public Integer peopleGoing,peopleInterested;
    public boolean isBoosted;
    public Event(){

    }
    public Event(String eventID,String eventAddress,Place place,String imgUrl,String eventName,String eventDetails,List<Interest> interests,Date startDate,Date endDate,String eventAdmission,Integer peopleGoing,Integer peopleInterested,boolean isBoosted){
        this.place=place;
        this.imgUrl=imgUrl;
        this.eventName=eventName;
        this.eventDetails=eventDetails;
        this.startDate=startDate;
        this.endDate=endDate;
        this.eventID=eventID;
        this.interests=interests;
        this.eventAddress=eventAddress;
        this.eventAdmission=eventAdmission;
        this.peopleGoing=peopleGoing;
        this.peopleInterested=peopleInterested;
        this.isBoosted=isBoosted;
    }
    Place getPlace(){
        return place;
    }
    String getImgUrl(){
        return imgUrl;
    }
    String getEventName(){
        return eventName;
    }
    String getEventDetails(){
        return eventDetails;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public Integer getPeopleGoing() {
        return peopleGoing;
    }

    public Integer getPeopleInterested() {
        return peopleInterested;
    }

    public String getEventID() {
        return eventID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEventAdmission() {
        return eventAdmission;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public boolean isBoosted() {
        return isBoosted;
    }

    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }

    public void setEventAdmission(String eventAdmission) {
        this.eventAdmission = eventAdmission;
    }

    public void setPeopleGoing(Integer peopleGoing) {
        this.peopleGoing = peopleGoing;
    }

    public void setPeopleInterested(Integer peopleInterested) {
        this.peopleInterested = peopleInterested;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof Event){
            Event a=(Event) obj;
            isMatch=a.eventID.equalsIgnoreCase(this.eventID);
        }
        return isMatch;
    }

    @Override
    public int hashCode() {

        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.eventID == null ? 0 : this.eventID.hashCode());

        return result;    }

}


