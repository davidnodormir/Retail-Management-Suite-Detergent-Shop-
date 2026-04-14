package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import exception.DAOException;
import exception.DBConnectionException;
import entity.EntityProdotto;

public class ProdottoDAO {
	
	//Restituisce una lista, eventualmente vuota, di prodotti già presenti nel database con lo stesso nome (case-insensitive)
    public static ArrayList<EntityProdotto> trovaProdottoPerNome(String nome) throws DAOException, DBConnectionException {
        if (nome == null || nome.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        ArrayList<EntityProdotto> prodottiTrovati = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        
        try {
            conn = DBManager.getConnection();
            String query = "SELECT * FROM PRODOTTO WHERE LOWER(Nome) = LOWER(?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nome.trim());
            result = stmt.executeQuery();
            
            while (result.next()) {
                EntityProdotto eP = new EntityProdotto(
                    result.getInt("CODICE"),
                    result.getString("NOME"),
                    result.getFloat("PREZZO"),
                    result.getString("DESCRIZIONE"),
                    result.getInt("QTADISPONIBILE")
                );
                prodottiTrovati.add(eP);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Errore lettura prodotto");
        } finally {
            chiudiRisorse(result, stmt, conn);
        }
        
        return prodottiTrovati;
    }
    
    //aggiunge nuovo Prodotto al database popolando i campi prelevati dall'istanza eP
    public static void createProdotto(EntityProdotto eP) throws DAOException, DBConnectionException {
        if (eP == null) {
            throw new DAOException("Prodotto non valido");
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBManager.getConnection();
            String query = "INSERT INTO PRODOTTO (NOME, DESCRIZIONE, PREZZO, QTADISPONIBILE) VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(query);
            
            stmt.setString(1, eP.getNome());
            stmt.setString(2, eP.getDescrizione());
            stmt.setFloat(3, eP.getPrezzo());
            stmt.setInt(4, eP.getQtaDisponibile());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DAOException("Errore scrittura prodotto");
        } finally {
            chiudiRisorse(null, stmt, conn);
        }
    }
    
    //Aggiorna prodotto già esistente nel database
    public static boolean updateProdotto(EntityProdotto eP) throws DAOException, DBConnectionException {
        if (eP == null) {
            throw new DAOException("Prodotto non valido");
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBManager.getConnection();
            String query = "UPDATE PRODOTTO SET DESCRIZIONE = ?, PREZZO = ?, QTADISPONIBILE = ? WHERE CODICE = ?";
            stmt = conn.prepareStatement(query);
            
            stmt.setString(1, eP.getDescrizione());
            stmt.setFloat(2, eP.getPrezzo());
            stmt.setInt(3, eP.getQtaDisponibile());
            stmt.setInt(4, eP.getCodice());
            
            int righeAggiornate = stmt.executeUpdate();
            return righeAggiornate > 0;
            
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento prodotto");
        } finally {
            chiudiRisorse(null, stmt, conn);
        }
    }
    
    
    //Preleva la quantità di prodotti disponibili all'interno del magazzino.
	
	public static int readQtaDisponibile(int idProdotto) throws DAOException, DBConnectionException{
		int QtaDisp = 0;
		try {
			Connection conn = DBManager.getConnection();
			
			String query = "SELECT QtaDisponibile FROM Prodotto WHERE CODICE = ? ;";
			
			try {
				
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, idProdotto); 
				
				ResultSet result = stmt.executeQuery();
				
				if (result.next()) {
					QtaDisp = result.getInt(1);
				}
				
			} catch (SQLException e) {
				throw new DAOException("Errore nella lettura della quantità disponibile");
			}finally {
				DBManager.closeConnection();
			}
		}catch (SQLException e) {
			throw new DBConnectionException("Errore connessione database");
			
		} 
		return QtaDisp;
	}
	
	//Preleva e restituisce il prezzo di un prodotto
	public static float readPrezzo (int idProdotto) throws DAOException, DBConnectionException {
		float prezzo = 0.0f;

	    try {
	        Connection conn = DBManager.getConnection();

	        String query = "SELECT Prezzo FROM Prodotto WHERE CODICE = ?";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, idProdotto);

	            try (ResultSet result = stmt.executeQuery()) {
	                if (result.next()) {
	                    prezzo = result.getFloat(1);
	                }
	            }
			}catch (SQLException e) {
				throw new DAOException("Errore nella lettura del prezzo");
			} finally {
				DBManager.closeConnection();
			}
		} catch(SQLException e) {
			throw new DBConnectionException("Erore connessione al database ");
		}
		
		return prezzo;
		
	}
	
	//Restituisce un singolo EntityProdotto    
    public static EntityProdotto readProdotto(int idProdotto) throws DAOException, DBConnectionException {
	    EntityProdotto ep = null;

	    try {
	        Connection conn = DBManager.getConnection();

	        String query = "SELECT * FROM Prodotto WHERE codice = ?";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, idProdotto);  
	            try (ResultSet result = stmt.executeQuery()) {
	                if (result.next()) {
	                    String nome = result.getString("Nome");
	                    String descrizione = result.getString("Descrizione");
	                    float prezzo = result.getFloat("Prezzo");
	                    int qtaDisponibile = result.getInt("QtaDisponibile");

	                    ep = new EntityProdotto(idProdotto, nome, prezzo, descrizione, qtaDisponibile);
	                }
	            }
	        } catch (SQLException e) {
	            throw new DAOException("Errore nella lettura del prodotto: " + e.getMessage());
	        } finally {
	            DBManager.closeConnection();
	        }

	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore connessione database: " + e.getMessage());
	    }

	    return ep;
	}
	/**Aggiorna la quantità disponibile di un prodotto con una query di update
		 * @param idDel prodotto da aggiornare
		 * @param nuovaQta la nuoa quantità da inserire */
		public static void aggiornaQtaDisponibile(int idProdotto, int nuovaQta) throws DAOException, DBConnectionException  {
		    try {
		        Connection conn = DBManager.getConnection();
		        
		        String query = "UPDATE Prodotto SET QtaDisponibile = ? WHERE CODICE = ?;";
		        
		        try (PreparedStatement stmt = conn.prepareStatement(query)) {
		            stmt.setInt(1, nuovaQta);
		            stmt.setInt(2, idProdotto);
		            stmt.executeUpdate(); 
		        } catch (SQLException e) {
		            throw new DAOException("Errore nell'aggiornamento della quantità disponibile");
		        } finally {
		            DBManager.closeConnection();
		        }
		    } catch(SQLException e) {
		        throw new DBConnectionException("Errore connessione al database");
		    }
		}

    
    private static void chiudiRisorse(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {}
        
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {}
        
        try {
            if (conn != null) DBManager.closeConnection();
        } catch (SQLException e) {}
    }
}