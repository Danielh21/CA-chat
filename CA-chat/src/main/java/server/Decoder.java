package server;

import java.util.ArrayList;

public class Decoder {

//    private String cmd;
    private ArrayList<String> connectedUsers;
    private String msg;
    private String[] splitString;
    private String currentUser;

    String result = "Unrecognized command!";
    ArrayList<String> recipientsList;

    public Decoder() {

    }

    public void splitLine(String line) {
        String[] words = line.split(":");
        String cmd = words[0];
        try {
            
            switch (cmd.toUpperCase()) {
                
                case "MSG":
                    ArrayList<String> recipients = new ArrayList<>();
                    String recipient = words[1];
                    String msg = words[2];
                    String[] list = recipient.split(",");
                    //add the list of recipients who will receive the message
                    if (!recipient.equals("") && list.length >= 1) {
                        recipients.add(currentUser);
                        for (int i = 0; i < list.length; i++) {
                            recipients.add(list[i].toUpperCase());
                        }
                    }
                    String response = generateResponse(recipients, msg);
                    if (recipients.isEmpty()) {
                        ChatServer.sendToAllClients(response);
                    } else {
                        ChatServer.sendToSomeClients(recipients, response);
                    }
                    break;
                
                default:
                    break;
            }
        } catch (Exception e) {
            ArrayList<String> rec = new ArrayList<>();
            rec.add(currentUser.toUpperCase());
            ChatServer.sendToSomeClients(rec, result);
        }
    }

    public String generateResponse(ArrayList<String> recipients, String msg) {
        String mesRes = "";
        mesRes = "MSGRES:" + currentUser + ":" + msg;
        System.out.println(mesRes);
        return mesRes;
    }


    public void setCurrentUserName(String username) {
        currentUser = username;
    }
}
