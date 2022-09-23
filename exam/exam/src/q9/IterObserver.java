package q9;

public class IterObserver<P extends Comparable<P>> {
    private NetworkIterator<P> iterator;
    private String orderBy;
    
    public IterObserver(NetworkIterator<P> iterator, String orderBy) {
        this.iterator = iterator;
        this.orderBy = orderBy;
    }

    public void update(WasteBookController<P> w) {
        iterator = w.getIterator(orderBy);
    }
}
