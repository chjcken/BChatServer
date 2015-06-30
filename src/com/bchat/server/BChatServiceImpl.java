/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bchat.server;

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

    public BChatServiceImpl() {
    }
    
    

    @Override
    public boolean login(String id, String password) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> getFriendList(String userid) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean sendRequest(Request request) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Request> getRequest(String userid) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
