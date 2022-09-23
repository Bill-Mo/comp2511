package unsw.piazza;

import java.util.List;

/**
 * A Piazza Forum 
 * 
 * @author Your Name
 */
public class PiazzaForum {
    
    /**
     * Initialises the new PiazzaForum with the given group name
     */
    public PiazzaForum(String className) {}

    /**
     * @return The name of the forum
     */
    public String getName() {
        return null;
    }

    /**
     * Sets the name of the group of the Forum
     * @param name
     */
    public void setName(String name) {}

    /**
     * Returns a list of Threads in the Forum, in the order that they were published
     */
    public List<Thread> getThreads() {
        return null;
    }

    /**
     * Creates a new thread with the given title and adds it to the Forum.
     * The content is provided to allow you to create the first Post.
     * Threads are stored in the order that they are published.
     * Returns the new Thread object
     * @param title
     * @param content
     */
    public Thread publish(String title, String content) {
        return null;
    }

    /**
     * Searches all forum Threads for any that contain the given tag.
     * Returns a list of all matching Thread objects in the order that they were published.
     * @param tag
     * @return
     */
    public List<Thread> searchByTag(String tag) {
        return null;
    }

}