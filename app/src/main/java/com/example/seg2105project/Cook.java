package com.example.seg2105project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Cook extends Account {

    /**
     * holds the list of complaints that are against a specific cook
     */
    private ArrayList<Complaint> complaintsAgainst;
    /**
     * hold the list of meals that a cook offers
     */
    private ArrayList<Meal> menu;
    /**
     * list of the types of cuisine that a cook can offer
     */
    private ArrayList<CuisineTypes> cuisine;
    /**
     * this is to determine if an account is suspended. an Admin will be able to modify this with their
     * access.
     */
    private boolean suspension;

    private Date suspensionTime;

    private Rating rating;

    /**
     *
     * @param username
     * @param password
     * @param name
     * @param address
     *
     */

    public Cook(String email, String username, String password, String name, Address address) {
        super(email, username, password, name, address, AccountType.COOK);
        this.suspension = false;
        this.menu = new ArrayList<>();
        this.rating = new Rating();
    }

    public Cook(){

    }
    public void setSuspension(boolean result){
        this.suspension = result;
    }

    public void addCuisineType(CuisineTypes type) {
        cuisine.add(type);
    }

    public ArrayList<CuisineTypes> getCuisineTypes() {
        return cuisine;
    }

    public void addComplaints(Complaint complaintMsg) {
        complaintsAgainst.add(complaintMsg);
    }

    public ArrayList<Complaint> getComplaints() {
        return complaintsAgainst;
    }

    public void addMeal(Meal meal) {
        menu.add(meal);
    }

    public void removeMeal(Meal meal) {
        menu.remove(meal);
    }

    public ArrayList<Meal> getMeal() {
        return menu;
    }

    public boolean getSuspension() {
        return suspension;
    }

    public void setSuspensionTime(Date time){
        suspensionTime = time;
    }

    public Date getSuspensionTime(){
        return suspensionTime;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }
}
