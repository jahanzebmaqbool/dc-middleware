/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package dcproject;

/**
 *
 * @author bibrak
 */
public class node{
    public String IP;
    public node next;
    public node(){
        IP = null;
    }

    public void set(String ip){
        this.IP = ip;
    }

    public String get(){
        return this.IP;
    }
}