package com.flyerbox.logic;

/**
 * FlyerBoxClient
 * Created by roman on 11/13/14.
 * 1:27 PM
 */
public class Session {
    private int sessionID;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String country;

    public int getSessionID() {
        return sessionID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void createSessionFrom(String response) {
        sessionID = parseSessionIDFromResponse(response);
    }

    public void loadUserDataToApp(String response) {
        parseUserData(response);
    }

    private int parseSessionIDFromResponse(String response) {
        int result;
        String devidedBy = "\"";
        String[] arrayOfStrings = response.split(devidedBy);
        String token = arrayOfStrings[7];
        result = Integer.parseInt(token);

        return result;
    }

    private void parseUserData(String response) {
        String devidedBy = "\"";
        String[] arrayOfStrings = response.split(devidedBy);
        lastName = arrayOfStrings[3];
        email = arrayOfStrings[11];
        firstName = arrayOfStrings[15];
        country = arrayOfStrings[19];
        city = arrayOfStrings[23];

        // Server response {"lastName":"roman","status":"success","email":"qw@qw.com",
        //                  "firstName":"roman","country":"oiuh","city":"oiuhn"}
    }

}
