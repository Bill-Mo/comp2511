package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Hotel {

    private String name;
    private Room e;
    private Room s;
    private Room p;

    public Hotel(String name) {
    this.e = new EnsuiteRoom();
    this.s = new StandardRoom();
    this.p = new PenthouseRoom();
    this.name = name;
    }
    /**
     * Makes a booking in any available room with the given preferences.
     * 
     * @param arrival
     * @param departure
     * @param standard - does the client want a standard room?
     * @param ensuite - does the client want an ensuite room?
     * @param penthouse - does the client want a penthouse room?
     * @return If there were no available rooms for the given preferences, returns false.
     * Returns true if the booking was successful
     */
    public boolean makeBooking(LocalDate arrival, LocalDate departure, boolean standard, boolean ensuite, boolean penthouse) {
        if (standard) {
            if (s.book(arrival, departure) != null) {
                return true;
            }
        }
        if (ensuite) {
            if (e.book(arrival, departure) != null) {
                return true;
            }
        }
        if (penthouse) {
            if (p.book(arrival, departure) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return A JSON Object of the form:
     * { "name": name, "rooms": [ each room as a JSON object, in order of creation ]}
     */
    public JSONObject toJSON() {
        return null;
    }

    public static void main(String[] args) {
        
    }   
}