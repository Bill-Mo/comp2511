package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class StandardRoom extends Room {

    private List<Booking> bookings = new ArrayList<Booking>();

    public StandardRoom() {
        super();
    }



    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
    
}