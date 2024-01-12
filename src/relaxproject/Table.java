package relaxproject;
import java.util.ArrayList;
import java.util.HashMap;
public class Table {
    private ArrayList<HashMap<String,String>> table;
    private ArrayList<String> categories;

    public Table(ArrayList<String> categories){
        this.categories = categories;
        table = new ArrayList<HashMap<String,String>>();
    }

    private ArrayList<HashMap<String,String>> getTable(){ return table; }
    public ArrayList<String> getCategories(){ return categories; }

    public boolean addRow(String[] newRow){
        if(newRow.length != categories.size()){ return false; } //return false if they don't add up
        HashMap<String,String> rowMap = new HashMap<>(); //new hashmap to add
        //initialize the new row as a map
        for(int i = 0; i < categories.size(); i++){
            rowMap.put(categories.get(i),newRow[i]);
        }
        table.add(rowMap);
        return true;
    }
    public boolean addRow(HashMap<String,String> newRow){
        table.add(newRow);
        return true;
    }

    public Table projection(ArrayList<String> newCategories){
        Table projTable = new Table(newCategories);
        for(HashMap<String,String> row : getTable()){
            HashMap<String,String> newRow = new HashMap<>();
            for(String category : newCategories){
                newRow.put(category,row.get(category));
            }
            projTable.addRow(newRow);
        }
        return projTable;
    }

    public String toString(){
        String output = "";
        //list of categories
        for(String category : categories){
            output+=(category+",");
        }
        output+="\n";
        for(HashMap<String,String> row : table){
            for(String entry : row.values()){
                output+=(entry+",");
            }
            output+="\n";
        }
        return output;
    }
}
