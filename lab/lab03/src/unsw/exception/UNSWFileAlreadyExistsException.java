package unsw.exception;

import java.nio.file.FileAlreadyExistsException;


public class UNSWFileAlreadyExistsException extends FileAlreadyExistsException{

    public UNSWFileAlreadyExistsException(String message) {
        super(message);
    }
    
}
