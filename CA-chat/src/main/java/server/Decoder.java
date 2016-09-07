package server;

import java.util.ArrayList;

public class Decoder {

    private String cmd;
    private ArrayList connectedUsers;
    private String msg;
    private String[] splitString;

    public Decoder(String input) {

        if (splitString.length > 1) {
            splitString = input.split(":");
            switch (splitString[0]) {
                case "LOGIN":
                    login();
                    break;
                case "MSG":
                    sendMsg();
                    break;
                case "LOGOUT":
                    logout();
                    break;
                default:
                    cmd = "SYNTAX ERROR";
            }
        }
    }

    public String getCmd() {
        return cmd;
    }

    private void login() {
        connectedUsers.add(splitString[1]);
        for (Object connectedUser : connectedUsers) {
            System.out.println(connectedUser.toString());
        }
    }

    private void sendMsg() {
    }

    private void logout() {

    }

}
