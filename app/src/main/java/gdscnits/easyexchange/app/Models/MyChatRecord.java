package gdscnits.easyexchange.app.Models;

public class MyChatRecord {
    String sender;
    String profImage;
    String chatRoomID;

    public MyChatRecord(String sender,String profImage,String chatRoomID){
        this.sender=sender;
        this.profImage=profImage;
        this.chatRoomID=chatRoomID;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }
}
