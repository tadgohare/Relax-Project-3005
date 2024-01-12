package relaxproject;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class Main {
    public static void main(String[] args) throws Exception {
        Table table = getFile("src\\organizations-100.csv");
        //System.out.println(table.toString());
        ArrayList<String> newCats = new ArrayList<>();
        newCats.add("Index");
        newCats.add("Website");
        Table indexs = table.projection(newCats);
        System.out.println(indexs.toString());
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
