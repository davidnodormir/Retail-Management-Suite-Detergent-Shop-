package database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;


import exception.DAOException;
import exception.DBConnectionException;
import entity.EntityDettaglioSpesa;

public class DettaglioSpesaDAO {
	
	//Restituisce una lista dei prodotti agginti al carrello distinguendoli per l'idSpesa
	
	public static List<EntityDettaglioSpesa> readProdottiSpesa(int idSpesa) throws DAOException, DBConnectionException {

    List<EntityDettaglioSpesa> lp = new ArrayList<>();

    try {
        Connection conn = DBManager.getConnection();

        
        String query = "SELECT IDPROD, QTACARRELLO FROM DETTAGLIOSPESA WHERE IDSPESA = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idSpesa);

            try (ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    // Usa i nomi corretti dei campi dalla query
                    int idProdotto = result.getInt("IDPROD");        // Nome corretto dalla query
                    int qta = result.getInt("QTACARRELLO");          // Nome corretto dalla query

                    EntityDettaglioSpesa pc = new EntityDettaglioSpesa(idSpesa, idProdotto, qta);
                    lp.add(pc);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nella lettura della spesa: " + e.getMessage());
        } finally {
            DBManager.closeConnection();
        }
    } catch (SQLException e) {
        throw new DBConnectionException("Errore connessione al database: " + e.getMessage());
    }

    return lp;
}
	
	public static boolean productExists(int codiceProdotto, String idSpesa) {
        String sql = "SELECT COUNT(*) FROM dettagliospesa WHERE idspesa = ? AND idprod = ?";
        
        try {
	    	
	    	Connection conn = DBManager.getConnection();
	    	
	        try {
	        	
	        	PreparedStatement stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, idSpesa);
                stmt.setInt(2, codiceProdotto);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
                
            } catch (SQLException e) {
                System.out.println("Errore durante la verifica dell'esistenza del prodotto " + codiceProdotto + " nella spesa " + idSpesa);
                e.printStackTrace();
            } finally {
				DBManager.closeConnection();
			}
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database");
            e.printStackTrace();
        }
        
        return false;
    }

    public static void createDettaglioSpesa(int idSpesa, int codiceProdotto, int quantitaRichiesta) {
        String sql = "INSERT INTO DettaglioSpesa (idspesa, idprod, QtaCarrello) VALUES (?, ?, ?)";

        try {
	    	
	    	Connection conn = DBManager.getConnection();
	    	
	        try {
	        	
	        	PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setInt(1, idSpesa);
                stmt.setInt(2, codiceProdotto);
                stmt.setInt(3, quantitaRichiesta);
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Errore durante l'inserimento del dettaglio spesa");
                e.printStackTrace();
            } finally {
				DBManager.closeConnection();
			}
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database");
            e.printStackTrace();
        }
    }

    public static void updateDettaglioSpesa(String IDSpesa, String codiceProdotto, int quantitaModificata) {
        String sql = "UPDATE DettaglioSpesa SET QtaCarrello = ? WHERE idSpesa = ? AND idProd = ?";

        try {
	    	
	    	Connection conn = DBManager.getConnection();
	    	
	        try {
	        	
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	        	
                stmt.setInt(1, quantitaModificata);
                stmt.setInt(2, Integer.parseInt(IDSpesa));
                stmt.setInt(3, Integer.parseInt(codiceProdotto));
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Errore durante l'aggiornamento del dettaglio spesa");
                e.printStackTrace();
            } finally {
				DBManager.closeConnection();
			}
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database");
            e.printStackTrace();
        }
    }
    

    
	
}
