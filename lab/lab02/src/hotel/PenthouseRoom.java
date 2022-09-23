package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class PenthouseRoom extends Room {

    private List<Booking> bookings = new ArrayList<Booking>();

    public PenthouseRoom() {
        super();
    }

    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your penthouse apartment, complete with ensuite, lounge, kitchen and master bedroom.");
    }
    
}