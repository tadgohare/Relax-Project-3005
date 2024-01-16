package relaxproject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * A collection of Table objects
 */
public class TableList {
    private ArrayList<Table> tables;
    private static int id = 0;

    /**
     * Constructor that initalizes an empty TableList
     */
    public TableList(){
        tables = new ArrayList<>();
    }
    /**
     * Gets the size of the collection
     * @return integer val of how many elements in the collection
     */
    public int size(){
        return tables.size();
    }

    /**
     * if table is empty or not
     * @return true or false
     */
    public boolean isEmpty(){
        return size() == 0;
    }

    /**
     * Adds a table into the collection
     * @param table the table object to be added
     * @return true if no tables match name and can be added.
     */
    public boolean addTable(Table table){
        if(hasTable(table.getName()) || table == null || table.getName().equals("")){
            return false; 
        }
        else {
            tables.add(table);
            return true;
        }
    }

    /**
     * Removes a table from the collection
     * @param name name of table to be removed
     * @return if the table was successfully removed or not
     */
    public boolean removeTable(String name){
        return tables.remove(getByName(name));
    } 

    /**
     * Gets a table from the collection using the name
     * @param name the name of the table to find
     * @return the table if found, null if not found
     */
    public Table getByName(String name){
        for(Table t : tables){
            if(name.equals(t.getName())){ return t; }
        }
        return null;
    }

    /**
     * Checks to see if the specified table exists
     * @param name name of table to check
     * @return true or false
     */
    public boolean hasTable(String name){
        for(Table t : tables){
            if(name.equals(t.getName())) { return true; }
        }
        return false;
    }

    /**
     * gets a string of all the names of tables
     * @return
     */
    public String listNames(){
        String output = "LISTING TABLES\n";
        for(Table table : tables){
            output += table.getName() + "\n";
        }
        output += "END OF LIST\n";
        return output;
    }

    /**
     * Checks conditions and switches name of table
     * @param currName name of table to update
     * @param newName new chosen name of table
     * @return whether the function succeeded or failed in the name change.
     */
    public boolean changeName(String currName, String newName){
        if(!hasTable(newName) && hasTable(currName)){
            getByName(currName).setName(newName);
            return true;
        } else { return false; }
    }


    public Table getQuery(String query, String name){
        Table table = getByName(name);
        if(table == null){
            System.out.println("TableList ERROR: TABLE DOES NOT EXIST");
        }
        String[] qElets = query.split(" ");
        for(int i = qElets.length-2; i >= 0; i-=2){
            switch(qElets[i]){
                case "pi":
                    String[] params = qElets[i+1].split(",");
                    table = table.projection(params);
                    table.setName("newtable_"+id++);
                    break;
                case "sigma":
                    String[] sigparams = qElets[i+1].split(",");
                    table = table.selection(sigparams[0],sigparams[1],sigparams[2]);
                    break;
                case "cart":
                    Table toCart = getByName(qElets[i+1]);
                    table = table.cartesian(toCart);
                    break;
                case "natjoin":
                    Table toJoin = getByName(qElets[i+1]);
                    table = table.naturalJoin(toJoin);
                    break;
                case "intersect":
                    Table toIntersect = getByName(qElets[i+1]);
                    table = table.intersect(toIntersect);
                    break;
                default:
                    return null;
            }
        }
        return table;
    }

    /**
     * Gets the help options for the console
     * @return String value of the help
     */
    public String getHelp(){
        String output = "List of commands for DBMS project 3005: \n";
        output+="-=Relational Algebra Operators=-\n";
        output+="pi <category1>,<category2>,...,<categoryn> - takes a projection of the table with new values\n";
        output+="sigma <key>,<operator>,<value> - returns a table filtering values specified in command\n";
        output+="cart <table name to merge> - takes the cartesian product of the two tables\n";
        output+="natjoin <table name to join> - does a natural join operation on the tables\n";
        output+="intersect <table to intersect> - does the intersect operator on two tables\n";

        output+="Other commands (will not be able to combine with relational algebra commands)\n";
        output+="add <filepath> <name> - loads a table in from a .csv file\n";
        output+="table <table name> - switches table to be selected\n";
        output+="list - lists all the names of the tables\n";
        output+="display <tableName> - prints out the specified table to the console, if none specified, prints the selected table.\n";
        output+="set-name <new name> - sets a new name of the table currently selected\n";
        output+="exit - ends the program lifecycle\n";

        return output;
    }

    /**
     * adds a table to the collection from csv file
     * @param filepath the path to the csv file
     * @param name the name of the table to add
     * @return whether or not the table was successfully added.
     */
    public boolean addTableFromFile(String filepath, String name) {
        try {
            BufferedReader r = new BufferedReader(new FileReader(filepath));
            String line = r.readLine();
            String[] categories = line.split(",");
            Table table = new Table(new ArrayList<>(Arrays.asList(categories)),name);
            while((line = r.readLine()) != null){
                table.addRow(line.split(","));
            }
            tables.add(table);
            r.close();
            return true;
        } catch(Exception e){
            System.out.println("ERROR: FAILED TO READ FILE");
            return false;
        }
    }
}
