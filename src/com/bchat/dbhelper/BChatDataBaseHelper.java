/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bchat.dbhelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author datbt
 */
public class BChatDataBaseHelper {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    public void readDB() throws Exception{
        try{
            //
            Class.forName("com.mysql.jdbc.Driver");
            //
            connect = DriverManager.getConnection("jdbc:mysql://localhost/BChatDB?"
                                                    + "user=root&password=qwe123");
            statement = connect.createStatement();
            
            resultSet = statement.executeQuery("select * from BChatDB.User");
            writeResult(resultSet);
        }
        catch (ClassNotFoundException | SQLException e){
            throw e;
        }
        finally{
            close();
        }
    }
    
    private void writeResult(ResultSet resultSet) throws SQLException{
        String id, pass, name, email;
        while (resultSet.next()){
            id = resultSet.getString("userid");
            pass = resultSet.getString("password");
            name = resultSet.getString("name");
            email = resultSet.getString("email");
            System.out.println("User id: " + id + "\tPass: " + pass + "\tName: " + name + "\tEmail: " + email);
        }
    }
    
    private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }
    
    public static void main(String[] args) throws Exception{
        BChatDataBaseHelper dbHelper = new BChatDataBaseHelper();
        dbHelper.readDB();
    }
}
