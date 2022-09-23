package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Position;

public class Spider extends Mob implements Enemies {

    private int moveCycle;
    private boolean dirClockwise;
    private int delayMovementCount;

    public Spider(String id, String type, Position position, boolean isInteractable, int health, int attack) {
        super(id, type, position, isInteractable, health, attack);
        moveCycle = 0;
        dirClockwise = true;
        delayMovementCount = 0;
    }

    @Override
    public void move(List<Entity> entities, Character character) {
        
        Position curPos = this.getPosition();
        Position newPos = null;

        //check character state
        if (character.getStateName().equals("Invincible")) {
            //runs away
            Position charPos = character.getPosition();

            if (charPos.getY() > curPos.getY()) { //character is below the spider
                newPos = new Position(curPos.getX(), curPos.getY()-1);
                this.setPosition(newPos);
            } else if (charPos.getY() < curPos.getY()) { //character is above the spider
                newPos = new Position(curPos.getX(), curPos.getY()+1);
                this.setPosition(newPos);
            } else if (charPos.getX() > curPos.getX()) { //character is on the right of the spider 
                newPos = new Position(curPos.getX()-1, curPos.getY());
                this.setPosition(newPos);
            } else if (charPos.getX() <= curPos.getX()) { //character is on the left of the spider
                newPos = new Position(curPos.getX()+1, curPos.getY());
                this.setPosition(newPos);
            }
        } else {
            if (dirClockwise == true) {
                if (moveCycle == 0 || moveCycle == 6 || moveCycle == 7) { //UP
                    newPos = new Position(curPos.getX(), curPos.getY()-1);
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 1 || moveCycle == 8) { //RIGHT
                    newPos = new Position(curPos.getX()+1, curPos.getY());
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 2 || moveCycle == 3) { //DOWN
                    newPos = new Position(curPos.getX(), curPos.getY()+1);
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 4 || moveCycle == 5) { //LEFT
                    newPos = new Position(curPos.getX()-1, curPos.getY());
                    checkBoulder(entities, newPos, character);
                }
            } else {
                if (moveCycle == 0 || moveCycle == 7 || moveCycle == 8) { //DOWN
                    newPos = new Position(curPos.getX(), curPos.getY()+1);
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 1 || moveCycle == 2) { //LEFT
                    newPos = new Position(curPos.getX()-1, curPos.getY());
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 3 || moveCycle == 4) { //UP
                    newPos = new Position(curPos.getX(), curPos.getY()-1);
                    checkBoulder(entities, newPos, character);
                } else if (moveCycle == 5 || moveCycle == 6) { //RIGHT
                    newPos = new Position(curPos.getX()+1, curPos.getY());
                    checkBoulder(entities, newPos, character);
                }       
            }
        }

        if (this.isOn(character)) {
            character.battle(this);
        }
    }

    /**
     * Checks if a boulder is in a position
     * if true then change direcion
     * if false then sets spider position to new position
     * @param entities
     * @param position
     */
    public void checkBoulder(List<Entity> entities, Position position, Character character) {
        boolean setPos = true;
        for (Entity entity : new ArrayList<>(entities)) {

            Position entPos = entity.getPosition();

            if (entity.getType().equals("boulder") && position.equals(entPos)) {
                changeDirection();
                setPos = false;
                move(entities, character);
            }
        }
        if (setPos == true) {
            this.setPosition(position);
            if (dirClockwise) {
                if (moveCycle == 8) {
                    moveCycle = 1;
                } else {
                    moveCycle++;
                }
            } else {
                if (moveCycle == 1) {
                    moveCycle = 8;
                } else {
                    moveCycle--;
                }
            }


        }
        
    }

    /**
     * change direction of spider
     */
    public void changeDirection() {
        if (dirClockwise == true) {
            dirClockwise = false;
        } else {
            dirClockwise = true;
        }
    }

    public int getDelayMovementCount() {
        return delayMovementCount;
    }

    public void setDelayMovementCount(int delayMovementCount) {
        this.delayMovementCount = delayMovementCount;
    }

    @Override
    public boolean canMove() {
        setDelayMovementCount(getDelayMovementCount() - 1);

        if (getDelayMovementCount() < 0) {
            return true;
        }
        return false;
    }

    @Override
    public void isOnSwampTile(ArrayList<SwampTile> swampTilePosition) {
        int currentX = getPosition().getX();
        int currentY = getPosition().getY();
        for (SwampTile sw : swampTilePosition) {
            Position p = sw.getPosition();
            if (p.getX() == currentX && p.getY() == currentY) {
                setDelayMovementCount(sw.getMovementFactor());
            }
        }
    }
}

