/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
/**
 *
 * @author bibrak
 */
public class Master extends UnicastRemoteObject implements caller{

    /**
     * @param args the command line arguments
     */

     public methods calls;
     public long dataSet[];
     public int N;
     public int rows;
     public int numWorkers;
     public int chunk;
      private String OPT;
      //private String currentWorker;
     public int synch[];
     private circularList cirList;

     public Master(int n,int c,String op) throws RemoteException {

         super();
         N = n*n;
         rows = n;
         numWorkers = 0;
         chunk = c; // amount of data on which to work
         calls = null;
         dataSet = new long[N];
         synch = new int[1];
         synch[0] = 0;
         OPT = op;
        // currentWorker = cWorker;
         for(int i = 0;i<N;i++){

             dataSet[i] = 1;

         }
         cirList = new circularList();
         populate(); // populate from file

     }

     public methods bind(String ip,methods obj){

     obj = null;
    try{
    obj = (methods) Naming.lookup("rmi://"+ip+"/methods");
    }catch(Exception ex){
        ex.printStackTrace();
    }


         return obj;
     }


     public void distribute(long data[],long sub_data[],int offset, int len){

         sub_data = new long[len];

         for(int i = offset; i < len; i++){

             sub_data[i] = data[i];

         }

     }


     public void writeFile (String ip) {
     try {

             BufferedWriter out = new BufferedWriter (new FileWriter ("workers.txt", true));
             out.write (ip);
             out.newLine ();


             out.close ();

        } catch (IOException e) {
              e.printStackTrace();
            }
    }


     public long[] reduction(long arr[],String opt){


           long sum[] = new long[1];
        if(opt.equals("+")){


        for(int i= 0 ; i <arr.length;i++){
            sum[0] +=arr[i];
        }


        return sum;
        }

        return arr;

     }

       public int alive(String ip) throws RemoteException{


         /*  FileWriter fw = null;

           try{
           fw = new FileWriter("workers.txt");
           }catch(java.io.IOException EX){
               EX.printStackTrace();
           }

           BufferedWriter bw = new BufferedWriter(fw);
           PrintWriter pw = new PrintWriter(bw);
           */
           boolean found = cirList.search(ip);

           if(!found){

           System.out.println("Registering new volunteer "+ip);
          // pw.println(ip);
           this.writeFile(ip);
           cirList.add(ip);
           }else{
                System.out.println("A registered volunteer has appeared online "+ip);
           }

           
         //  pw.close();

           return 1;
       }




     public void setWorkersNum(int num,String opt){

        this.numWorkers = this.cirList.getCount();

       /*if(opt.equals("+"))
        numWorkers = N/this.chunk; // required work
       // chunk = N/numWorkers;
        else if(opt.equals("x"))
        */

        this.chunk = N/this.numWorkers;
           this.synch = new int[1];
         //  this.synch[0] = 0;
     }

     public long[] reduce(String operator){


/////////////////////////////////////////////////////////////////////////
        // Setup the Enviroment

           this.setWorkersNum(1,operator);

           System.out.println("number of workers set: "+numWorkers);
           System.out.println("opt = "+operator);
           long matrixB[] = null;
           methods orders;
            long result[] = null;
           if(operator.equals("+"))
             result = new long [this.numWorkers];
           else if (operator.equals("x")){
             result = new long [this.N];
             matrixB = new long [this.N];

             for(int i = 0 ; i < matrixB.length;i++){
                 matrixB[i] = 1;
             }


           }

////////////////////////////////////////////////////////////////////////////
         //System.out.println("sum arr created ");

            int retry = (int) Math.ceil(this.numWorkers * 2.50); // assume 80% of workers are offline
            System.out.println("max retries = "+retry+" numWorker = "+this.numWorkers);
            
         for(int w = 0;w<this.numWorkers;w++){
          orders = null;
        String ip = this.cirList.traverse(); // read from file
       // ip = this.currentWorker;
 
         // String ip = args[0];
        orders = bind(ip,orders);
        if ( orders == null){
            System.out.println("Can not bind to "+ip+" w = "+w);
          //  return -1;
            w--; // next worker
            retry--;
            continue;
        }
        if(retry == 0){ // terminate

            break;
        }
       // long sub_data [] = null;
        //distribute(dataSet, sub_data, w*chunk, chunk); //this will be called in a thread


        /*
        try{
        //sum[w] = calls.reduce(sub_data,"+"); // function will be called in the same thread of distribute
       sum[w] = calls.reduce(dataSet,"+"); // function will be called in the same thread of distribute
        }catch(java.rmi.RemoteException e){
            e.printStackTrace();
        }*/
         threadValGetter Th = null;
        if(operator.equals("x")){
          Th = new threadValGetter(orders,w,this.chunk,dataSet,result,synch,operator,this.rows);
        }else{
         Th = new threadValGetter(orders,w,this.chunk,dataSet,result,synch,operator,this.N);
        //Th.setDaemon(true);
        }
        if(operator.equals("x")){
        System.out.println("opt = "+operator);
        Th.setEnviroment(matrixB,this.numWorkers);
        }
        Th.start();
        
         }
        // Master.yield();

     //       System.out.println("count in reduce : "+ Master.activeCount());
      /* try{
      //   this.join();
             //Master.sleep(4000);
           this.join();
             //this.yield();
      //   this.
         }catch(java.lang.InterruptedException Ex){
             Ex.printStackTrace();
         }*/

         while(synch[0] != this.numWorkers){
          System.out.println("synch = "+synch[0]+" numWorker = "+this.numWorkers);
          
         }

      /*  for(int i= 0 ; i <result.length;i++){
            System.out.println("Sum "+i+" = "+sum[i]);
        }*/


        System.out.println("All threads done ");
         this.synch[0] = 0;

       //  if(operator.equals("+"))
         return this.reduction(result,operator);
        // else
       //  return result;



        // return sum;
     }


     public void populate() {
         FileReader fr = null;
         BufferedReader br = null;
         String str = null;

         // open file
         try{
         fr = new FileReader("workers.txt");
         br = new BufferedReader(fr);
         }catch(java.io.FileNotFoundException EX){
             EX.printStackTrace();
         }


         // read line
         try{


         while(true){
             str = br.readLine();
             if(str == null){
                 break;
             }
             cirList.add(str);
             
         }

         }catch(java.io.IOException EX){
             EX.printStackTrace();
         }



         // close file
         try{
         br.close();
         }catch(java.io.IOException EX){
             EX.printStackTrace();
         }

         System.out.println("Checking the population - > "+cirList.traverse());


         
     }




     public long[] called(String opt){

         System.out.println("I am in called ");

         long result[] = new long[1];
         this.OPT = opt;

         result = reduce(this.OPT);

         return result;
     }
   /* @Override
     public void run(){

       // String opt = "+";
        System.out.println("The sum of Data Set is "+reduce(this.OPT));

     }*/

    public static void main(String[] args) {
        // TODO code application logic here
       //   System.out.println("count in main before bergouiss : "+ Master.activeCount());

        int ch = 4; // no use
        int x = Integer.parseInt(args[0]);
        //int x = 10; // x is row
       // int size = x*ch;
         Master bourgeoisie = null;
        try{
        bourgeoisie = new Master(x,ch,"+");

         Naming.bind ("caller",bourgeoisie);

        System.out.println ("Bourgeoisie created to oppress ....");
        }catch(java.rmi.RemoteException e){

           e.printStackTrace();

        }catch(java.rmi.AlreadyBoundException a){

           a.printStackTrace();

        }catch(Exception Ex){

           Ex.printStackTrace();

        }
       // String opt = "+";
        //System.out.println("The sum of Data Set is "+bourgeoisie.reduce(opt));
        //bourgeoisie.setDaemon(true);
       // bourgeoisie.start();
      //  bourgeoisie.called("+");
       // this.join();
        // read info about workers

  //  System.out.println(" main is here !!!!! "+bourgeoisie.reduce("+"));
       // Master.yield();
      // System.out.println("count in main : "+ Master.activeCount());
  //      System.out.println(" main is here again !!!!! ");
        // check if worker is alive



        // copy its working part and distribute it.


    }

}
