import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class Product {
    public int Code;
    public String ProductName;
    public int ProductPrice;
    public List<Integer> RawMaterialsCode = new ArrayList<>();
    public static int count_product = 1;
    public Product(int Cd,String productname, int productprice, List<Integer> rawmaterialsCode) {
        this.Code=Cd;
        this.ProductName = productname;
        this.ProductPrice = productprice;
        this.RawMaterialsCode = rawmaterialsCode;
    }
    public Product(){

    }
    public  void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Product Management Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Remove Product");
            System.out.println("5. Exit");
            System.out.print("Please choose an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();



            switch (choice) {
                case 1:
                   addProduct();
                    break;
                case 2:
                    viewAllProducts();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    removeProduct();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("invalid, please try again.");
            }
        }

    }
    public  void writeProductToFile(Product product) {
        try (FileWriter writer = new FileWriter("src/db/products.txt", true)) {
            writer.write(product.Code + "|" + product.ProductName + "|" + product.ProductPrice + "|");
            if (!product.RawMaterialsCode.isEmpty()) {
                StringJoiner joiner = new StringJoiner(",");
                for (int code : product.RawMaterialsCode) {
                    joiner.add(String.valueOf(code));
                }
                writer.write(joiner.toString());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewAllProducts() {
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt"))) {
            String line;
            System.out.println("--- Product List ---");
            while ((line = file.readLine()) != null) {
                String[] productDb = line.split("\\|");
                System.out.println("Code: " + productDb[0] + ", Name: " + productDb[1] + ", Price: " + productDb[2]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void addProduct() {

        Scanner scanner = new Scanner(System.in);
            System.out.print("please enter product code: ");
            int productCode = scanner.nextInt();
            scanner.nextLine();

            if (!findCode(productCode)) {
                System.out.println("code has been used.");
                return;
            }
            System.out.print("please enter product name: ");
            String productName = scanner.nextLine();

            System.out.print("please enter product price: ");
            int productPrice = scanner.nextInt();
            scanner.nextLine();

            List<Integer> rawMaterialsCodes = new ArrayList<>();
            System.out.print("Do you add raw materials? Y/N: ");
            String isMaterials = scanner.nextLine();

            if (isMaterials.equals("Y")) {
                List<RawMaterials> findedMaterials = readRawMaterialsFile();

                if (findedMaterials.isEmpty()) {
                    System.out.println("there are not any raw materials");
                } else {
                    System.out.println("--- Available Raw Materials ---");
                    for (RawMaterials material : findedMaterials) {
                        System.out.println("#" + material.Code + " | " + material.Material + " | " + material.Price);
                    }

                    while (true) {
                        System.out.print("please enter raw material code or Exit:");
                        String input = scanner.nextLine();
                        if (!input.equals("Exit")) {
                            break;
                        }

                        try {
                            int code = Integer.parseInt(input);
                            boolean exists = false;
                            for (RawMaterials material : findedMaterials) {
                                if (material.Code == code) {
                                    rawMaterialsCodes.add(code);
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                System.out.println("please try again.");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("please enter only number or Exit");
                        }
                    }
                }

            }
            Product newProduct = new Product(productCode, productName, productPrice, rawMaterialsCodes);
            writeProductToFile(newProduct);

            System.out.println("Product added/updated...");

        }


    public List<RawMaterials> readRawMaterialsFile() {
        List<RawMaterials> rawMaterialsList = new ArrayList<>();

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/raw.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] rawMaterialData = line.split("\\|");
                    int code = Integer.parseInt(rawMaterialData[0]);
                    String material = rawMaterialData[1];
                    int price = Integer.parseInt(rawMaterialData[2]);
                    RawMaterials rawMaterial = new RawMaterials(code, material, price);
                    rawMaterialsList.add(rawMaterial);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return rawMaterialsList;
    }
    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("please enter product Code: ");
        int productCode = scanner.nextInt();
        scanner.nextLine();
        List<String> products = new ArrayList<>();
        boolean found = false;
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] productDb = line.split("\\|");
                int Code = Integer.parseInt(productDb[0]);

                if (Code == productCode) {
                    found = true;
                    System.out.println("Product #" + productDb[1] + "removed.");
                }else {
                products.add(line);
            }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Product not found.");
            return;
        }

        try (FileWriter writer = new FileWriter("src/db/products.txt", false)) {
            for (String product : products) {
                writer.write(product + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("please enter product Code: ");
        int productCODE = scanner.nextInt();
        scanner.nextLine();

        List<String> products = new ArrayList<>();
        boolean found = false;

        try (BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] productDb = line.split("\\|");
                int code = Integer.parseInt(productDb[0]);
                if (code == productCODE) {
                    System.out.println("prodcut: " + productDb[1] + " | Price: " + productDb[2]);

                    found = true;
                    System.out.print("please enter new Product Name: ");
                    String newProductName = scanner.nextLine();
                    System.out.print("please new Product Price: ");
                    int newProductPrice = scanner.nextInt();
                    scanner.nextLine();
                    productDb[1] = newProductName;
                    productDb[2] = String.valueOf(newProductPrice);
                    products.add(String.join("|", productDb));
                    System.out.println("Product updated..");
                } else {
                    products.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Product not found.");
            return;
        }

        try (FileWriter writer = new FileWriter("src/db/products.txt", false)) {
            for (String product : products) {
                writer.write(product + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private boolean findCode(int Code) {
        try (BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] productDb = line.split("\\|");
                int existingCode = Integer.parseInt(productDb[0]);
                if (existingCode == Code) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public  void buyProduct(User currentUser){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.print("please enter product code:");
            int productCode = scanner.nextInt();
            if(!findCode(productCode)){
                List<String> products = new ArrayList<>();
                try(BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt"))){
                    String line;
                    while((line = file.readLine()) != null){
                        String [] productDb = line.split("\\|");
                        int code = Integer.parseInt(productDb[0]);
                        if(code == productCode){
                            System.out.println("product: " + productDb[1] + " | Price: "+ productDb[2]+ "-->  was purchased");
                            Order newOrder = new Order(currentUser.UUID,code);
                            newOrder.writeOrderToFile(newOrder);
                        }
                    }
                }
                catch (IOException e){
                    System.out.println("Error reading file: " + e.getMessage());
                    return;
                }
                System.out.print("Do you have another purchase? (true/false):  ");
                boolean repeatBuy = scanner.nextBoolean();
                if(!repeatBuy){
                    break;
                }


            }
            else{
                System.out.print("\ncode is incorrect. please enter product code again:");
            }
            System.out.println("");
        }
    }

    public void searchproduct(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Search Product by Name: ");
        String search = scanner.next();
        boolean found = false;
        try(BufferedReader file = new BufferedReader(new FileReader("src/db/products.txt")) ){
            String line;
            while ((line = file.readLine()) != null){
                String[] product = line.split("\\|");
                if(search.equals(product[1])){
                    found = true;
                    System.out.println("Code: " + product[0]+ ", Name: " + product[1] + ", Price: " + product[2]);
                }
            }
            if(!found){
                System.out.println("---> Product is not available !!");
            }
        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
    }
}

