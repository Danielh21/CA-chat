package server;

import java.util.ArrayList;

public class Decoder {

//    private String cmd;
    private ArrayList<String> connectedUsers;
    private String msg;
    private String[] splitString;
    
    String result;
    ArrayList<String> recipientsList;

    public Decoder() {

    }
    
    public void splitLine(String line){
        String[] words = line.split(":");
        String cmd = words[0];
        
        switch(cmd.toUpperCase()){
            case "LOGIN":
                String username = words[1];
                //new client to connect
                //send all the clients
                break;
                
            case "MSG":
                ArrayList<String> recipients = null;
                String recipient = words[1];
                String msg = words[2];
                String[] list = recipient.split(",");
                //add the list of recipients who will receive the message
                for (int i = 0; i < list.length; i++) {
                    recipients.add(list[i]);
                }
                sendMsg(recipients,msg);
                break;
                
            case "LOGOUT":
                //check current user and close the connection
                //send all the clients an update who loggedout
                break;
                
            default: 
                result = "Unrecognized command!";
                break;
        }
    }

//    public String getCmd() {
//        return cmd;
//    }

//    private void login() {
//        connectedUsers.add(splitString[1]);
//        for (Object connectedUser : connectedUsers) {
//            System.out.println(connectedUser.toString());
//        }
//    }

    public String sendMsg(ArrayList<String> recipients, String msg) {
        String mesRes="";
        //check if the recipients is null, if null.. it needs to send to all
        if(recipients==null){
            for (String user : connectedUsers) {
                recipients.add(user);
            }
        }
        //holding the recipients list in the recipients arrayList
        for (String recipient : recipients) {
            recipientsList.add(recipient);
        }
        mesRes = "MESRES:" + getUser() + ":" + msg;
        System.out.println(mesRes);
        return mesRes;
    }

    private void logout() {

    }

    public String getUser() {
        //It has to determine the username corresponds to that Thread
        String currentUser = Thread.currentThread().getName();
        return currentUser;
    }
    
    public ArrayList<String> getRecipients(){
        return recipientsList;
    }

//    public static void main(String[] args) {
//        Decoder d = new Decoder();
//        String line = "MSG::This is just a trial!";
//        d.splitLine(line);
//        System.out.println("Recipients:" + d.getRecipients());
//    }
}
