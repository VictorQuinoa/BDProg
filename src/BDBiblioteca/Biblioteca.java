package BDBiblioteca;

import javax.naming.spi.DirStateFactory;
import java.sql.*;

public class Biblioteca {

    public static void main(String[] args) throws Exception{

        Connection Connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Biblioteca", "postgres", "postgres");

        System.out.println("SI");

        Statement Query = Connection.createStatement();

        ResultSet Resultado = Query.executeQuery("select * from Libros");

        while(Resultado.next()){

            String Titulo = Resultado.getString("Titulo");

            System.out.println(Titulo);
        }



    }

}
