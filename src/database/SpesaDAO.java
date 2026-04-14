package database;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import entity.EntityDettaglioSpesa;
import entity.EntitySpesa;
import entity.StatoSpesa;

import exception.DAOException;
import exception.DBConnectionException;

public class SpesaDAO {
    public static int countSpese(int idCliente) {
	    int count = 0;

	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "SELECT COUNT(*) FROM Spesa WHERE idCliente = ?";
	        /*
	         * effettuata la connessione con il database
	         * si richiede il conto totale delle spese di ogni cliente
	         */

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        	/*
	        	 * eseguita la query si inserisce l'id del cliente
	        	 * il campo '1' indica il paramentro '?' da sostituire con il parametro effettivo nella funzione
	        	 */
	            stmt.setInt(1, idCliente);
	            ResultSet result = stmt.executeQuery();
	            /*
	             * si prende il risultato della query e si preleva il valore
	             */

	            if (result.next()) {
	                count = result.getInt(1);
	            }

	        } catch (SQLException e) {
	            throw new DAOException("Errore conteggio spese.");
	        }finally {
	        	DBManager.closeConnection();
	        }
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore connessione DB.");
	    }

	    return count;
	}

    public static float readImportoSpese(int idCliente) {
    	float importoSpese = 0;
    	try {
    		Connection conn = DBManager.getConnection();
	        String query = "SELECT SUM(costoTotale) AS Totale FROM Spesa WHERE idCliente = ?";
	        
	     try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, idCliente);
	            ResultSet result = stmt.executeQuery();
	            if(result.next()) {
	            	importoSpese = result.getFloat("Totale");
	            }
	            
    	}catch (SQLException e) {
            throw new DAOException("Errore lettura spese cliente ID " + idCliente);
    	}
    } catch (SQLException e) {
        throw new DBConnectionException("Errore connessione DB");
    }
    	return importoSpese;
    }
	
	public static int ReadidSconto (int idSpesa) throws DAOException, DBConnectionException {
		int idSconto = 0;
		try {
			Connection conn = DBManager.getConnection();
			
			String query = "SELECT idSconto FROM Spesa WHERE idSpesa = ? ;";
			
			try {
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, idSpesa);
				
				ResultSet result = stmt.executeQuery(); 
				
				if (result.next()) {
					idSconto = result.getInt(1);	
				}
			}catch(SQLException e) {
				throw new DAOException("Errore nella lettura dell'idSConto ");
			}finally  {
				DBManager.closeConnection();
			}
			
		}catch (SQLException e) {
			throw new DBConnectionException("Errore di connessione al database ");
		}
		
		
		return idSconto;
	}
	
	
	//Restituisce l'id del cliente associato alla spesa
	public static int ReadCliente(int idSpesa) throws DAOException, DBConnectionException{
		int idCliente = 0;
		try {
			Connection conn = DBManager.getConnection();
			
			String query = "SELECT idCliente FROM Spesa WHERE idSpesa = ? ;";
			
			try {
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, idSpesa);
				
				ResultSet result = stmt.executeQuery(); 
				
				if (result.next()) {
					idCliente = result.getInt(1);	
				}
			}catch(SQLException e) {
				throw new DAOException("Errore nella lettura dell'idCliente ");
			}finally  {
				DBManager.closeConnection();
			}
			
		}catch (SQLException e) {
			throw new DBConnectionException("Errore di connessione al database ");
		}
		
		
		return idCliente;
	}
	
	
	
	
	// Recupera tutte le spese IN_CORSO di un cliente, includendo per ciascuna i prodotti acquistati e le relative quantità.
	public static ArrayList<String> readSpeseWithProdottiByCliente(String idCliente) {
	    ArrayList<String> speseList = new ArrayList<>();
	    String querySpese = "SELECT idspesa, data, costoTotale, stato FROM spesa WHERE idcliente = ? AND stato = ?";
	    String queryProdotti = """
	        SELECT p.nome, ds.qtacarrello 
	        FROM dettagliospesa ds 
	        JOIN prodotto p ON ds.idprod = p.codice 
	        WHERE ds.idspesa = ?
	    """;

	    try {
	        Connection conn = DBManager.getConnection();

	        try {
	            PreparedStatement stmtSpese = conn.prepareStatement(querySpese);
	            stmtSpese.setString(1, idCliente);
	            stmtSpese.setString(2, StatoSpesa.IN_CORSO.name()); 
	            ResultSet rsSpese = stmtSpese.executeQuery();

	            while (rsSpese.next()) {
	                int idSpesa = rsSpese.getInt("idspesa");
	                Date data = rsSpese.getDate("data");
	                float costoTotale = rsSpese.getFloat("costoTotale");
	                String stato = rsSpese.getString("stato");

	                StringBuilder spesaBuilder = new StringBuilder();
	                spesaBuilder.append("idSpesa: ").append(idSpesa)
	                            .append(", Data: ").append(data)
	                            .append(", CostoTotale: ").append(costoTotale)
	                            .append(", Stato: ").append(stato)
	                            .append("\n    -> Prodotti:\n");

	                // Recupero i prodotti relativi a questa spesa
	                PreparedStatement stmtProdotti = conn.prepareStatement(queryProdotti);
	                stmtProdotti.setInt(1, idSpesa);
	                ResultSet rsProdotti = stmtProdotti.executeQuery();

	                while (rsProdotti.next()) {
	                    String nome = rsProdotti.getString("nome");
	                    int quantita = rsProdotti.getInt("qtacarrello");

	                    spesaBuilder.append("       - Nome: ").append(nome)
	                                .append(", Quantità: ").append(quantita).append("\n");
	                }

	                rsProdotti.close();
	                stmtProdotti.close();

	                speseList.add(spesaBuilder.toString());
	            }

	            rsSpese.close();
	            stmtSpese.close();

	        } catch (SQLException e) {
	            System.out.println("Errore durante il recupero delle spese e dei prodotti per il cliente con id " + idCliente);
	            e.printStackTrace();
	        } finally {
	            DBManager.closeConnection();
	        }

	    } catch (SQLException e) {
	        System.out.println("Errore durante la connessione al database");
	        e.printStackTrace();
	    }

	    return speseList;
	}

	
	public static EntitySpesa readSpesa(String id) {
	    String query = "SELECT * FROM spesa WHERE idspesa = ?";
	
	    try {
	        Connection conn = DBManager.getConnection();
	
	        try {
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, id);
	            ResultSet result = stmt.executeQuery();
	            
	            if (result.next()) {
	                // Raccogli i dati dal ResultSet
	                int idSpesa = result.getInt("idSpesa");
	                Date data = result.getDate("Data");
	                float costoTotale = result.getFloat("costoTotale");
	                StatoSpesa stato = StatoSpesa.valueOf(result.getString("Stato"));
	                int idSconto = result.getInt("idSconto");
	                int idCliente = result.getInt("idCliente");
	                
	                // Per ora lista vuota, andrebbe popolata con una query separata
	                List<EntityDettaglioSpesa> prodotti = new ArrayList<>();
	                
	                // Crea l'oggetto usando il costruttore parametrizzato
	                EntitySpesa s = new EntitySpesa(idSpesa, data, costoTotale, stato, idSconto, idCliente, prodotti);
	                
	                return s;
	            }
	        } catch (SQLException e) {
	            System.out.println("Errore durante la lettura della spesa con id " + id);
	            e.printStackTrace();
	        } finally {
	            DBManager.closeConnection();
	        }
	    } catch (SQLException e) {
	        System.out.println("Errore durante la connessione al database");
	        e.printStackTrace();
	    }
	    return null;
	}
	
	
	public static void updatePrezzoSpesa(float costoTotale, int idSpesa) throws DAOException, DBConnectionException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try {
	        conn = DBManager.getConnection();
	        String query = "UPDATE Spesa SET costoTotale = ? WHERE idSpesa = ?";
	        
	        stmt = conn.prepareStatement(query);
	        stmt.setFloat(1, costoTotale);
	        stmt.setInt(2, idSpesa);
	        
	        
	        int rowsAffected = stmt.executeUpdate();

	        if (rowsAffected == 0) {
	            throw new DAOException("Nessuna spesa trovata con ID: " + idSpesa);
	        }
	        
	    } catch (SQLException e) {
	        throw new DAOException("Errore nell'aggiornamento del prezzo della spesa: " + e.getMessage());
	    } finally {
	        // Chiudi le risorse nell'ordine corretto
	        try {
	            if (stmt != null) {
	                stmt.close();
	            }
	        } catch (SQLException e) {
	            // Log dell'errore ma non propagarlo
	            System.err.println("Errore nella chiusura del PreparedStatement: " + e.getMessage());
	        }
	        
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            // Log dell'errore ma non propagarlo
	            System.err.println("Errore nella chiusura della Connection: " + e.getMessage());
	        }
	    }
	}
	
	public static void UpdateStatoSpesa(int idSpesa, StatoSpesa stato) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        conn = DBManager.getConnection();
        String query = "UPDATE Spesa SET stato = ? WHERE idSpesa = ?";

        stmt = conn.prepareStatement(query);
        stmt.setString(1, stato.name()); // Usa stato.toString() o stato.name() a seconda del tipo
        stmt.setInt(2, idSpesa);

        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected == 0) {
            throw new DAOException("Nessuna spesa trovata con ID: " + idSpesa);
        }

    } catch (SQLException e) {
        throw new DAOException("Errore nell'aggiornamento dello stato della spesa: " + e.getMessage());
    } finally {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nella chiusura del PreparedStatement: " + e.getMessage());
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Errore nella chiusura della Connection: " + e.getMessage());
        }
    }
}


// Restituisce tutti gli ID delle spese associate a un cliente
	public static List<Integer> ReadIdSpeseByCliente(int idCliente) throws DAOException, DBConnectionException {
	    List<Integer> listaIdSpese = new ArrayList<>();
	    
	    try {
	        Connection conn = DBManager.getConnection();
	        
	        String query = "SELECT idSpesa FROM Spesa WHERE idCliente = ?";
	        
	        try {
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setInt(1, idCliente);
	            
	            ResultSet result = stmt.executeQuery();
	            
	            while (result.next()) {
	                int idSpesa = result.getInt("idSpesa");
	                listaIdSpese.add(idSpesa);
	            }
	            
	        } catch (SQLException e) {
	            throw new DAOException("Errore nella lettura degli ID spese del cliente con ID: " + idCliente);
	        } finally {
	            DBManager.closeConnection();
	        }
	        
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione al database");
	    }
	    
	    return listaIdSpese;
	}

}


