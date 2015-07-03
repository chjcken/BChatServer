/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bchat.dbhelper;
import com.bchat.code.RequestCode;
import com.bchat.service.Account;
import com.bchat.service.Request;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author datbt
 */
public class BChatDataBaseHelper {
    private final String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/bchatdb";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "qwe123";
    
    
    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
   public BChatDataBaseHelper(){
       open();//open connection to db      
   }

    
    public boolean authenticateUserLogin(String userid, String password) {
        boolean authenticateResult = false;
        String sql = "select * from bchatdb.user where userid=? and password=?";
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
            //throw e;
        }
        
        return authenticateResult;        
    }
    
    public ArrayList<Account> getFriendList(String userid) {
        ArrayList<Account> friendList = new ArrayList<>();
        try {
            
            String sql = "select user.* from bchatdb.user, bchatdb.friends where friends.userid=? and friends.friendid=user.userid";
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
        } catch (SQLException ex) {
            friendList = null;
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friendList;
    }
    
    public ArrayList<Request> getRequestList(String receiver){
        ArrayList<Request> requestList = new ArrayList<>();
        try {
            
            String sql = "select * from  bchatdb.request, bchatdb.user where request.receiverid=? and request.senderid=user.userid";
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
        } catch (SQLException ex) {
            requestList = null;
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return requestList;
    }
    
    public boolean insertRequest(Request request){
        boolean insertStatus = false;
        try {
            
            String sql = "insert into bchatdb.request values (default, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(request.getRequestCode()));
            preparedStatement.setString(2, request.getSender().getId());
            preparedStatement.setString(3, request.getReceiver());
            preparedStatement.setString(4, request.getMessage());
            preparedStatement.executeUpdate();
            insertStatus = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertStatus;
    }
    
    
    public boolean deleteChatRequest(String receiverid) {
        boolean deleleStatus = false;
        try {
            
            String sql = "delete from bchatdb.request where requestcode=? and receiverid=?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(RequestCode.CHAT_MESSAGE));
            preparedStatement.setString(2, receiverid);
            preparedStatement.executeUpdate();
            deleleStatus = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleleStatus;
    }
    
    public boolean deleteFriendRequest(Request request){
        boolean deleteStatus = false;
        try {
            String sql = "delete from bchatdb.request where requestcode=? and senderid=? and receiverid=?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(RequestCode.FRIEND_REQUEST));
            preparedStatement.setString(2, request.getSender().getId());
            preparedStatement.setString(3, request.getReceiver());
            preparedStatement.executeUpdate();
            deleteStatus = true;
        } catch (Exception e) {
        }
        return deleteStatus;
    }
    
    public boolean insertFriends(String user1id, String user2id){
        boolean insertStatus = false;
        try {
            
            String sql = "insert into bchatdb.friends values (default, ?, ?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, user1id);
            preparedStatement.setString(2, user2id);
            preparedStatement.executeUpdate();
            
            preparedStatement.setString(1, user2id);
            preparedStatement.setString(2, user1id);
            preparedStatement.executeUpdate();
            insertStatus = true;
        } catch (SQLException ex) {
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return insertStatus;
    }
    
    public boolean deleteFriend(String userid, String friendid){
        boolean deleteStatus = false;
        try {
            String sql = "delete from bchatdb.friends where userid=? and friendid=?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, friendid);
            preparedStatement.executeUpdate();
            deleteStatus = true;
        } catch (SQLException ex) {
            Logger.getLogger(BChatDataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deleteStatus;
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
        //dbHelper.authenticateUserLogin("user1", "123456");
        dbHelper.getFriendList("user1");
        //dbHelper.getRequestList("user2");
        //Request req = new Request(2, new Account("user3", "", "", ""), "user4", "");
        //dbHelper.insertRequest(req);
        //dbHelper.deleteFriendRequest(req);
    }
}
