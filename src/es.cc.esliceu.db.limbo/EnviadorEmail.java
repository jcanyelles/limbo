package es.cc.esliceu.db.limbo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviadorEmail {
    private String user;
    private String password;
    Properties prop;

    private static EnviadorEmail enviador = null;

    private EnviadorEmail(String user, String password, String host, String port, String auth) {
        this.user = user;
        this.password = password;
        prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", auth);


    }

    private void envia(String to, String subject, String text) throws AddressException {
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.user));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            System.out.println("Email enviat");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void enviaEmail(String to, String subject, String text)  {
        if (enviador==null){
            FileInputStream input = null;
            try {
                input = new FileInputStream("resources/limbo.properties");
                Properties props = new Properties();
                props.load(input);
                enviador = new EnviadorEmail(
                        props.getProperty("mail.user"),
                        props.getProperty("mail.password"),
                        props.getProperty("mail.smtp.host"),
                        props.getProperty("mail.smtp.port"),
                        props.getProperty("mail.smtp.auth")
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }

        }
        try {
            enviador.envia(to, subject, text);
        } catch (AddressException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

}
