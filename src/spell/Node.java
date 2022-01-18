package spell;

import java.lang.reflect.Array;


public class Node implements INode {

    //Data members of a Node
    private static int CHAR_SIZE = 26;
    int count;
    private Node[] children;

    //Constructor
    public Node() {
        count = 0;
        children = new Node[CHAR_SIZE];
    }


    public Node getChild(int pos) {
        return (Node) Array.get(children, pos);
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;

    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
