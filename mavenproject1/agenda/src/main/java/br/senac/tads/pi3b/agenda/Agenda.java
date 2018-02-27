package br.senac.tads.pi3b.agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruna.ssjodai
 */
public class Agenda {

    private Connection obterConexao() throws ClassNotFoundException, SQLException {
        // Passo 1: Registrar o driver JDBC
        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenda",
                "root", // usuário BD
                ""); // senha BD

        return conn;
    }

    public List<Pessoa> listar() throws ClassNotFoundException, SQLException {
        List<Pessoa> lista = new ArrayList<Pessoa>();

        //abrir conexao com bd
        //declarar o drive jdbc de acordo com o bd usado
        try (Connection conn = obterConexao();
                PreparedStatement stmt = conn.prepareStatement("SELECT id, nome, dtnasc FROM agenda.PESSOA");
                ResultSet resultados = stmt.executeQuery();) {
            while (resultados.next()) {
                long id = resultados.getLong("id");
                String nome = resultados.getString("nome");
                Date dtnasc = resultados.getDate("dtnasc");

                Pessoa p = new Pessoa();
                p.setId(id);
                p.setNome(nome);
                p.setDtNasc(dtnasc);

                lista.add(p);

                //System.out.println(id + ", " + nome + ", " + dtnasc);
            }

        }

        return lista;

    }

    public void incluir() throws ClassNotFoundException, SQLException {
        try (Connection conn = obterConexao();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO agenda.PESSOA (nome, dtnasc) VALUES (?, ?)")) {
            stmt.setString(1, "Jonas Ribeiro");
            GregorianCalendar cal = new GregorianCalendar(1995, 10, 11);
            stmt.setDate(2, new java.sql.Date(cal.getTimeInMillis()));

            int status = stmt.executeUpdate();

            System.out.println("status: " + status);
        }
    }

    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        try {
            //agenda.incluir();

            List<Pessoa> lista = agenda.listar();

            for (Pessoa p : lista) {
                System.out.println(p.getId() + " - " + p.getNome() + " - " + p.getDtNasc());
            }

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
