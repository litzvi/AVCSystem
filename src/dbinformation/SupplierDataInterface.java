/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbinformation;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.json.JsonObject;

/**
 *
 * @author Zvi
 */
interface SupplierDataInterface {
    
    /**
     * Get the list of company position types and there respective table id.
     * @return ResultSet table with types of positions and there table id.
     */
    ResultSet getCompanyPositions() throws SQLException;
    
    /**
     * Get the list of supply categories and there respective table id.
     * @return ResultSet table with supply categories and there table id.
     */
    ResultSet getSupplyCategories() throws SQLException ;    
 
    /**
     * Get a table with cities, city id, country and country id.
     * @return ResultSet table with city, city id, country and country id.
     */
    ResultSet getCitiesAndCountries()throws SQLException;
    
    /**
    * Adds a new supplier to the database.
    * @param suplierName unique name of supplier.
    * @return supplier id number.
    */
    int addNewSupplier(String suplierName) throws SQLException;
    
    /**
    * Adds the categories of items supplied by given supplier.
    * @param supplierID id of supplier.
    * @param supplyCategories list of IDs of supply categories.
    * @return number of categories added.
    */
    int addSupplierCategory(int supplierID, int[] supplyCategories) throws SQLException;
    
    /**
    * Updates information for a given company.
    * @param companyID the company to update.
    * @param legalInfo legal names, tax code, license number of the company, could be null.
    * @param contactDetails address, phone, etc. associated with the company, could be null.
    * @return true if information was successfully updated.
    */
    boolean updateCompanyInfo(int companyID, JsonObject legalInfo, JsonObject contactDetails) throws SQLException;
    
    /**
    * Adds a contact person for a given company.
    * @param companyID the company to add a contact to.
    * @param contactName name of contact.
    * @param position position of the contact in the company, could be null.
    * @return contact person id.
    */
    int addCompanyContact(int companyID, String contactName, int positionID) throws SQLException;
    
    /**
    * Updates id details for a person - ID number, date of birth, date of issue, place of issue.
    * @param personID the person table id.
    * @param IdInfo id information - ID number, date of birth(optional alone), 
    * date of issue(optional), place of issue(optional).
    * @return true if update successful.
    */
    boolean updatePersonID(int personID, JsonObject IdInfo) throws SQLException;
}
