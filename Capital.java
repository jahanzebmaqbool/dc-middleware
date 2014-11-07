/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
//package dcproject;

/**
 *
 * @author bibrak
 */
public class Capital {






    public static caller bind(String ip){

     caller obj;
     obj = null;
          try{
    obj = (caller) Naming.lookup("rmi://"+ip+"/caller");
    }catch(Exception ex){
        ex.printStackTrace();
    }


         return obj;

     }



public static void main(String args[]){

    String Master = args[0];
     System.out.println("Operation = "+args[1]);
    caller CALLER = null;//= bind(Master);
          try{
    CALLER = (caller) Naming.lookup("rmi://"+Master+"/caller");
    }catch(Exception ex){
        ex.printStackTrace();
    }

    if(CALLER == null){
         System.out.println(" CALLER NULL ");
    }




    long result[] = null;
    try{


    result = CALLER.called(args[1]);
    }catch(java.rmi.RemoteException EX){
        EX.printStackTrace();
    }

    if(args[1].equals("+"))
    System.out.println("Result of "+args[1]+" = "+result[0]);
    else if(args[1].equals("x")){
        int N = (int) Math.sqrt(result.length);
        for(int i = 0;i<N;i++){
            System.out.println("");
            for(int j = 0;j<N;j++){
                System.out.print(result[i*N+j]+"  ");
            }

        }

    }

}



}

