package test;

public class entity {
    private pos p;
    private String name;
    public entity(pos p, String name) {
        this.p = p;
        this.name = name;
    }
    public pos getP() {
        return p;
    }
    public void setP(pos p) {
        this.p = p;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
