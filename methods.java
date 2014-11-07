/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

import java.rmi.*;

/**
 *
 * @author bibrak
 */
public interface methods extends java.rmi.Remote {

    /**
     * @param args the command line arguments
     */
    public long reduce(long dataSet[],String operator) throws RemoteException;
    public long[] matrixMultiply(long matrixA[],long matrixB[],int N,int numw) throws RemoteException;


}
