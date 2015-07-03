/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bchat.server;

import com.bchat.code.RequestCode;
import com.bchat.dbhelper.BChatDataBaseHelper;
import com.bchat.service.Account;
import com.bchat.service.Chatter;
import com.bchat.service.Request;
import java.util.List;
import org.apache.thrift.TException;

/**
 *
 * @author datbt
 */
public class BChatServiceImpl implements Chatter.Iface{

    private final BChatDataBaseHelper dbHelper;
    public BChatServiceImpl() {
        dbHelper = new BChatDataBaseHelper();
    }
    
    

    @Override
    public boolean login(String id, String password) throws TException {
        boolean loginStatus = dbHelper.authenticateUserLogin(id, password);
       
        return loginStatus;
    }

    @Override
    public List<Account> getFriendList(String userid) throws TException {
        List<Account> friendList = dbHelper.getFriendList(userid);
        return friendList;
    }

    @Override
    public boolean sendRequest(Request request) throws TException {
        boolean sendStatus = false;
        switch (request.getRequestCode()){
            case RequestCode.FRIEND_ACCEPT:
                sendStatus = (dbHelper.insertFriends(request.getSender().getId(), request.getReceiver())
                                && dbHelper.deleteFriendRequest(request));
                break;
                
            case RequestCode.FRIEND_DENY:
                sendStatus = dbHelper.deleteFriendRequest(request);
                break;
                
            case RequestCode.FRIEND_DELETE:
                sendStatus = dbHelper.deleteFriend(request.getSender().getId(), request.getReceiver());
                break;
                
            case RequestCode.CHAT_MESSAGE:
            case RequestCode.FRIEND_REQUEST:
                sendStatus = dbHelper.insertRequest(request);
                break;
            default:
                break;
        }
        
         
        return sendStatus;
    }

    @Override
    public List<Request> getRequest(String userid) throws TException {
        List<Request> requestList = dbHelper.getRequestList(userid);
        if (requestList != null && !requestList.isEmpty()){
            dbHelper.deleteChatRequest(userid);
        }
        return requestList;
    }
    
}
