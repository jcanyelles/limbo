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
    private static final Integer NOMBRE_ALEATORIS = 5;
    private ClientDao clientDao;
    private ProducteDao producteDao;
    private CompraDao compraDao;
    private Collection<Categoria> categories;
    private Collection<Ciutat> ciutats;
    private Client usuari = null;
    private Map<Producte, Integer> cistella;
    private List<Producte> productesAleatoris = new ArrayList<>();

    public Limbo(Connection connection) {
        this.clientDao = new ClientDaoImpl(connection);
        this.producteDao = new ProducteDaoImpl(connection);
        this.compraDao = new CompraDaoImpl(connection);
        this.categories = producteDao.llistaCategories();
        this.ciutats = clientDao.llistaCiutats();
        this.cistella = new LinkedHashMap<>();
    }

    public boolean usuariValidat(){
        return usuari!=null;
    }

    public void altaAdreca(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                Alta adreca             **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        String carrer = pregunta(scanner, "Carrer: ");
        String numero = pregunta(scanner, "Numero: ");
        String pis = pregunta(scanner, "Pis: ");
        String porta = pregunta(scanner, "Porta: ");
        String cp = pregunta(scanner, "CP: ");
        System.out.println(Color.YELLOW_BACKGROUND + "" + Color.WHITE_BRIGHT + "Ciutat:" + Color.RESET);
        for (Ciutat ciutat : ciutats) {
            System.out.println("\t(" + Color.YELLOW_BOLD + ciutat.getId() + Color.RESET + ") " + ciutat.getNom());
        }
        String ciutat = scanner.nextLine();
        Adreca adreca = new Adreca();
        adreca.setCodiClient(usuari.getId());
        adreca.setPorta(porta);
        adreca.setCp(cp);
        adreca.setNumero(numero);
        adreca.setAdreca(carrer);
        adreca.setPis(Integer.parseInt(pis));
        adreca.setCodiCiutat(Integer.parseInt(ciutat));
        clientDao.inserta(adreca);
    }

    public void altaTargeta(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                Alta targeta            **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        String numero = pregunta(scanner, "Numero: ");
        String tipus = pregunta(scanner, "Tipus (Visa, Mastercard): ");
        String caducitat = pregunta(scanner, "Caducitat: ");
        String codi = pregunta(scanner, "codi: ");
        Targeta targeta = new Targeta();
        targeta.setTipus(tipus);
        targeta.setCodi(Integer.parseInt(codi));
        targeta.setNumero(new BigInteger(numero));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            targeta.setCaducitat(sdf.parse("01/" + caducitat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        targeta.setCodiClient(usuari.getId());
        clientDao.inserta(targeta);
    }

    public void pintaPagament(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                  Pagament              **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        int productes = pintaTotalCistella(false);

        int i=0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        List<Targeta> targetes = new ArrayList<>(clientDao.llistaTargetes(usuari.getId()));
        if (targetes.isEmpty()){
            errada("No hi ha targetes disponibles");
        } else {
            System.out.println("Targetes dispinibles: ");
            for (Targeta t : targetes){
                System.out.println(Color.GREEN_BOLD + "" + i + "\t" + Color.CYAN_BOLD + t.getNumero() + " " + t.getTipus() + " " + sdf.format(t.getCaducitat()) + Color.RESET);
                i++;
            }
        }
        System.out.println("a) Afegir targeta");
        System.out.println("x) Sortir ");
        String opcio = pregunta(scanner, "Esculli una opció: ");
        Targeta targetaSeleccionda = null;
        while (!opcio.equalsIgnoreCase("x")){
            if (opcio.equalsIgnoreCase("a")){
                altaTargeta(scanner);
                pintaPagament(scanner);
                return;
            } else if (!opcio.equalsIgnoreCase("x")){
                //Es una targeta
                Integer numero = Integer.parseInt(opcio);
                targetaSeleccionda = targetes.get(numero);
                break;
            }
            opcio = pregunta(scanner, "Esculli una opció: ");
        }
        if (targetaSeleccionda!=null){
            Adreca adrecaSeleccionada = null;
            List<Adreca> adreces = new ArrayList<>(clientDao.llistaAdreces(usuari.getId()));
            if (adreces.isEmpty()){
                errada("No hi ha adreces disponibles");
            } else {
                System.out.println("Adreces d'enviament disponibles: ");
                i=0;
                for (Adreca a : adreces){
                    System.out.println(Color.GREEN_BOLD + "" + i + "\t" + Color.CYAN_BOLD + a.getAdreca() + " " + a.getNumero() + " " + a.getCp() + Color.RESET);
                    i++;
                }
            }
            System.out.println("a) Afegir adreça d'enviament");
            System.out.println("x) Sortir ");
            opcio = pregunta(scanner, "Esculli una opció: ");
            Adreca adrecaSeleccionda = null;
            while (!opcio.equalsIgnoreCase("x")){
                if (opcio.equalsIgnoreCase("a")){
                    altaAdreca(scanner);
                    pintaPagament(scanner);
                    return;
                } else if (!opcio.equalsIgnoreCase("x")){
                    //Es una targeta
                    Integer numero = Integer.parseInt(opcio);
                    adrecaSeleccionada = adreces.get(numero);
                    break;
                }
                opcio = pregunta(scanner, "Esculli una opció: ");
            }

            if (adrecaSeleccionada!=null){
                System.out.println("Es farà el següent pagament a:");
                System.out.println(usuari.getNom() + " " + usuari.getLlinatge1() + " " + usuari.getLlinatge1() + " " + usuari.getEmail());
                pintaTotalCistella(false);
                System.out.println("Amb la targeta:");
                System.out.println( "\t" + Color.CYAN_BOLD + targetaSeleccionda.getNumero() + " " + targetaSeleccionda.getTipus() + " " + sdf.format(targetaSeleccionda.getCaducitat()) + Color.RESET);
                System.out.println("Amb la adreça d'enviament:");
                System.out.println( "\t" + Color.CYAN_BOLD + adrecaSeleccionada.getAdreca() + " " + adrecaSeleccionada.getNumero() + " " + adrecaSeleccionada.getCp() + Color.RESET);
                String continuar = pregunta(scanner, "Desitja continuar amb el pagament (S/N)? ");
                if (continuar.equalsIgnoreCase("s")){
                    Compra compra = compraDao.altaCompra(usuari, cistella, targetaSeleccionda);
                    System.out.println(Color.BLUE_BOLD + "COMPRA REALITZADA CORRECTAMENT. S'ha envuat la factura a " + usuari.getEmail() + ". ID Transaccio: " + compra.getTransaccio());
                    this.cistella = new LinkedHashMap<>();
                    pregunta(scanner, "Pulsi [ENTER] per continuar ");
                }
            }
        }

    }
    public void eliminaProducte(int i){
        int inici = 0;
        Iterator<Map.Entry<Producte, Integer>> iterator = cistella.entrySet().iterator();
        while (iterator.hasNext()){
            iterator.next();
            if (inici == i){
                iterator.remove();
            }
            inici++;
        }

    }
    public Float calculaTotalCistella(){
        Float totalCistella = 0F;
        for (Map.Entry<Producte, Integer> entrada : cistella.entrySet()) {
            totalCistella += entrada.getKey().getPreu() * entrada.getValue();
        }
        return totalCistella;
    }

    public int pintaTotalCistella(boolean pintarMenu){
        Float totalCistella = 0F;
        int i=0;
        for (Map.Entry<Producte, Integer> entrada : cistella.entrySet()) {
            totalCistella += entrada.getKey().getPreu() * entrada.getValue();
            System.out.println(Color.GREEN_BOLD + "" + i + "\t" + Color.CYAN_BOLD + entrada.getKey().getNom() + Color.RESET + " " + entrada.getKey().getPreu() + " " + entrada.getValue() + " unitats"+ Color.RESET);
            i++;
        }
        System.out.println("--------------------------------------------");
        System.out.println("Total cistella: " + Color.BLUE_BOLD + totalCistella + Color.RESET);
        System.out.println("--------------------------------------------");
        if (pintarMenu){
            if (i>0){
                System.out.println("e) Eliminar producte");
                System.out.println("p) Pagat ");
            }
            System.out.println("x) Sortir cistella ");
        }
        return i;
    }
    public void pintaCistella(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                  Cistella              **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        int i = pintaTotalCistella(true);
        String opcio = pregunta(scanner, "Esculli una opció de cistella: ");
        while (!opcio.equalsIgnoreCase("x")){
            if (opcio.equalsIgnoreCase("e")){
                String producte = pregunta(scanner, "Producte (0-" + (i-1) + "):" );
                eliminaProducte(Integer.parseInt(producte));
                i = pintaTotalCistella(true);
            }
            if (opcio.equalsIgnoreCase("p")){
                pintaPagament(scanner);
                return;
            }
            opcio = pregunta(scanner, "Esculli una opció de cistella: ");
        }
    }
    public void cercaProductes(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"*******************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**          Cerca            **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"*******************************"+Color.RESET);
        String nom = pregunta(scanner, "Nom producte");
        String descripcio = pregunta(scanner, "Descripció");
        String marca = pregunta(scanner, "Marca");

        System.out.println(Color.YELLOW_BACKGROUND + "" + Color.WHITE_BRIGHT + "Categoria:" + Color.RESET);
        for (Categoria category : categories) {
            System.out.println("\t(" + Color.YELLOW_BOLD + category.getId() + Color.RESET + ") " + category.getNom() + " " + category.getDescripcio());
        }
        String categoria = scanner.nextLine();
        Integer codiCategoria = null;
        if (categoria!=null && !categoria.isEmpty()){
            codiCategoria = Integer.parseInt(categoria);
        }

        List<Producte> productes =  new ArrayList<>(producteDao.llistaProductes(null,"%" + nom + "%","%" + descripcio + "%","%" + marca + "%",codiCategoria));
        info("S'han trobat " + productes.size() + " productes");
        int i = 0;
        for (Producte producte : productes) {
            System.out.println(Color.GREEN_BOLD + "" + i + Color.RESET + "\t" + producte.getNom() + "\t" + producte.getDescripcio() + "\t" + Color.CYAN_BOLD + producte.getMarca()+ Color.RESET + "\t" + Color.BLUE_BOLD  + producte.getPreu().toString() + "€" + Color.RESET);
            i++;
        }
        if (i>0){
            System.out.println("Seleccioni el producte (0-" + (i-1) + "): ");
            System.out.println("x) Sortir cerca ");
            String opcio = pregunta(scanner, "Esculli l'opció: ");
            while (!opcio.equalsIgnoreCase("x")){
                int seleccionat = Integer.parseInt(opcio);
                Producte producteSeleccionat =  productes.get(seleccionat);
                info("Ha seleccionat el producte " + seleccionat + " " + producteSeleccionat.getNom() + " " +producteSeleccionat.getPreu().toString());
                String unitatsSeleccionades  = pregunta(scanner, "Unitats: ");
                Integer unitats = Integer.parseInt(unitatsSeleccionades);

                cistella.put(producteSeleccionat, (cistella.get(producteSeleccionat)==null ? 0 : cistella.get(producteSeleccionat)) + unitats);

                System.out.println("Seleccioni el producte (0-" + (i-1) + "): ");
                System.out.println("x) Sortir cerca ");
                opcio = pregunta(scanner, "Esculli l'opció: ");
            }


        }


    }

    public void login(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"*******************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**          Login            **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"*******************************"+Color.RESET);
        String username = pregunta(scanner, "Username");
        Client client = clientDao.cercaClientPerUsername(username);
        while (client==null){
            errada("Error: username no existeix.");
            username = pregunta(scanner, "Username");
            client = clientDao.cercaClientPerUsername(username);
        }
        info("Usuari trobat al sistema");
        String password = pregunta(scanner, "Password:");
        int intents = 0;
        boolean validat = GeneradorHash.generaHash(password).equals(client.getPassword());
        while (!validat && intents < 2) {
            password = pregunta(scanner, "Password:");
            validat = GeneradorHash.generaHash(password).equals(client.getPassword());
            intents ++;
        }
        if (validat){
            this.usuari = client;
            info("Usuari validat correctament");
        } else {
            errada("No s'ha pogut validar usuari");
            System.exit(1);
        }

    }

    public void registre(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                 Registe                **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        String username = pregunta(scanner, "Username");
        Client client = clientDao.cercaClientPerUsername(username);
        while (client!=null){
            errada("Error: username ja existeix. Provi un altre usuari");
            username = pregunta(scanner, "Username");
            client = clientDao.cercaClientPerUsername(username);
        }
        String email = pregunta(scanner, "Email");
        client = clientDao.cercaClientPerEmail(email);
        while (client!=null){
            errada("Error: email ja existeix. Provi un altre email");
            email = pregunta(scanner, "Email");
            client = clientDao.cercaClientPerEmail(email);
        }

        String referencia = GeneradorHash.generaRandomString();
        EnviadorEmail.enviaEmail(email,"Alta usuari a Limbo", "La refeència és : " + referencia);
        System.out.println("S'ha enviat un correu electrònic amb un areferència l'adreça: " + email);
        System.out.println("Referència:");
        String ref = scanner.nextLine();
        if (!ref.equals(referencia)){
            System.out.printf("Referència incorrecte");
            return;
        }

        System.out.println("Password:");  String password = scanner.nextLine();
        System.out.println("Nom:");       String nom = scanner.nextLine();
        System.out.println("Llinatge1:"); String llinatge1 = scanner.nextLine();
        System.out.println("Llinatge2:"); String llinatge2 = scanner.nextLine();
        Client nou = new Client();
        nou.setEmail(email);
        nou.setUsername(username);
        nou.setPassword(GeneradorHash.generaHash(password));
        nou.setNom(nom);
        nou.setLlinatge1(llinatge1);
        nou.setLlinatge2(llinatge2);
        clientDao.inserta(nou);
        info("Usuari " + username + " creat correctament amb id " + nou.getId());
        this.usuari = nou;
    }
    public void menuPrincipal(){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                Limbo app              **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.BLUE_BOLD + "1)" + Color.RESET + " Login ");
        System.out.println(Color.BLUE_BOLD + "2)" + Color.RESET + " Registrar-se ");
        System.out.println(Color.BLUE_BOLD + "h)" + Color.RESET + " Ajuda ");
        System.out.println(Color.BLUE_BOLD + "x)" + Color.RESET + " Sortir ");
    }

    public void menuValidat(){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**               Opcions                  **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"Usuari:" + Color.WHITE_BOLD + usuari.getNom() + " " + usuari.getLlinatge1()+" " + Color.RED_BOLD + " "+ this.usuari.getUsername()+Color.RESET);
        if (!cistella.isEmpty()){
            System.out.println(Color.BLUE_BOLD +"Cistella:" + Color.RED_BOLD + calculaTotalCistella() + "€" +Color.RESET);
        }
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.BLUE_BOLD+"c)" +Color.RESET+" Cerca productes ");
        System.out.println(Color.BLUE_BOLD+"v)" +Color.RESET+" Veure cistella ");
        System.out.println(Color.BLUE_BOLD+"d)" +Color.RESET+" Dades personals ");
        System.out.println(Color.BLUE_BOLD+"h)" +Color.RESET+" Ajuda ");
        System.out.println(Color.BLUE_BOLD+"x)" +Color.RESET+" Sortir ");
        productesAleatoris = new ArrayList<>(producteDao.llistaProductesAleatoris(NOMBRE_ALEATORIS));
        int i = 0;
        System.out.println(Color.RED_BOLD + "----------------   Productes suggerits -------------------" + Color.RESET);
        for (Producte producte : productesAleatoris) {
            System.out.println(Color.GREEN_BOLD + "" + i + Color.RESET + "\t" + producte.getNom() + "\t" + producte.getDescripcio() + "\t" + Color.CYAN_BOLD + producte.getMarca()+ Color.RESET + "\t" + Color.BLUE_BOLD  + producte.getPreu().toString() + "€" + Color.RESET);
            i++;
        }
        System.out.println(Color.RED_BOLD + "----------------------------------------------------------" + Color.RESET);
        if (i>0){
            System.out.println(Color.BLUE_BOLD+ " (0-" + (i-1) + ") " + Color.RESET + "Productes suggerits");
        }

    }
    private Float calculaTotalCompra(Collection<DetallCompra> detalls){
        Float total = 0f;
        for (DetallCompra detall : detalls) {
            total += detall.getPreu() * detall.getUnitats();
        }
        return total;
    }
    public void pantallaCompres(Scanner scanner){
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                 Compres                **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"** Usuari:" + Color.WHITE_BOLD + usuari.getNom() + " " + usuari.getLlinatge1()+" " + Color.RED_BOLD + " "+ this.usuari.getUsername()+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        int i=0;
        for(Compra c : compraDao.llistaCompres(usuari.getId())){
            Collection<DetallCompra> detalls = compraDao.llistaDetallCompta(c.getId());
            System.out.println(Color.GREEN_BOLD + "" + i + "\t" + Color.CYAN_BOLD + c.getData() + " " + c.getTransaccio() + " "  + Color.RED_BOLD + calculaTotalCompra(detalls) + " €" + Color.RESET);
            for (DetallCompra detall : detalls) {
                Producte producte = producteDao.carrega(detall.getCodiProducte());
                System.out.println(Color.YELLOW_BOLD + "\t\t" + producte.getNom() + " " + detall.getUnitats() + " " + detall.getPreu() + " €" + Color.RESET);
                i++;
            }
        }

    }
    public void pantallaSettings(Scanner scanner){
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                 Settings               **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"** Usuari:" + Color.WHITE_BOLD + usuari.getNom() + " " + usuari.getLlinatge1()+" " + Color.RED_BOLD + " "+ this.usuari.getUsername()+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println("a) Modifica dades personals");
        System.out.println("b) Canvi password");
        System.out.println("x) Sortir ");
        String opcio = pregunta(scanner, "Esculli una opció: ");
        while (!opcio.equalsIgnoreCase("x")){
            if (opcio.equalsIgnoreCase("a")){
                String nom = pregunta(scanner, "Nom:" + Color.CYAN_BOLD+ "("+usuari.getNom()+")"+ Color.RESET + ":");
                String llinatge1 = pregunta(scanner, "Llinatge1:" + Color.CYAN_BOLD+ "(" + usuari.getLlinatge1()+")"+ Color.RESET + ":");
                String llinatge2 = pregunta(scanner, "Llinatge2:" + Color.CYAN_BOLD+ "(" + usuari.getLlinatge2()+")"+ Color.RESET + ":");
                boolean modificar = false;
                if (nom!=null && !nom.isEmpty()){
                    usuari.setNom(nom);
                    modificar = true;
                }
                if (llinatge1!=null && !llinatge1.isEmpty()){
                    usuari.setLlinatge1(llinatge1);
                    modificar = true;
                }
                if (llinatge2!=null && !llinatge2.isEmpty()){
                    usuari.setLlinatge2(llinatge2);
                    modificar = true;
                }
                if (modificar){
                    clientDao.modifica(usuari);
                }
            }
            if (opcio.equalsIgnoreCase("b")){
                String passwordAntic =  pregunta(scanner, "\tPassword antic: ");
                if (GeneradorHash.generaHash(passwordAntic).equals(usuari.getPassword())){
                    String passwordNou =  pregunta(scanner, "\tPassword nou: ");
                    String passwordRepeticio =  pregunta(scanner, "\tRepeteixi el password nou: ");
                    if (passwordNou.equals(passwordRepeticio)){
                        String referencia = GeneradorHash.generaRandomString();
                        EnviadorEmail.enviaEmail(usuari.getEmail(),"S'ha sol.licitat nova constrassenya a Limbo", "La referència és : " + referencia);
                        info("S'ha enviat un correu electrònic amb un areferència l'adreça: " + usuari.getEmail());
                        String refEnviada = pregunta(scanner,"Referència? ");
                        if(refEnviada.equals(referencia)){
                            usuari.setPassword(GeneradorHash.generaHash(passwordNou));
                            clientDao.modifica(usuari);
                        }
                    } else {
                        errada("Passwords no coincideixen");
                    }
                } else {
                    errada("Password antic incorrecte");
                }
            }
            opcio = pregunta(scanner, "Esculli una opció: ");
        }
    }
    public void pantallaAdreces(Scanner scanner){
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                     Adreces            **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"** Usuari:" + Color.WHITE_BOLD + usuari.getNom() + " " + usuari.getLlinatge1()+" " + Color.RED_BOLD + " "+ this.usuari.getUsername()+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        List<Adreca> adreces = new ArrayList<>(clientDao.llistaAdreces(usuari.getId()));
        if (adreces.isEmpty()){
            errada("No hi ha adreces disponibles");
        } else {
            int i=0;
            for (Adreca a : adreces){
                System.out.println(Color.GREEN_BOLD + "" + i + "\t" + Color.CYAN_BOLD + a.getAdreca() + " " + a.getNumero() + " " + a.getCp() + Color.RESET);
                i++;
            }
        }
        System.out.println("a) Afegir adreça d'enviament");
        System.out.println("x) Sortir ");
        String opcio = pregunta(scanner, "Esculli una opció: ");
        while (!opcio.equalsIgnoreCase("x")){
            if (opcio.equalsIgnoreCase("a")){
                altaAdreca(scanner);
                pantallaAdreces(scanner);
            }else {
                Adreca adrecaSeleccionada = adreces.get(Integer.parseInt(opcio));
                String carrer = pregunta(scanner, "Carrer:" + Color.CYAN_BOLD+ "("+adrecaSeleccionada.getAdreca()+")"+ Color.RESET + ":");
                String numero = pregunta(scanner, "Numero:" + Color.CYAN_BOLD+ "("+adrecaSeleccionada.getNumero()+")"+ Color.RESET + ":");
                String pis = pregunta(scanner, "Pis:" + Color.CYAN_BOLD+ "("+adrecaSeleccionada.getPis()+")"+ Color.RESET + ":");
                String porta = pregunta(scanner, "Porta:" + Color.CYAN_BOLD+ "("+adrecaSeleccionada.getPorta()+")"+ Color.RESET + ":");
                String cp = pregunta(scanner, "CP:" + Color.CYAN_BOLD+ "("+adrecaSeleccionada.getCp()+")"+ Color.RESET + ":");
                System.out.println(Color.YELLOW_BACKGROUND + "" + Color.WHITE_BRIGHT + "Ciutat:" + Color.RESET);
                for (Ciutat ciutat : ciutats) {
                    String  cadena = "\t(" + Color.YELLOW_BOLD + ciutat.getId() + Color.RESET + ") ";
                    if (ciutat.getId().equals(adrecaSeleccionada.getCodiCiutat())){
                        cadena += Color.CYAN_BOLD;
                    }
                    cadena += ciutat.getNom() + Color.RESET;
                    System.out.println(cadena);
                }
                String ciutat = scanner.nextLine();
                Adreca adreca = new Adreca();
                adreca.setId(adrecaSeleccionada.getId());
                adreca.setCodiClient(usuari.getId());
                adreca.setPorta(adrecaSeleccionada.getPorta());
                if (porta!=null && !porta.isEmpty()){
                    adreca.setPorta(porta);
                }
                adreca.setCp(adrecaSeleccionada.getCp());
                if (cp!=null && !cp.isEmpty()){
                    adreca.setCp(cp);
                }
                adreca.setNumero(adrecaSeleccionada.getNumero());
                if (numero!=null && !numero.isEmpty()){
                    adreca.setNumero(numero);
                }
                adreca.setAdreca(adrecaSeleccionada.getAdreca());
                if (carrer!=null && !carrer.isEmpty()){
                    adreca.setAdreca(carrer);
                }
                adreca.setPis(adrecaSeleccionada.getPis());
                if (pis!=null && !pis.isEmpty()){
                    adreca.setPis(Integer.parseInt(pis));
                }
                adreca.setCodiCiutat(adrecaSeleccionada.getCodiCiutat());
                if (ciutat!=null && !ciutat.isEmpty()){
                    adreca.setCodiCiutat(Integer.parseInt(ciutat));
                }
                clientDao.modifica(adreca);
                pantallaAdreces(scanner);
            }
            opcio = pregunta(scanner, "Esculli una opció: ");
        }

    }
    public void menuDadesPersonlas(Scanner scanner){
        clearScreen();
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"**                Dades personals        **"+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"** Usuari:" + Color.WHITE_BOLD + usuari.getNom() + " " + usuari.getLlinatge1()+" " + Color.RED_BOLD + " "+ this.usuari.getUsername()+Color.RESET);
        System.out.println(Color.GREEN_BOLD +"********************************************"+Color.RESET);
        System.out.println(Color.BLUE_BOLD+"s)" +Color.RESET+" Settings ");
        System.out.println(Color.BLUE_BOLD+"c)" +Color.RESET+" Compres realitzades ");
        System.out.println(Color.BLUE_BOLD+"a)" +Color.RESET+" Adreces ");
        System.out.println(Color.BLUE_BOLD+"t)" +Color.RESET+" Targetes ");
        System.out.println(Color.BLUE_BOLD+"x)" +Color.RESET+" Sortir ");
        String opcio = pregunta(scanner, "Esculli una opció de dades personals: ");
        while (!opcio.equalsIgnoreCase("x")){
            if (opcio.equalsIgnoreCase("a")){
                pantallaAdreces(scanner);
            }
            if (opcio.equalsIgnoreCase("c")){
                pantallaCompres(scanner);
            }
            if (opcio.equalsIgnoreCase("s")){
                pantallaSettings(scanner);
            }
            opcio = pregunta(scanner, "Esculli una opció de dades personals: ");
        }
    }

    public void sortir(){
        System.out.println("Gràcies per utlitzar Limbo ");
    }
    public String pregunta(Scanner scanner, String texte){
        System.out.println(Color.YELLOW_BACKGROUND + "" + Color.WHITE_BRIGHT + texte + Color.RESET);
        return scanner.nextLine();
    }
    public void info(String texte){
        System.out.println(Color.BLUE_BOLD + "\t" + texte + Color.RESET);
    }
    public void errada(String texte){
        System.out.println(Color.RED_BOLD + "\t" + texte + Color.RESET);
    }
    public void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }
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
