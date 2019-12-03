/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbinformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.json.JsonObject;

/**
 *
 * @author Zvi
 */
public class SupplierData implements SupplierDataInterface {
    
    private final Connection conn;
    
    public SupplierData(Connection conn) {
        this.conn = conn;
    }
    
     /**
     * Gets all the entries from a chosen table with the specified columns 
     * from the database.
     * @param columns table columns to receive.
     * @param tableName name of table to receive.
     * @return a ResultSet with all table rows and chosen fields.
     * @throws SQLException 
     */
    private ResultSet getTableColumns(String columns, String tableName) throws SQLException {
        String query = "SELECT " + columns + " FROM " + tableName;
        ResultSet rs;
        try (PreparedStatement pstat = conn.prepareStatement(query)) { 
            rs = pstat.executeQuery();            
        }
        catch(SQLException ex) {
            throw ex;
        }
        return rs;
    }

    @Override
    public ResultSet getCompanyPositions() throws SQLException {
        return getTableColumns("*", "company_positions");
    }

    @Override
    public ResultSet getSupplyCategories() throws SQLException {
        return getTableColumns("*", "supply_category");
    } 

    @Override
    public ResultSet getCitiesAndCountries() throws SQLException{
        String query = "SELECT * " + 
                "FROM cities " +
                "NATURAL JOIN countries";
        ResultSet rs;
        try (PreparedStatement pstat = conn.prepareStatement(query)) { 
            rs = pstat.executeQuery();            
        }
        catch(SQLException ex) {
            throw ex;
        }
        return rs;
    }

    @Override
    public int addNewSupplier(String suplierName) throws SQLException{
        String insertCompany = "INSERT INTO companies (ID) VALUES (null)";
        String insertSupplier = "INSERT INTO suppliers (companyID, name) VALUES (?, ?)"; 
        PreparedStatement pstatCompany = null;
        PreparedStatement pstatSupplier = null;
        try {            
            pstatCompany = conn.prepareStatement(insertCompany, Statement.RETURN_GENERATED_KEYS);
                        
            conn.setAutoCommit(false);
            if(pstatCompany.executeUpdate() == 0) {
                return -1;
            }
            ResultSet keys = pstatCompany.getGeneratedKeys();
            keys.next();
            int companyID = keys.getInt(1);           
                 
            pstatSupplier = conn.prepareStatement(insertSupplier);            
            pstatSupplier.setInt(1, companyID);
            pstatSupplier.setString(2, suplierName);
            if(pstatSupplier.executeUpdate() == 0) {
                return -1;
            }
            conn.commit();
            
            return companyID;
        } 
        catch(SQLException ex) {
            if(conn != null) {
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            }
            throw ex;
        }
        finally {
            if(pstatCompany != null)
                pstatCompany.close();
            if(pstatSupplier != null)
                pstatSupplier.close();
        }
    }

    @Override
    public int addSupplierCategory(int supplierID, int[] supplyCategories) throws SQLException{
        String query = "INSERT INTO category_supplier (companyID, categoryID) VALUES (?, ?)";
        int updatedRows = 0;
        try (PreparedStatement pstat = conn.prepareStatement(query)) { 
            for(int category: supplyCategories) {
                pstat.setInt(1, supplierID);
                pstat.setInt(2, category);
                if(pstat.executeUpdate() != 0) {
                    updatedRows++;
                }          
            }
            return updatedRows;
        }
        catch(SQLException ex) {
            throw ex;
        }
    }

    @Override
    public boolean updateCompanyInfo(int companyID, JsonObject legalInfo, JsonObject contactDetails)  throws SQLException{
        String query = "UPDATE compenies "
                + "SET legalInfo = ?, contactDetails = ? "
                + "WHERE companyID = ? ";
        try (PreparedStatement pstat = conn.prepareStatement(query)){            
            
            pstat.setObject(1, legalInfo);
            pstat.setObject(2, contactDetails);
            pstat.setInt(3, companyID);            
            
            return pstat.executeUpdate() != 0;            
        } 
        catch(SQLException ex) {
            throw ex;
        }
    }

    @Override
    public int addCompanyContact(int companyID, String contactName, int positionID) throws SQLException {
        String insertPerson = "INSERT INTO person (name) VALUES (?)";
        String insertContact = "INSERT INTO suppliers (personID, companyID, position) VALUES (?, ?, ?)"; 
        PreparedStatement pstatPerson = null;
        PreparedStatement pstatContact = null;
        try {            
            pstatPerson = conn.prepareStatement(insertPerson, Statement.RETURN_GENERATED_KEYS);
                        
            conn.setAutoCommit(false);
            if(pstatPerson.executeUpdate() == 0) {
                return -1;
            }
            ResultSet keys = pstatPerson.getGeneratedKeys();
            keys.next();
            int personID = keys.getInt(1);           
                 
            pstatContact = conn.prepareStatement(insertContact);            
            pstatContact.setInt(1, personID);
            pstatContact.setInt(2, companyID);
            pstatContact.setInt(3, positionID);
            if(pstatContact.executeUpdate() == 0) {
                return -1;
            }
            conn.commit();
            
            return personID;
        } 
        catch(SQLException ex) {
            if(conn != null) {
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            }
            throw ex;
        }
        finally {
            if(pstatPerson != null)
                pstatPerson.close();
            if(pstatContact != null)
                pstatContact.close();
        }
    }

    @Override
    public boolean updatePersonID(int personID, JsonObject IdInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
