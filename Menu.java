package avltree;

import java.util.List;

/**
 *
 * @author Tikhoglo
 */
public class Menu {
    
    private final AVLTree<String> tree;
    
    public Menu(){
        tree = new AVLTree();
    }
    
    public void insertCommand(String command){
        String[] splittedCommand = command.split(" ");
        
        try {
            if (splittedCommand[0].equals("find"))
                find(splittedCommand);
            else if (splittedCommand[0].equals("insert"))
                insert(splittedCommand);
            else if (splittedCommand[0].equals("remove"))
                remove(splittedCommand);
            else if (splittedCommand[0].equals("size"))
                size();
            else if (splittedCommand[0].equals("print") && splittedCommand.length == 2)
                orderedOutput(splittedCommand[1]);
            else if (splittedCommand[0].equals("print") && splittedCommand[2].equals("rows"))
                printRows(splittedCommand);
            else throw new Exception("Incorrect command");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void find(String[] splittedCommand) {
        System.out.println(this.tree.search(Integer.parseInt(splittedCommand[1])));
    }
    
    private void insert(String[] splittedCommand) {
        String key = splittedCommand[1];
        String value = splittedCommand[2];
        this.tree.insert(Integer.parseInt(key), value);
    }
    
    private void remove(String[] splittedCommand) {
        this.tree.remove(Integer.parseInt(splittedCommand[1]));
    }
    
    private void size() {
        System.out.println(this.tree.getSize());
    }

    private void orderedOutput(String type) throws Exception {
        List<String> list;
        switch(type) {
            case "pre-order":
                list = this.tree.preOrder();
                break;
            case "in-order":
                list = this.tree.inOrder();
                break;
            case "post-order":
                list = this.tree.postOrder();
                break;
            default:
                throw new Exception("Incorrect command");
        }
        
        for (String elem : list){
            if (list.indexOf(elem) != list.size()-1)
                System.out.print(elem + ", ");
            else
                System.out.print(elem);
        }
        System.out.println("");
    }
    
    private void printRows(String[] splittedCommand) {
        this.tree.printRows(Integer.parseInt(splittedCommand[1]));
    }
}
