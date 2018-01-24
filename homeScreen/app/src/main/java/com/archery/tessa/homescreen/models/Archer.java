package com.archery.tessa.homescreen.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Archer implements Serializable {

    private String  firstName = "";
    private String  lastName = "";
    private int     coachId = -1;
    private int     weight = 0;
    private int     height = 0;
    private boolean rightHanded = true;

    //Name of the corresponding database table
    private static final String TABLENAME = "Athlete";

    public Archer(String _firstName, String _lastName, int _coachId, int _weight, int _height, boolean _rightHanded) {
        firstName = _firstName;
        lastName = _lastName;
        coachId = _coachId;
        weight = _weight;
        height = _height;
        rightHanded = _rightHanded;
    }

    public Archer(String _firstName, String _lastName, boolean _rightHanded) {
        firstName = _firstName;
        lastName = _lastName;
        rightHanded = _rightHanded;
    }

    public Archer(String _firstName, String _lastName) {
        firstName = _firstName;
        lastName = _lastName;
        rightHanded = true;
    }

    public Archer() {}


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public boolean isRightHanded() {
        return rightHanded;
    }

    public void setRightHanded(boolean rightHanded) {
        this.rightHanded = rightHanded;
    }

    public static String getTableName() {
        return TABLENAME;
    }

    public String toString() {
        String res = "Archer: " + firstName + " " + lastName + ", handedness: ";
        res += (rightHanded) ? "right" : "left";
        return res;
    }

}

