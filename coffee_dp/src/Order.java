import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;


public class Order {
    private int OrderId;
    private String UserId;
    private int ProductId;
    public static int count_orders = 1;
    Order(String userid, int productid) {
        OrderId = count_orders++;
        UserId = userid;
        ProductId = productid;
    }

    public Order() {
    }

    public static void writeOrderToFile(Order newOrder){
        try (FileWriter writer = new FileWriter("src/db/orders.txt", true)) {
            writer.write(newOrder.UserId + "|" + newOrder.ProductId + "\n" );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showAllStats(){
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/orders.txt"))){
            String line;
            System.out.println("--- Stats List ---");
            while ((line = file.readLine()) != null){
                String[] statsDb = line.split("\\|");
                System.out.println("UserId: "+ statsDb[0]+ ", ProductId: "+ statsDb[1]);
            }
            System.out.println();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public static void showUserStats(User user){
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/orders.txt"))){
            String line;
            System.out.println("--- Stats List ---");
            while ((line = file.readLine()) != null){
                String[] statsDb = line.split("\\|");
                if(user.UUID.equals(statsDb[0])) {
                    System.out.println("UserId: " + statsDb[0] + ", ProductId: " + statsDb[1]);
                }
                }
            System.out.println();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
