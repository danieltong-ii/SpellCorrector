package spell;

public class Trie implements ITrie{

    //private data in TrieMap; only thing it needs is the single node
    private Node root;
    int wordCount = 0;
    int nodeCount = 1;

    public Trie() {
        root = new Node();
    }

    @Override
    public void add(String word) {
        Node current = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int pos = c - 'a';
            INode[] currentChildren = current.getChildren();

            if (currentChildren[pos] == null) {
                //return childNode and set to current
                currentChildren[pos] = new Node();
                current = (Node) currentChildren[pos];
                nodeCount++;
            }
            else {
                current = current.getChild(pos);
            }
        }
        current.incrementValue();
        if (current.getValue() == 1) {
            wordCount++;
        }
    }

    @Override
    public INode find(String word) {
        Node current = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int pos = c - 'a';
            current = current.getChild(pos);

            if (current == null) {
                return null;
            }
        }
        if (current.getValue() == 0) {
            return null;
        }
        return current;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public boolean equals(Object obj) {
        Trie other = (Trie)obj;

        if (other.getWordCount() != this.wordCount) {
            return false;
        }
        if (other.getNodeCount() != this.nodeCount) {
            return false;
        }
        return equals_helper(this.root, other.root);
    }

    private boolean equals_helper(INode a, INode b) {
        // compare counts
        if (a.getValue() != b.getValue()) {
            return false;
        }
        // compare child locations
        INode[] aChildren = a.getChildren();
        INode[] bChildren = b.getChildren();

        for (int i = 0; i < aChildren.length; i++) {
            if ((aChildren[i] != null) && (bChildren[i] != null)) {
                // call equals equals_helper on each pair of children

                if (!(equals_helper(aChildren[i], bChildren[i]))) {
                    return false;
                }
            }
            else if ((aChildren[i] != null) && (bChildren[i] == null)){
                return false;
            }
            else if ((aChildren[i] == null) && (bChildren[i] != null)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = nodeCount * wordCount;
        INode[] children = root.getChildren();

        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                hash = hash + i;
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        StringBuilder curWord = new StringBuilder();
        toString_Helper(root, curWord, output);
        return String.valueOf(output);
    }

    private void toString_Helper(Node n, StringBuilder curWord, StringBuilder output) {
        INode[] currentChildren = n.getChildren();
        if (n.count > 0) {
            //Append this node's word to the output
            output.append(curWord.toString());
            output.append("\n");
        }
        for (int i = 0; i < currentChildren.length; i++) {

            if (currentChildren[i] != null) {

                char childLetter = (char)('a' + i);
                curWord.append(childLetter);
                toString_Helper((Node) currentChildren[i], curWord, output);

                curWord.deleteCharAt(curWord.length()-1);
            }
        }
    }
}
