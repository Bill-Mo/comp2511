package dungeonmania.util;

import java.util.List;

public class Node {
    private Position position;
    private Boolean visited;
    private List<Node> adjacentPath;
    private Node prevNode;

    public Node(Position position, List<Node> adjacentPath) {
        this.position = position;
        this.visited = false;
        this.adjacentPath = adjacentPath;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public List<Node> getAdjacentPath() {
        return adjacentPath;
    }

    public void setAdjacentPath(List<Node> adjacentPath) {
        this.adjacentPath = adjacentPath;
    }

    public Node getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }

}
