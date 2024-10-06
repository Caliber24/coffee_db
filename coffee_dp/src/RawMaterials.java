import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RawMaterials {
    public int Code;
    public String Material;
    public int Price;

    public RawMaterials(int code, String material, int price) {
        this.Code = code;
        this.Material = material;
        this.Price = price;
    }

    public RawMaterials() {

    }

    public void addRawMaterial() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter material code: ");
        int code = scanner.nextInt();
        scanner.nextLine();

        if (findeCode(code)) {
            System.out.println("code is already used.");
            return;
        }

        System.out.print("please Enter material name: ");
        String material = scanner.nextLine();

        System.out.print("please Enter material price: ");
        int price = scanner.nextInt();
        scanner.nextLine();

        try {
            FileWriter writer = new FileWriter("src/db/raw.txt", true);
            writer.write(code + "|" + material + "|" + price);
            writer.write("\n");
            writer.close();

            System.out.println("Raw material added.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public boolean findeCode(int code) {
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/raw.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] rawMaterial = line.split("\\|");
                int existingCode = Integer.parseInt(rawMaterial[0]);
                if (existingCode == code) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void viewRawMaterials() {
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/raw.txt"))) {
            String line;
            System.out.println("\n--- Raw Materials List ---");
            while ((line = file.readLine()) != null) {
                String[] rawMaterial = line.split("\\|");
                System.out.println("Code: " + rawMaterial[0] + ", Material: " + rawMaterial[1] + ", Price: " + rawMaterial[2]);
            }
        } catch (IOException e) {
            System.out.println( e.getMessage());
        }
    }

    public void editRawMaterial() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Enter the material code to edit: ");
        int code = scanner.nextInt();
        scanner.nextLine();

        List<String> rawMaterials = new ArrayList<>();
        boolean found = false;

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/raw.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] rawMaterial = line.split("\\|");

                if (Integer.parseInt(rawMaterial[0]) == code) {
                    found = true;
                    System.out.println("Material: " + rawMaterial[1] + " | Price: " + rawMaterial[2]);

                    System.out.print("Please enter new material name: ");
                    rawMaterial[1] = scanner.nextLine();

                    System.out.print("Please enter new price: ");
                    rawMaterial[2] = String.valueOf(scanner.nextInt());
                    scanner.nextLine();

                    rawMaterials.add(String.join("|", rawMaterial));
                } else {
                    rawMaterials.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Material not found.");
            return;
        }

        try (FileWriter writer = new FileWriter("src/db/raw.txt", false)) {
            for (String rawMaterialLine : rawMaterials) {
                writer.write(rawMaterialLine + "\n");
            }
            System.out.println("Material updated successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteRawMaterial() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter the material code to delete: ");
        int code = scanner.nextInt();
        scanner.nextLine();

        List<String> rawMaterials = new ArrayList<>();
        boolean found = false;

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/raw.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] rawMaterial = line.split("\\|");

                if (Integer.parseInt(rawMaterial[0]) == code) {
                    found = true;
                    System.out.println("Material #" + rawMaterial[1] + " deleted.");
                } else {
                    rawMaterials.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Material not found.");
            return;
        }

        try (FileWriter writer = new FileWriter("src/db/raw_materials.txt", false)) {
            for (String rawMaterial : rawMaterials) {
                writer.write(rawMaterial + "\n");
            }
            System.out.println("Material deleted successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void manageRawMaterials() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Manage Raw Materials ---");
            System.out.println("[1] - VIEW ALL MATERIALS");
            System.out.println("[2] - ADD MATERIAL");
            System.out.println("[3] - EDIT MATERIAL");
            System.out.println("[4] - DELETE MATERIAL");
            System.out.println("[0] - Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewRawMaterials();
                    break;
                case 2:
                    addRawMaterial();
                    break;
                case 3:
                    editRawMaterial();
                    break;
                case 4:
                    deleteRawMaterial();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("please try again.");
            }
        }
    }
}
