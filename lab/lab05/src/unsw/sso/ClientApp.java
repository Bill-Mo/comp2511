package unsw.sso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.sso.providers.Hoogle;

public class ClientApp {
    private boolean hasHoogle = false;

    // HINT: Don't overcomplicate this
    //       for Task 2) you'll want some sort of object
    //       but don't go overboard, even in Task 4)
    //       this object can be relatively simple.
    private Map<String, Boolean> usersExist = new HashMap<>();
    private final String name;

    public ClientApp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // TODO: you'll probably want to change a lot of this class
    public void registerProvider(Object o) {
        if (o instanceof Hoogle) {
            hasHoogle = true;
        }    
    }

    public boolean hasHoogle() {
        return hasHoogle;
    }

    public void registerUser(Token token) {
        // only hoogle is supported right now!  So we presume hoogle on user
        usersExist.put(token.getUserEmail(), true);
    }

    public boolean hasUserForProvider(String email, Object provider) {
        return provider instanceof Hoogle && this.hasHoogle && this.usersExist.getOrDefault(email, false);
    }

    public boolean hasHoogleUser(String email) {
        return usersExist.getOrDefault(email, false);
    }
}
