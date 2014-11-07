/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;


import java.rmi.server.*;
import java.rmi.*;

public class Worker extends UnicastRemoteObject implements methods {

    private String Master;
    private boolean online;
    private int count;
    public Worker (String M) throws RemoteException{

       super();
       Master = M;
       count = 0;
       online = false;

    }


     public long[] matrixMultiply(long matrixA[],long matrixB[],int N_size,int numWorkers) throws RemoteException{

         System.out.println("I am in Matrix Multiply Mode");
         int myRows = N_size/numWorkers;
         long result[] = new long[myRows*N_size];
     //    int N = (int) Math.sqrt(N_size);

         for(int i = 0;i<myRows;i++){
             for(int j = 0;j<N_size;j++){
                 result[i*N_size+j] = 0;
                 for(int k = 0;k<N_size;k++){
                     result[i*N_size+j] += matrixA[i*N_size+k] * matrixB[k*N_size+j];
                 }
             }
         }

         count++;
          System.out.println("Job# "+count+" Done : ");

         return result;
     }




    public long reduce(long []dataSet,String opt) throws RemoteException{

         System.out.println("I am in reduce ");

        long sum = -101010;
        if(opt.equals("+")){
           sum = sum(dataSet); 
        }


         count++;
          System.out.println("Job# "+count+" Done : ");

        return sum;
    }


    public boolean alive(){

        java.net.InetAddress i = null;
        try{
        i = java.net.InetAddress.getLocalHost();
        }catch(java.net.UnknownHostException EX){
            EX.printStackTrace();
        }


        String IP = i.getHostAddress();

        System.out.println("My IP = "+IP+" Master = "+this.Master);

        caller call = null;
        
        try{
             call = (caller) Naming.lookup("rmi://"+this.Master+"/caller");
          }catch(Exception ex){
        ex.printStackTrace();
        }

        if(call == null ){
            System.out.println("call is null worker ");
        }
         int respond = 0;
        try{
        respond = call.alive(IP);
        }catch(java.rmi.RemoteException EX){
            EX.printStackTrace();
        }

         if(respond == 1){
             this.online = true;
         }else{
             this.online = false;
         }

         return this.online;
       
    }


    public long sum(long []dataSet){

        long sum1 = 0;
          for(int i= 0 ; i <dataSet.length;i++){
            sum1 +=dataSet[i];
        }
        return sum1;
    }

    public static void main(String []args){ //args[0] = master


        Worker proletariat = null;

        try{

        //args[0] = master ip address
        proletariat = new Worker(args[0]);

        Naming.bind ("methods", proletariat);

        System.out.println ("Proletariat created to be oppressed ....");
        }catch(java.rmi.RemoteException e){

           e.printStackTrace();

        }catch(java.rmi.AlreadyBoundException a){

           a.printStackTrace();

        }catch(Exception Ex){

           Ex.printStackTrace();

        }

        // check if registered

        boolean check = proletariat.alive();

        if(!check){
            System.exit(1);
        }

        System.out.println("I have been registered with Master "+args[0]);

    }


}
