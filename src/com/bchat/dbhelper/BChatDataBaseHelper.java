/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bchat.dbhelper;
import com.bchat.service.Account;
import com.bchat.service.Request;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author datbt
 */
public class BChatDataBaseHelper {
    private final String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/BChatDB";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "qwe123";
    
    
    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
   
    
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
    
    public boolean authenticateUserLogin(String userid, String password) throws Exception{
        boolean authenticateResult = false;
        String sql = "select * from BChatDB.User where userid=? and password=?";
        try{

            preparedStatement = connect.prepareStatement(sql);
            
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, password);
            
            resultSet = preparedStatement.executeQuery();
            
            
            if (resultSet.last()) 
                authenticateResult = true;
            //if (resultSet.next())
            //System.out.println("user: " + resultSet.getString("userid") + "\tname: " + resultSet.getString("password"));
        }
        catch (SQLException e){
            throw e;
        }
        
        return authenticateResult;        
    }
    
    public ArrayList<Account> getFriendList(String userid) throws SQLException{
        ArrayList<Account> friendList = new ArrayList<>();
        String sql = "select User.* from BChatDB.User, BChatDB.Friends where Friends.userid=? and Friends.friendid=User.userid";
        preparedStatement = connect.prepareStatement(sql);
        //resultSet = statement.executeQuery("select User.* from BChatDB.User, BChatDB.Friends where Friends.userid=\"" + userid + "\" and Friends.friendid=User.userid");
        preparedStatement.setString(1, userid);
        
        resultSet = preparedStatement.executeQuery();
        String id, pass, name, email;
        while (resultSet.next()){
            id = resultSet.getString("userid");
            pass = resultSet.getString("password");
            name = resultSet.getString("name");
            email = resultSet.getString("email");
            System.out.println("User id: " + id + "\tPass: " + pass + "\tName: " + name + "\tEmail: " + email);
            friendList.add(new Account(id, pass, name, email));
        }
        return friendList;
    }
    
    public ArrayList<Request> getRequestList(String receiver) throws SQLException{
        ArrayList<Request> requestList = new ArrayList<>();
        String sql = "select * from  BChatDB.Request, BChatDB.User where Request.receiverid=? and Request.senderid=User.userid";
        preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1, receiver);
        
        resultSet = preparedStatement.executeQuery();
        String senderId, senderName, senderEmail,message;
        int requestCode;
        while(resultSet.next()){
            requestCode = resultSet.getInt("requestcode");
            senderId = resultSet.getString("userid");
            senderName = resultSet.getString("name");
            senderEmail = resultSet.getString("email");
            message = resultSet.getString("message");
            Account sender = new Account(senderId, "", senderName, senderEmail);
            Request request = new Request(requestCode, sender, receiver, message);
            requestList.add(request);
            System.out.println("Code: "+requestCode+"\tSender ID: "+senderId+"\tSender name: "+senderName+"\tMessage: "+message);
            
        }
        return requestList;
    }
    
    public boolean insertRequest(Request request) throws SQLException{
        boolean insertStatus = false;
        String sql = "insert into BChatDB.Request values (default, ?, ?, ?, ?)";
        preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(request.getRequestCode()));
        preparedStatement.setString(2, request.getSender().getId());
        preparedStatement.setString(3, request.getReceiver());
        preparedStatement.setString(4, request.getMessage());
        preparedStatement.executeUpdate();
        return insertStatus;
    }
    
    private void open(){
        try {
            Class.forName(DB_DRIVER_CLASS);
          
            connect = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }
        catch (ClassNotFoundException | SQLException e){
            System.err.println("Error at open(): " + e.getMessage());
        }
    }
    
    private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (preparedStatement != null) {
        preparedStatement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {
        System.err.println("Error at close():" + e.getMessage());
    }
  }
    
    public static void main(String[] args) throws Exception{
        BChatDataBaseHelper dbHelper = new BChatDataBaseHelper();
        dbHelper.open();
        //dbHelper.authenticateUserLogin("user1", "123456");
        //dbHelper.getFriendList("user1");
        //dbHelper.getRequestList("user2");
        Request req = new Request(123, new Account("user4", "", "", ""), "user3", "insert message");
        dbHelper.insertRequest(req);
    }
}
