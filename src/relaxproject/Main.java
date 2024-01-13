package relaxproject;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class Main {
    public static void main(String[] args) throws Exception {
        Table table = getFile("src\\sample.csv");
        //System.out.println(table.toString());
        ArrayList<String> newCats = new ArrayList<>();
        newCats.add("Name");
        ArrayList<String> newCats2 = new ArrayList<>();
        newCats2.add("Gender");
        Table cart1 = table.projection(newCats);
        Table cart2 = table.projection(newCats2);
        Table cartprod = cart1.cartesian(cart2);
        Table age = table.selection("Age",">=","12");
        System.out.println(age.toString());
        System.out.println(cartprod.toString());
        //main input loop
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(!input.equalsIgnoreCase("exit")){
            System.out.print("> ");
            input = scanner.next();
            System.out.println("you entered: "+ input);
        }
        scanner.close();
    }

    private static Table getFile(String filepath) throws IOException{
        BufferedReader r = new BufferedReader(new FileReader(filepath));
        String line = r.readLine();
        String[] categories = line.split(",");
        Table table = new Table(new ArrayList<>(Arrays.asList(categories)));
        while((line = r.readLine()) != null){
            table.addRow(line.split(","));
        }
        r.close();
        return table;
    }
}
