package relaxproject;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws Exception {
        //testing stuff but keep tablelist
        TableList tl = new TableList();
        tl.addTableFromFile("src\\sample.csv","students1");
        tl.addTableFromFile("src\\sample2.csv","students2");
        tl.addTableFromFile("src\\sample3.csv","students3");


        //main input loop
        String selected = "students1";
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.print(selected+"> ");
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){ break; }
            //add a table from file
            else if(input.startsWith("add")){
                String[] brokenLine = input.split(" ");
                tl.addTableFromFile(brokenLine[1], brokenLine[2]);
            } 
            else if(input.equals("help")){
                System.out.println(tl.getHelp());
            }
            else if(input.equals("list")){
                System.out.println(tl.listNames());
            }
            else if(input.equals("display")){
                System.out.println(tl.getByName(selected));
            }
            else if(input.startsWith("display")){
                System.out.println(tl.getByName(input.split(" ")[1]));
            }
            else if(input.startsWith("table")){
                String toSelect = input.split(" ")[1];
                if(tl.hasTable(toSelect)){ selected = toSelect; }
            }
            else if(input.startsWith("set-name")){
                String newName = input.split(" ")[1];
                if(tl.changeName(selected, newName)){
                    selected = newName;
                }
            }
            else if(input.equals("export")){
                System.out.print("Are you sure you want to export "+selected+" to a csv file? (this will overwrite) (y/n) > ");
                input = scanner.nextLine();
                if(input.equalsIgnoreCase("y")){
                    boolean exported = tl.exportTable(selected);
                    System.out.println("Saved to file = "+exported);
                } else{ System.out.println("Aborted, no file has been saved");}
            }
            else {
                Table t2 = tl.getQuery(input, selected);
                if(t2 == null){
                    System.out.println("ERROR: something went wrong in the query, please check your query and try again");
                } else {
                    System.out.println("Table "+t2.getName()+" successfully created, switch to this table using the 'table' commmand. Now outputting the new table: ");
                    System.out.println(t2);
                    tl.addTable(t2);
                }
            }
        }
        scanner.close();
    }

    
}
