package com.archery.tessa.homescreen.models;

import java.io.Serializable;

public class Archer implements Serializable {

    private String  email = "";
    private String  firstName = "";
    private String  lastName = "";
    private int     coachId = -1;
    private int     weight = 0;
    private int     height = 0;
    private boolean rightHanded = true;

    public Archer(String _email, String _firstName, String _lastName, int _coachId, int _weight, int _height, boolean _rightHanded) {
        email = _email;
        firstName = _firstName;
        lastName = _lastName;
        coachId = _coachId;
        weight = _weight;
        height = _height;
        rightHanded = _rightHanded;
    }

    public Archer(String _email, String _firstName, String _lastName, boolean _rightHanded) {
        email = _email;
        firstName = _firstName;
        lastName = _lastName;
        rightHanded = _rightHanded;
    }

    public Archer(String _email, String _firstName, String _lastName) {
        email = _email;
        firstName = _firstName;
        lastName = _lastName;
        rightHanded = true;
    }

    public Archer() {}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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


    public String toString() {
        String res = "Archer: " + firstName + " " + lastName + ", handedness: ";
        res += (rightHanded) ? "right" : "left";
        return res;
    }

}
