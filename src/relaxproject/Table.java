package relaxproject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Table object that represents a table of data
 */
public class Table {
    private ArrayList<LinkedHashMap<String,String>> table;
    private ArrayList<String> categories;
    private String name;

    /**
     * Constructor that has predefined categories and a name
     * @param categories ArrayList of strings that represent each category
     * @param name The name of the table
     */
    public Table(ArrayList<String> categories, String name){
        this.categories = categories;
        table = new ArrayList<LinkedHashMap<String,String>>();
        this.name = name;
    }
    /**
     * Constructor that has categories but an undefined table name (might remove)
     * @param categories List of strings corresponding to each category
     */
    public Table(ArrayList<String> categories){
        this(categories,"");
    }
    /**
     * Creates an empty table object
     */
    public Table(){
        this(new ArrayList<String>(),"");
    }

    protected ArrayList<LinkedHashMap<String,String>> getTable(){ return table; }
    /**
     * gets the categories
     * @return ArrayList of strings that are the categories
     */
    public ArrayList<String> getCategories(){ return categories; }

    /**
     * get method for name
     * @return string value of name
     */
    public String getName(){ return name; }

    /**
     * sets a new name for the table
     * @param newName the new name string to set the name to
     */
    protected void setName(String newName){
        name = newName;
    }

    /**
     * checks if the table has a category (case sensitive)
     * @param category the string value of the category to check for
     * @return whether or not the category exists
     */
    protected boolean hasCategory(String category){
        return categories.contains(category);
    }

    /**
     * Adds a row to the table
     * @param newRow array of strings to add which correspond to a category
     * @return whether the row was added successfully or not
     */
    public boolean addRow(String[] newRow){
        if(newRow.length != categories.size()){ return false; } //return false if they don't add up
        LinkedHashMap<String,String> rowMap = new LinkedHashMap<>(); //new hashmap to add
        //initialize the new row as a map
        for(int i = 0; i < categories.size(); i++){
            rowMap.put(categories.get(i),newRow[i]);
        }
        table.add(rowMap);
        return true;
    }
    /**
     * adds a new row to the table that is already in map form
     * @param newRow LinkedHashMap that represents the row
     * @return whether it was successfully added
     */
    public boolean addRow(LinkedHashMap<String,String> newRow){
        table.add(newRow);
        return true;
    }
    /**
     * Sigma relational algebra operator that selects rows that satisfy a certain condition
     * @param key the category that will be checked
     * @param selection the operator used
     * @param value the value used in the condition
     * @return the new table that meets the specified conditions, null if unsuccessfull
     */
    public Table selection(String key, String selection, String value){
        if(!hasCategory(key)){ return null; }
        Table t = new Table(getCategories(),getName());
        switch(selection){
            case ">=":
                for(LinkedHashMap<String,String> row : table){
                    if(Double.parseDouble(row.get(key)) >= Double.parseDouble(value)){
                        t.addRow(row);
                    }
                }
                break;
            case "<=":
                for(LinkedHashMap<String,String> row : table){
                    if(Double.parseDouble(row.get(key)) <= Double.parseDouble(value)){
                        t.addRow(row);
                    }
                }
                break;
            case "=":
                for(LinkedHashMap<String,String> row : table){
                    if(row.get(key).equals(value)){
                        t.addRow(row);
                    }
                }
                break;
            default:
                return null;
        }
        return t;
    }

    /**
     * Pi relational algebra operator that projects new table with certain categories
     * @param newCategories ArrayList of categories to be included in the new table
     * @return the newly formed table, null if something goes wrong.
     */
    public Table projection(ArrayList<String> newCategories){
        //check if has all categories
        for(String category : newCategories){
            if(!hasCategory(category)){ return null; }
        }
        Table projTable = new Table(newCategories,getName());
        for(LinkedHashMap<String,String> row : getTable()){
            LinkedHashMap<String,String> newRow = new LinkedHashMap<>();
            for(String category : newCategories){
                newRow.put(category,row.get(category));
            }
            projTable.addRow(newRow);
        }
        return projTable;
    }

    /**
     * Does a projection by calling other method
     * @param newCategories Primitive array of strings representing the new categories
     * @return whether the projection was successful or not.
     */
    public Table projection(String[] newCategories){
        ArrayList<String> al = new ArrayList<String>();
        al.addAll(Arrays.asList(newCategories));
        return projection(al);
    }

    /**
     * Takes the cartesian product of two different tables
     * @param t2 second table to do cartesian product with
     * @return a table with the cartesian product if successful.
     */
    public Table cartesian(Table t2){
        ArrayList<String> newCategories = new ArrayList<>(categories);
        newCategories.addAll(t2.getCategories());
        Table newTable = new Table(newCategories,getName());
        for(LinkedHashMap<String,String> row : table){
            for(LinkedHashMap<String,String> t2row : t2.getTable()){
                LinkedHashMap<String,String> toAdd = new LinkedHashMap<>(row);
                toAdd.putAll(t2row);
                newTable.addRow(toAdd);
            }
        }
        return newTable;
    }
    /**
     * does a natural join operation on two tables
     * @param t2 the second table to do the natural join with
     * @return a new table after the natural join, cartesian product if the right conditions aren't met.
     */
    public Table naturalJoin(Table t2){
        //check for common categories
        ArrayList<String> commonCats = new ArrayList<>();
        for(String cat1 : categories){
            for(String cat2 : t2.getCategories()){
                if(cat1.equals(cat2)){ commonCats.add(cat1); }
            }
        }

        //make new categories list
        if(commonCats.isEmpty()){ return cartesian(t2); }
        ArrayList<String> newCats = new ArrayList<>(getCategories());
        for(String category : t2.getCategories()){
            if(!newCats.contains(category)){ newCats.add(category); }
        }
        //combine the two tables
        Table newTable = new Table(newCats);
        for(LinkedHashMap<String,String> row1 : table){
            for(LinkedHashMap<String,String> row2 : t2.getTable()){
                boolean toAdd = true;
                for(String category : commonCats){
                    if(!row1.get(category).equals(row2.get(category))){ toAdd = false; }
                }
                if(toAdd){
                    LinkedHashMap<String,String> newRow = new LinkedHashMap<>(row1);
                    newRow.putAll(row2);
                    newTable.addRow(newRow);
                }
            }
        }
        return newTable;
    }

    /**
     * Set intersect operator
     * @param t2 secondary table
     * @return the newly formed table
     */
    public Table intersect(Table t2){
        if(!categories.equals(t2.getCategories())){ return null; }
        Table newTable = new Table(categories);
        for(LinkedHashMap<String,String> row : table){
            for(LinkedHashMap<String,String> row2 : t2.getTable()){
                if(row.equals(row2)){ newTable.addRow(row); }
            }
        }
        return newTable;
    }

    public String toString(){
        String output = getName() + "\n";
        //list of categories
        for(String category : categories){
            output+=(category+",");
        }
        output = output.substring(0, output.length() - 1); // Remove last comma
        output+="\n";
        for(LinkedHashMap<String,String> row : table){
            for(String entry : row.values()){
                output+=(entry+",");
            }
            output = output.substring(0, output.length() - 1); // Remove last comma
            output+="\n";
        }
        return output;
    }
}