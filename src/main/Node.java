package src.main;

import src.vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String label;
    Vector2 pos;
    ArrayList<Node> adjacentNodes = new ArrayList<Node>();
    Node(String label, Vector2 pos) {
        this.label = label;
        this.pos = pos;
    }
}
