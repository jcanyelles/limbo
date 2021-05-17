package es.cc.esliceu.db.limbo;

import es.cc.esliceu.db.limbo.util.Color;
import javax.mail.internet.AddressException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Limbo {

    public static void main(String[] args) throws IOException {

        FileInputStream input = new FileInputStream("limbo/resources/limbo.properties");
        Properties props = new Properties();
        props.load(input);
        String URL = props.getProperty("url");
        String USERNAME = props.getProperty("user");
        String PASSWORD = props.getProperty("password");

        info("username " + USERNAME);
        Scanner scanner = new Scanner(System.in);

    }

    public static void info(String texte){
        System.out.println(Color.BLUE_BOLD + "\t" + texte + Color.RESET);
    }
    public static void errada(String texte){
        System.out.println(Color.RED_BOLD + "\t" + texte + Color.RESET);
    }
}
