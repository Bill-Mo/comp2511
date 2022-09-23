package satellite;


public class Satellite {
    private String n;
    private int h;
    private double v;
    private double angle;
    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double velocity, double position) {
        this.n = name;
        this.h = height;
        this.v = velocity;
        this.angle = position;
    }

    /**
     * Getter for name
     */
    public String getName() {
        return n;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return h;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return angle;
    }

    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return Math.toRadians(angle);
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return v;
    }

    /**
     * Returns the angular velocity (degrees per second) of the satellite
     */
    public double getAngularVelocity() {
        return v / (2 * Math.PI * h * 1000) * 360;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        n = name;
    }

    /**
     * Setter for height
     * @param height
     */
    public void setHeight(int height) {
        h = height;
    }

    /**
     * Setter for velocity
     * @param velocity
     */
    public void setVelocity(double velocity) {
        v = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        angle = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return v * time;
    }

    public static void main(String[] args) {
        Satellite A = new Satellite("A", 10000, 55, 122);
        Satellite B = new Satellite("B", 5438, 234000, 0);
        Satellite C = new Satellite("C", 9029, 0, 284);
        System.out.println("I am " + A.n + " at position " + A.angle + " degrees, " + A.h + " km above the centre of the earth and moving at a velocity of " + A.v + " metres per second");
        A.setHeight(9999);
        B.setPosition(45);
        C.setVelocity(36.5);
        System.out.println(A.getPositionRadians());
        System.out.println(Math.toRadians(B.getAngularVelocity()));
        System.out.println(C.distance(120));
    }

}