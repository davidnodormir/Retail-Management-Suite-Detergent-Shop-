package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.EntitySconto;


import exception.DAOException;
import exception.DBConnectionException;

public class ScontoDAO {
	
	//Preleva e restituisce la percentuale di sconto a partire da un id 
	public static float readPercentualeSconto (int idSconto) throws DAOException, DBConnectionException{
		Float Percentuale = (float) 0;
		
		try {
			Connection conn = DBManager.getConnection();
			
			String query = "SELECT Percentuale FROM Sconto WHERE idSconto = ? ;";
			
			try {
				PreparedStatement stmt = conn.prepareStatement(query);
				
				stmt.setInt(1, idSconto);
				
				ResultSet result = stmt.executeQuery(); 
				
				if (result.next()) {
					Percentuale = result.getFloat(1);	
				}
			}catch(SQLException e) {
				throw new DAOException("Errore nella lettura della percentuale di sconto");
			}finally  {
				DBManager.closeConnection();
			}
			
		}catch (SQLException e) {
			throw new DBConnectionException("Errore di connessione al database ");
		}
		
		
		return Percentuale;
	}
	
	// preleviamo informazioni relative allo sconto dal codice univoco
    public static EntitySconto readSconto(String codice) {
        String sql = "SELECT * FROM sconto WHERE idsconto = ?";
        
        try (Connection conn = DBManager.getConnection()) {

        	try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            	stmt.setString(1, codice);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return new EntitySconto(
                        rs.getInt("idSconto"),
                        rs.getFloat("percentuale"),
                        rs.getDate("Scadenza")
                    );
                }
            } catch (SQLException e) {
                System.out.println("Errore durante la ricerca dello sconto con codice " + codice);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database");
            e.printStackTrace();
        }
        return null;
    }
	
	
	
}
