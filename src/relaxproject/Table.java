package relaxproject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
public class Table {
    private ArrayList<LinkedHashMap<String,String>> table;
    private ArrayList<String> categories;

    public Table(ArrayList<String> categories){
        this.categories = categories;
        table = new ArrayList<LinkedHashMap<String,String>>();
    }

    protected ArrayList<LinkedHashMap<String,String>> getTable(){ return table; }
    public ArrayList<String> getCategories(){ return categories; }

    private boolean hasCategory(String category){
        return categories.contains(category);
    }

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
    public boolean addRow(LinkedHashMap<String,String> newRow){
        table.add(newRow);
        return true;
    }

    public Table selection(String key, String selection, String value){
        if(!hasCategory(key)){ return null; }
        Table t = new Table(getCategories());
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

    public Table projection(ArrayList<String> newCategories){
        //check if has all categories
        for(String category : newCategories){
            if(!hasCategory(category)){ return null; }
        }
        Table projTable = new Table(newCategories);
        for(LinkedHashMap<String,String> row : getTable()){
            LinkedHashMap<String,String> newRow = new LinkedHashMap<>();
            for(String category : newCategories){
                newRow.put(category,row.get(category));
            }
            projTable.addRow(newRow);
        }
        return projTable;
    }

    public Table cartesian(Table t2){
        ArrayList<String> newCategories = new ArrayList<>(categories);
        newCategories.addAll(t2.getCategories());
        Table newTable = new Table(newCategories);
        for(LinkedHashMap<String,String> row : table){
            for(LinkedHashMap<String,String> t2row : t2.getTable()){
                LinkedHashMap<String,String> toAdd = new LinkedHashMap<>(row);
                toAdd.putAll(t2row);
                newTable.addRow(toAdd);
            }
        }
        return newTable;
    }

    public String toString(){
        String output = "";
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