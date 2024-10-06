import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    public String UUID;
    private String Name;

    private String Email;
    private String Password;
    public boolean IsAdmin;



    public  User(String name,String email,String password,Boolean isadmin,String UUID){
        this.Name=name;
        this.Password=password;
        this.Email=email;
        this.IsAdmin=isadmin;
        this.UUID=UUID;

    }
    public void addUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter user password: ");
        String password = scanner.nextLine();

        System.out.print("Is the user an admin? (true/false): ");
        String isAdmin = scanner.nextLine();

        try {
            FileWriter writer = new FileWriter("src/db/users.txt", true);

            writer.write(Config.createUUID() + "|" + name + "|" + email + "|" + password + "|" + isAdmin);
            writer.write("\n");
            writer.close();

            System.out.println("User added successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "UUID='" + UUID + '\'' +
                "Email='" + Email + '\'' +
                ", name='" + Name + '\'' +
                ", password='" + Password + '\'' +
                ", isAdmin=" + IsAdmin +
                '}';
    }
    public void manageUsers() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Manage USERS ---");
            System.out.println("[1] - VIEW ALL USERS");
            System.out.println("[2] - ADD USER");
            System.out.println("[3] - EDIT USER");
            System.out.println("[4] - DELETE USER");
            System.out.println("[5] - SEARCH USER");
            System.out.println("[0] - Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    editUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    searchuser();
                case 0:
                    return;
                default:
                    System.out.println("please try again.");
            }
        }
    }

    public void viewUsers() {
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt"))) {
            String line;
            System.out.println("\n--- User List ---");
            while ((line = file.readLine()) != null) {
                String[] user = line.split("\\|");
                System.out.println(" Name: " + user[1] + ", Email: " + user[2] + ", isAdmin: " + user[4]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void editUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the email of the user to edit: ");
        String email = scanner.nextLine();

        List<String> users = new ArrayList<>();
        boolean found = false;

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] user = line.split("\\|");

                if (user.length >= 5 && user[2].equals(email)) {
                    found = true;
                    System.out.println("User: " + user[1] + " | Email: " + user[2] + ")");

                    System.out.println("Please enter new name:");
                    user[1] = scanner.nextLine();

                    System.out.println("Please enter new email:");
                    user[2] = scanner.nextLine();

                    System.out.println("Please enter new password: ");
                    user[3] = scanner.nextLine();

                    System.out.println("Please enter new role: is admin? true or false: ");
                    user[4] = scanner.nextLine();

                    users.add(String.join("|", user));
                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("User not found.");
            return;
        }
        try (FileWriter writer = new FileWriter("src/db/users.txt", false)) {

            for (String userLine : users) {
                writer.write(userLine + "\n");
                System.out.println("User: " + userLine + "\n");
            }
            writer.close();
            System.out.println("User updated.");
            manageUsers();
        } catch (IOException e) {
            System.out.println("Error writing to users file: " + e.getMessage());
        }
    }


    public void deleteUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("please enter email  user to delete: ");
        String email = scanner.nextLine();

        List<String> users = new ArrayList<>();
        boolean found = false;

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] user = line.split("\\|");
                if (user[2].equals(email)) {
                    found = true;
                    System.out.println("User #" + user[1] + " deleted.");

                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println( e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("User not found.");
            return;
        }

        try (FileWriter writer = new FileWriter("src/db/users.txt", false);) {
            for (String user : users) {
                writer.write(user + "\n");
            }

        } catch (IOException e) {
            System.out.println( e.getMessage());
        }

        return;
    }

    public static void searchuser(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Search User by Email: ");
        String search = scanner.next();
        boolean found = false;
        try(BufferedReader file = new BufferedReader(new FileReader("src/db/users.txt")) ){
            String line;
            while ((line = file.readLine()) != null){
                String[] user = line.split("\\|");
                if(search.equals(user[2])){
                    found = true;
                    System.out.println("Code: " + user[0]+ ", Name: " + user[1] + ", email: " + user[2]);
                }
            }
            if(!found){
                System.out.println("---> User is not available !!");
            }
        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
    }

}


