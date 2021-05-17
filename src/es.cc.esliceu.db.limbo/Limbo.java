package es.cc.esliceu.db.limbo;

import es.cc.esliceu.db.limbo.dao.ClientDao;
import es.cc.esliceu.db.limbo.dao.CompraDao;
import es.cc.esliceu.db.limbo.dao.ProducteDao;
import es.cc.esliceu.db.limbo.dao.impl.ClientDaoImpl;
import es.cc.esliceu.db.limbo.dao.impl.CompraDaoImpl;
import es.cc.esliceu.db.limbo.dao.impl.ProducteDaoImpl;
import es.cc.esliceu.db.limbo.domain.Adreca;
import es.cc.esliceu.db.limbo.domain.Categoria;
import es.cc.esliceu.db.limbo.domain.Ciutat;
import es.cc.esliceu.db.limbo.domain.Client;
import es.cc.esliceu.db.limbo.domain.Compra;
import es.cc.esliceu.db.limbo.domain.DetallCompra;
import es.cc.esliceu.db.limbo.domain.Producte;
import es.cc.esliceu.db.limbo.domain.Targeta;
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

        //Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        Limbo limbo = new Limbo(con);
        limbo.menuPrincipal();
        String opcio = limbo.pregunta(scanner, "Esculli una opció: ");
        while (!opcio.equalsIgnoreCase("x")){
            if (!limbo.usuariValidat() && opcio.equalsIgnoreCase("1")){
                limbo.login(scanner);
            } else if (!limbo.usuariValidat() && opcio.equalsIgnoreCase("2")){
                limbo.registre(scanner);
            } else if (limbo.usuariValidat() && opcio.equalsIgnoreCase("c")){
                limbo.cercaProductes(scanner);
            } else if (limbo.usuariValidat() && opcio.equalsIgnoreCase("v")){
                limbo.pintaCistella(scanner);
            } else  if (limbo.usuariValidat() && opcio.equalsIgnoreCase("d")){
                limbo.menuDadesPersonlas(scanner);
            } else if (opcio.equalsIgnoreCase("h")){
                System.out.println("ajuda");
            } else if (limbo.usuariValidat() && opcio.matches("\\d+")) {
                //Productes suggerits
                int seleccionat = Integer.parseInt(opcio);
                Producte producteSeleccionat =  limbo.productesAleatoris.get(seleccionat);
                limbo.info("Ha seleccionat el producte " + seleccionat + " " + producteSeleccionat.getNom() + " " +producteSeleccionat.getPreu().toString());
                String unitatsSeleccionades  =limbo. pregunta(scanner, "Unitats: ");
                Integer unitats = Integer.parseInt(unitatsSeleccionades);

                limbo.cistella.put(producteSeleccionat, (limbo.cistella.get(producteSeleccionat)==null ? 0 : limbo.cistella.get(producteSeleccionat)) + unitats);
            }
                if (limbo.usuariValidat()){
                    limbo.menuValidat();
                }







            opcio = limbo.pregunta(scanner, "Esculli una opció: ");

        }
        limbo.sortir();
        con.close();
        System.exit(0);


    }

}
