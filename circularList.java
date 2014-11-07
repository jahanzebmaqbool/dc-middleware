/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

/**
 *
 * @author bibrak
 */



public class circularList {


    private int count;
    public node head;
    public node tail;
    public node current;


    public circularList(){
        count = 0;
        head = null;
        tail = null;
        current = null;
    }

    public int getCount(){
        return this.count;
    }


    public boolean search(String ip){
        boolean found = false;

        if(this.count > 0){

            node temp = head;

            for(int i = 0; i<this.count;i++){
                if(temp.IP.equals(ip)){
                    found = true;
                    return true;
                }
               temp = temp.next;
            }

        }

        return found;

    }

    public void add(String ip){

        if(this.count == 0){
            head = new node();
            head.set(ip);
            tail = head;
            head.next = head; // circular
            current = head;
            count++;
            System.out.println("IP = "+ip+" count  ="+count);

        }else{
           // node temp = tail;
           node temp = new node();
           temp.set(ip);
           temp.next =  head;
           tail.next = temp;
           count++;
           System.out.println("IP = "+ip+" count  ="+count);
            
        }


    }

    public String traverse(){

     /*   if(count == 1){
            return this.current.get();
        }*/

        this.current = this.current.next;
        String temp =  this.current.get();
      //  return this.current.get();
        System.out.println("current is "+temp);
       return temp;
    }

}
