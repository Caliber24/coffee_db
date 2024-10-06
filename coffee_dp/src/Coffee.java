import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Coffee {
    Config config=new Config();
    public void showMainMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            if (!config.ISLOGIN) {
                System.out.println("[1]- LOGIN\n" +
                        "[2]- REGISTER\n" +
                        "[0]- EXIT");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            } else if (config.CurrentUser.IsAdmin) {
                System.out.println("[1]- MANAGE USER\n" +
                        "[2]- MANAGE MATERIALS\n" +
                        "[3]- MANAGE PRODUCT\n" +
                        "[4]- VIEW STATS\n" +
                        "[5]- LOGOUT\n" +
                        "[0]- EXIT");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        config.CurrentUser.manageUsers();
                        break;
                    case 2:
                        config.RawMaterials.manageRawMaterials();
                        break;
                    case 3:
                        config.product.showMenu();
                        break;
                    case 4:
                        Order.showAllStats();
                        break;
                    case 5:

                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return; // خروج از متد
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            } else {
                System.out.println("[1]- VIEW PRODUCTS\n" +
                        "[2]- BUY PRODUCT\n"+
                        "[3]- SEARCH PRODUCT\n" +
                        "[4]- VIEW MY STATS\n" +
                        "[0]- EXIT");

                int choice = scanner.nextInt();
                scanner.nextLine();
                Product product = new Product();

                switch (choice) {
                    case 1:
                        product.viewAllProducts();
                        break;
                    case 2:
                        product.buyProduct(config.CurrentUser);
                        break;
                    case 3:
                        product.searchproduct();
                        break;
                    case 4:
                        Order.showUserStats(config.CurrentUser);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            }
        }
    }

    public void ManageUser() {
        if (config.ISLOGIN && config.CurrentUser.IsAdmin) {
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("[1]- Add USER\n" +
                        "[2]- Search USER\n" +
                        "[3]- Edit USER\n" +
                        "[4]- Remove USER\n" +
                        "[0]- BACK");
                System.out.print("Please enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        config.CurrentUser.addUser();
                        break;
                    case 2:
                        removeUser();
                        break;
                    case 3:
                        addProduct();
                        break;
                    case 4:
                        showStats();
                        break;
                    case 0:
                        showMainMenu();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("#Access Denied. You are not an Admin.");
            showMainMenu();
        }
    }


    public void removeUser() {
        System.out.println("Remove User functionality");
    }

    public void addProduct() {
        System.out.println("Add Product functionality");
    }

    public void showStats() {
        System.out.println("Show Stats functionality");
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your email: ");
        String email = scanner.nextLine();

        System.out.println("enter your password: ");
        String password = scanner.nextLine();

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] userDB = line.split("\\|");
                String emailDB = userDB[2];
                String passDB = userDB[3];
                if (emailDB.equals(email) && passDB.equals(password)) {
                    String uuid = userDB[0];
                    String name = userDB[1];
                    boolean isAdmin = Boolean.parseBoolean(userDB[4]);
                    config.CurrentUser = new User(name, emailDB, passDB, isAdmin, uuid);
                    config.ISLOGIN = true;
                    System.out.println("Login is OK. welcome #" + name);
                    showMainMenu();
                    return;
                }
            }
            System.out.println("email or password is invalid.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your name: ");
        String name = scanner.nextLine();

        System.out.println("enter your email: ");
        String email = scanner.nextLine();

        System.out.println("enter your password: ");
        String password = scanner.nextLine();

        System.out.println("is admin? please enter true or false: ");
        boolean isAdmin = scanner.nextBoolean();
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] userDB = line.split("\\|");
                String emailDB = userDB[2];
                if (emailDB.equals(email)) {
                    System.out.println("#Error, email is already.");
                    showMainMenu();

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            showMainMenu();
        }
        try (FileWriter writer = new FileWriter("src/db/users.txt", true)) {
            String uuid = Config.createUUID();
            writer.write(uuid + "|" + name + "|" + email + "|" + password + "|" + isAdmin + "\n");
            writer.close();
            System.out.println("Registration OK.");
            showMainMenu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
