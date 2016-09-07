package server;

import java.util.ArrayList;

public class Decoder {

//    private String cmd;
    private ArrayList<String> connectedUsers;
    private String msg;
    private String[] splitString;
    private String currentUser;

    String result;
    ArrayList<String> recipientsList;

    public Decoder() {

    }

    public void splitLine(String line) {
        String[] words = line.split(":");
        String cmd = words[0];

        switch (cmd.toUpperCase()) {

            case "MSG":
                ArrayList<String> recipients = new ArrayList<>();
                String recipient = words[1];
                String msg = words[2];
                String[] list = recipient.split(",");
                //add the list of recipients who will receive the message
                if (list.length >= 1) {

                    for (int i = 0; i < list.length; i++) {
                        recipients.add(list[i].toUpperCase());
                    }
                }
                String response = generateResponse(recipients, msg);
                if (recipients.isEmpty()) {
                    ChatServer.sendToAllClients(msg);
                } else {
                    ChatServer.sendToSomeClients(recipients, response);
                }
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
    public String generateResponse(ArrayList<String> recipients, String msg) {
        String mesRes = "";
        //check if the recipients is null, if null.. it needs to send to all
        //holding the recipients list in the recipients arrayList
//        for (String recipient : recipients) {
//            recipientsList.add(recipient);
//        }
        mesRes = "MSGRES:" + currentUser + ":" + msg;
        System.out.println(mesRes);
        return mesRes;
    }

    private void logout() {

    }

    public ArrayList<String> getRecipients() {
        return recipientsList;
    }

//    public static void main(String[] args) {
//        Decoder d = new Decoder();
//        String line = "MSG::This is just a trial!";
//        d.splitLine(line);
//        System.out.println("Recipients:" + d.getRecipients());
//    }
    public void setCurrentUserName(String username) {
        currentUser = username;
    }
}
