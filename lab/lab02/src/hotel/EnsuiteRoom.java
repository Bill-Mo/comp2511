package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class EnsuiteRoom extends Room {

    private List<Booking> Bookings = new ArrayList<Booking>();

    public EnsuiteRoom() {
        super();
    }

    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your beautiful ensuite room which overlooks the Sydney harbour. Enjoy your stay");
    }
    
}