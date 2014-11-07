/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

/**
 *
 * @author bibrak/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

import java.rmi.*;

/**
 *
 * @author bibrak
 */

public interface caller extends java.rmi.Remote {
  public long[] called(String operator) throws RemoteException;
    public int alive(String ip) throws RemoteException;

}
