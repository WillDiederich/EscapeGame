package Networking;

import Game.SlightlyEvilerDead;
import GameEngine.GameNetworking;
import ray.networking.client.GameConnectionClient;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;
import java.lang.String;

public class ProtocolClient extends GameConnectionClient {
    private GameNetworking game;
    private UUID id;
    private Vector<GhostAvatar> ghostAvatars;

    public ProtocolClient(InetAddress remAddr, int remPort, ProtocolType pType, GameNetworking game) throws IOException {
        super(remAddr, remPort, pType);
        this.game = game;
        this.id = UUID.randomUUID();
        this.ghostAvatars = new Vector<GhostAvatar>();
    }

    @Override
    protected void processPacket(Object o) {
        String message = (String) o;
        String[] msgTokens = message.split(",");
        if (msgTokens.length > 0) {

            if (msgTokens[0].compareTo("join") == 0) {
                if (msgTokens[1].compareTo("success") == 0) {
                    game.setClientConnected(true);
                    sendCreateMessage(game.getPlayerPosition());
                }
                if (msgTokens[1].compareTo("failure") == 0) {
                    game.setClientConnected(false);
                }
            }

            if (msgTokens[0].compareTo("bye") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                removeGhostAvatarFromGameWorld(ghostID);
            }

            if (msgTokens[0].compareTo("dsfr") == 0 || msgTokens[0].compareTo("create") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(msgTokens[2]),
                                                            Float.parseFloat(msgTokens[3]),
                                                            Float.parseFloat(msgTokens[4]));
                createGhostAvatar(ghostID, ghostPosition);

            }

            if (msgTokens[0].compareTo("wsds") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                Vector3 ghostPosition = game.getPlayerPosition();
                sendDetailsForMessage(ghostID, ghostPosition);
            }

            if (msgTokens[0].compareTo("moveF") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                game.moveForward(ghostID);
            }

            if (msgTokens[0].compareTo("moveB") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                game.moveBackward(ghostID);
            }

            if (msgTokens[0].compareTo("moveL") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                game.moveLeft(ghostID);
            }

            if (msgTokens[0].compareTo("moveR") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                game.moveRight(ghostID);
            }
        }
    }

    private void createGhostAvatar(UUID ghostID, Vector3 ghostPosition) {
        GhostAvatar ghost = new GhostAvatar(ghostID, ghostPosition);
        ghostAvatars.add(ghost);
        try{
            game.addGhostAvatarToGameWorld(ghost, ghostPosition);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void removeGhostAvatarFromGameWorld(UUID ghostID) {
        for(int x = 0; x < ghostAvatars.size(); x++){
            if(ghostAvatars.elementAt(x).getId() == ghostID){
                game.removeGhostAvatarFromGameWorld(ghostAvatars.elementAt(x));
                ghostAvatars.remove(x);
                break;
            }
        }
    }

    public void sendJoinMessage(){
        try{
            sendPacket(new String("join," + id.toString()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendCreateMessage(Vector3 pos){
        try{
            String message = new String("create," + id.toString());
            message += "," + pos.x() + "," + pos.y() + "," + pos.z();
            sendPacket(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendByeMessage(){
        try {
            String message = new String("bye," + id.toString());
            sendPacket(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendDetailsForMessage(UUID remID, Vector3 pos){
        try {
            String message = new String("dsfr," + id.toString() + "," + remID.toString());
            message += "," + pos.x() + "," + pos.y() + "," + pos.z();
            sendPacket(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMoveForward(){
        try {
            String message = new String("moveF," + id.toString());
            sendPacket(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMoveBackward(){
        try {
            String message = new String("moveB," + id.toString());
            sendPacket(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMoveLeft(){
        try {
            String message = new String("moveL," + id.toString());
            sendPacket(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMoveRight(){
        try {
            String message = new String("moveR," + id.toString());
            sendPacket(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}