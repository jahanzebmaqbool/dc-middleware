/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

/**
 *
 * @author bibrak
 */
public class threadValGetter extends Thread {

    private methods calls;
    private int id;
    private int N;
    private int numWork;
     private String opt;
    private int chunk;
    private long data[];
    private long matrixB[];
    private long matrixC[];
    private long sum[];
    public int syn[];




    public threadValGetter(methods c,int w,int ch,long dat[],long s[],int sync[],String op,int n){
        calls = c;
        id = w;

        data = dat;
        sum = s;
        chunk = ch;
        opt = op;
        syn = sync;
        this.N = n;
    }

    public void setEnviroment(long matrix_B[],int work){
        if(this.opt.equals("x")){
        this.matrixB = matrix_B;
        this.matrixC = new long[this.chunk];
          numWork = work;
        }
    }

    public synchronized void increment(){
        syn[0]++;
        System.out.println("syn = "+syn[0]);
    }

    @Override
    public void run(){


         long dataSet[] = new long[chunk];

         int offset = id * chunk;
         int j = 0;
         for(int i = 0; i <chunk; i++){

             dataSet[i] = data[offset+j];
             j++;
         }

         if(calls == null){
             System.out.println("Calls is null in thread "+id);
         }


       if(this.opt.equals("+")){
          try{
        //sum[w] = calls.reduce(sub_data,"+"); // function will be called in the same thread of distribute
        sum[id] = calls.reduce(dataSet,opt); // function will be called in the same thread of distribute
        }catch(java.rmi.RemoteException e){
            e.printStackTrace();
        }

       }else if(this.opt.equals("x")){

          try{
        //sum[w] = calls.reduce(sub_data,"+"); // function will be called in the same thread of distribute
        this.matrixC = calls.matrixMultiply(dataSet, matrixB,this.N,this.numWork); // function will be called in the same thread of distribute
        }catch(java.rmi.RemoteException e){
            e.printStackTrace();
        }

          //write results in result[], here its sum[]

         j = 0;
         for(int i = 0; i <chunk; i++){

             this.sum[offset+j] = this.matrixC[i];
            // dataSet[i] = data[offset+j];
             j++;
         }




       }


       
        System.out.println("Thread with id "+id+" ends");
       increment();
    }





}
