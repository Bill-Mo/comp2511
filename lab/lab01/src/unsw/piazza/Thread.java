package unsw.piazza;

import java.util.ArrayList;
import java.util.List;

/**
 * A thread in the Piazza forum.
 */
public class Thread {

    /**
     * Creates a new thread with a title and an initial first post.
     * @param title
     * @param firstPost
     */
    public Thread(String title, String firstPost) {}

    /**
     * @return The title of the thread
     */
    public String getTitle() {
        return null;
    }

    /**
     * @return A SORTED list of tags
     */
    public List<String> getTags() {
        return null;
    }

    /**
     * @return A list of posts in this thread, in the order that they were published
     */
    public List<String> getPosts() {
        return null;
    }

    /**
     * Adds the given post object into the list of posts in the thread.
     * @param post
     */
    public void publishPost(String post) {}

    /**
     * Allows the given user to replace the thread tags (list of strings)
     * @param tags
     */
    public void setTags(String[] tags) {}
}
