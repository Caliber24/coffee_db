import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Config {
    //    admin information
    private int id = 0;
    private String usernameAdmin = "admin";
    private String passwordAdmin = "1234";
    public  boolean ISLOGIN = false;
    public  User CurrentUser;
    public RawMaterials RawMaterials=new RawMaterials();
    public  Product product=new Product();
    public static String createUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }



}
