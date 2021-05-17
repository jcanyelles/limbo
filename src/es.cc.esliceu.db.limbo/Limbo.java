package es.cc.esliceu.db.limbo;

import es.cc.esliceu.db.limbo.util.Color;
import javax.mail.internet.AddressException;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Limbo {

    public static void main(String[] args) throws IOException, SQLException, AddressException, ClassNotFoundException {

        FileInputStream input = new FileInputStream("resources/limbo.properties");
        Properties props = new Properties();
        props.load(input);
        String URL = props.getProperty("url");
        String USERNAME = props.getProperty("user");
        String PASSWORD = props.getProperty("password");

        Scanner scanner = new Scanner(System.in);


    }

}
