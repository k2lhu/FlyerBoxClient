package com.flyerbox.logic;

/**
 * Created by tmrafael on 24.10.2014.
 */
public class UserProfile {
    private String firstName;   //Public get; private set
    private String lastName;    //Public get; private set
    private String email;       //Public get; private set
    private String password;    //Private get; private set
    private String city;        //Public get; private set
    private String country;     //Public get; private set

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public UserProfile(String firstName, String lastName, String email, String password, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
