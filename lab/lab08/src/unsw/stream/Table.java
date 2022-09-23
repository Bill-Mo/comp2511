package unsw.stream;

import java.util.Iterator;
import java.util.List;

public class Table {
    private List<User> records;

    public Table(List<User> records) {
        this.records = records;
    }

    public TableView toView() {
        return new SimpleTableView(records.iterator());
    }
}
