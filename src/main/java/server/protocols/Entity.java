package server.protocols;

import java.util.ArrayList;

public class Entity {
    private String senderAddress;
     private String command;
     private ArrayList recipientsAddresses;
     private String content;
     private int CloseCode;

    public Entity() {
        recipientsAddresses = new ArrayList();
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ArrayList getRecipientsAddresses() {
        return recipientsAddresses;
    }

    public void addRecipientAddress(String address) {
        this.recipientsAddresses.add(address);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCloseCode() {
        return CloseCode;
    }

    public void setCloseCode(int closeCode) {
        CloseCode = closeCode;
    }
}
