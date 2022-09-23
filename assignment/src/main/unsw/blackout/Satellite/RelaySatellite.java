package unsw.blackout.Satellite;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite{
    private String direction;
    public RelaySatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, "RelaySatellite", height, position, 1500, 300000, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        double degreePosition = position.toDegrees();
        if (degreePosition >= 190 && degreePosition < 345) {
            direction = "negative";
        }
        direction = "positive";
    }

    @Override
    public void move() {
        double degreePosition = position.toDegrees();
        double r = height;
        int v = speed;
        double degree = v / (2 * Math.PI * r) * 360;
        Angle a = Angle.fromDegrees(degree);
        if (direction == "positive") {
            if (degreePosition >= 190 && degreePosition < 345) {
                setPosition(position.subtract(a));
                direction = "negative";
            } else {
                setPosition(position.add(a));
            }
        } else if (direction == "negative") {
            if (degreePosition < 140 || degreePosition >= 345) {
                setPosition(position.add(a));
                direction = "positive";
            } else {
                setPosition(position.subtract(a));
            }
        }
    }
}
