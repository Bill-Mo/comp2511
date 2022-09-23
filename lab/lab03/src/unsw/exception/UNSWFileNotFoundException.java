package unsw.exception;

import java.io.FileNotFoundException;

public class UNSWFileNotFoundException extends FileNotFoundException{

    public UNSWFileNotFoundException(String message) {
        super(message);
    }

}
