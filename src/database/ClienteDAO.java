package database;
import entity.EntityCliente;

import java.util.ArrayList;


import exception.DAOException;
import exception.DBConnectionException;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;


public class ClienteDAO {
   
   //funzioni per la serializzazione e deserializzazione del file -clienti.ser-
   private static List<EntityCliente> deserializeListaClienti(String filename) {
	        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
	            return (List<EntityCliente>) input.readObject();
	        } catch (IOException | ClassNotFoundException e) {
	            System.out.println("File non trovato o errore nel caricamento da file: " + e.getMessage());
	            return null;
	        }
	    }

	private static void serializeListaClienti(List<EntityCliente> lista, String filename) {
	        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))) {
	            output.writeObject(lista);
	           
	        } catch (IOException e) {
	            System.out.println("Errore nel salvataggio su file: " + e.getMessage());
	        }
	    }
	    
	    //funzione per restituire la lista di clienti presenti nel database
	    public static List<EntityCliente> getListaClienti() {
	        String filename = "clienti.ser";
	        System.out.println("File serializzato in : " + new File(filename).getAbsolutePath());
	        System.out.println("Tentativo di deserializzazione da file...");
	        List<EntityCliente> lista = deserializeListaClienti(filename);
	        if (lista != null && !lista.isEmpty()) {
	            System.out.println("Lista caricata da file: " + lista.size() + " clienti.");
	            return lista;
	        }

	        System.out.println("File non trovato o vuoto. Caricamento da database...");
	        lista = new ArrayList<>();

	        try {
	            Connection conn = DBManager.getConnection();
	            System.out.println("Connessione al DB riuscita.");

	            String query = "SELECT idCliente, nomeUtente, password, cellulare, cartaDiCredito FROM Cliente";

	            try (PreparedStatement stmt = conn.prepareStatement(query);
	                 ResultSet result = stmt.executeQuery()) {

	                while (result.next()) {
	                    int idCliente = result.getInt("idCliente");
	                    String nomeUtente = result.getString("nomeUtente");
	                    String password = result.getString("password");
	                    String cellulare= result.getString("cellulare");
	                    String cartaCredito = result.getString("cartaDiCredito");

	                    EntityCliente cliente = new EntityCliente( idCliente, nomeUtente, password, cellulare, cartaCredito);
	                    lista.add(cliente);
	                }

	                System.out.println("Clienti caricati da database: " + lista.size());

	            } catch (SQLException e) {
	                System.err.println("Errore SQL: " + e.getMessage());
	                throw new DAOException("Errore durante la lettura dei clienti.");
	            }finally {
		        	DBManager.closeConnection();
		        } 

	        } catch (SQLException e) {
	            System.err.println("Errore connessione DB: " + e.getMessage());
	            throw new DBConnectionException("Errore di connessione al database.");
	        }

	        if (lista.isEmpty()) {
	            System.out.println("Nessun cliente trovato nel database.");
	        } else {
	            System.out.println("Serializzazione in corso...");
	            serializeListaClienti(lista, filename);
	        }

	        return lista;
	    }
	    
	    	
	
	//Legge i dati della carta di credito del cliente
	public static int readDatiPagamento (int idCliente) throws DAOException, DBConnectionException {
		int  datiPagamento = 0;
		
		try {
			Connection conn = DBManager.getConnection();
			
			String query = "SELECT cartaDiCredito FROM Spesa WHERE idCliente = ? ;";
			
			try {
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, idCliente);
				
				ResultSet result = stmt.executeQuery(); 
				
				if (result.next()) {
					datiPagamento = result.getInt(1);
				}
			}catch(SQLException e) {
				throw new DAOException("Errore nella lettura dei dati di pagamento ");
			}finally  {
				DBManager.closeConnection();
			}
			
		}catch (SQLException e) {
			throw new DBConnectionException("Errore di connessione al database ");
		}
		
		return datiPagamento;
	}
	
	
	// relativo al caso d'uso AggiungiProdottoCarrello
	
	public static EntityCliente readCliente(String id) {
    // Query corretta - deve interrogare la tabella Cliente, non Spesa
    String query = "SELECT * FROM cliente WHERE idcliente = ?";
    
    try (Connection conn = DBManager.getConnection()) {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Usa setString per il parametro stringa
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            
            if (result.next()) {
                // Usa il costruttore parametrizzato della classe EntityCliente
                EntityCliente cliente = new EntityCliente(
                    result.getInt("idCliente"),
                    result.getString("nomeUtente"),
                    result.getString("password"),  // nome colonna corretto in minuscolo
                    result.getString("cellulare"),
                    result.getString("cartaDiCredito")  // nome colonna corretto
                );
                return cliente;
            }
        } catch (SQLException eccezione) {
            System.out.println("Errore durante la ricerca del cliente con id " + id);
            eccezione.printStackTrace();
        }
    } catch (SQLException e) {
        System.out.println("Errore durante la connessione al database");
        e.printStackTrace();
    }
    
    return null;
}

public static EntityCliente CheckCliente (int id) {
	        // Query corretta - deve interrogare la tabella Cliente, non Spesa
	        String query = "SELECT * FROM cliente WHERE idcliente = ?";
	        
	        try (Connection conn = DBManager.getConnection()) {
	            try (PreparedStatement stmt = conn.prepareStatement(query)) {
	                // Usa setString per il parametro stringa
	                stmt.setInt(1, id);
	                ResultSet result = stmt.executeQuery();
	                
	                if (result.next()) {
	                    // Usa il costruttore parametrizzato della classe EntityCliente
	                    EntityCliente cliente = new EntityCliente(
	                        result.getInt("idCliente"),
	                        result.getString("nomeUtente"),
	                        result.getString("password"),  // nome colonna corretto in minuscolo
	                        result.getString("cellulare"),
	                        result.getString("cartaDiCredito")  // nome colonna corretto
	                    );
	                    return cliente;
	                }
	            } catch (SQLException eccezione) {
	                System.out.println("Errore durante la ricerca del cliente con id " + id);
	                eccezione.printStackTrace();
	            }
	        } catch (SQLException e) {
	            System.out.println("Errore durante la connessione al database");
	            e.printStackTrace();
	        }
	        
	        return null;
	    }

}
