package Networking;

import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.rml.Vector3;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

@SuppressWarnings("Duplicates")
public class GameServerUDP extends GameConnectionServer<UUID> {

    public GameServerUDP(int localPort) throws IOException {
        super(localPort, ProtocolType.UDP);
    }

    @Override
    public void processPacket(Object o, InetAddress senderIP, int senderPort) {
        String message = (String) o;
        String[] msgTokens = message.split(",");
        if (msgTokens.length > 0) {
            if (msgTokens[0].compareTo("join") == 0) {
                try {
                    IClientInfo ci;
                    ci = getServerSocket().createClientInfo(senderIP, senderPort);
                    UUID clientID = UUID.fromString(msgTokens[1]);
                    System.out.println(clientID);
                    addClient(ci, clientID);
                    sendJoinedMessage(clientID, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (msgTokens[0].compareTo("create") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] position = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendCreateMessages(clientID, position);
                sendWantsDetailsMessages(clientID);
            }

            if (msgTokens[0].compareTo("bye") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendByeMessages(clientID);
                removeClient(clientID);
            }

            if (msgTokens[0].compareTo("dsfr") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                UUID remoteID = UUID.fromString(msgTokens[2]);
                String[] position = {msgTokens[3], msgTokens[4], msgTokens[5]};
                sendDetailsMsg(clientID, remoteID, position);
            }

            if (msgTokens[0].compareTo("moveF") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendMoveForward(clientID);
            }

            if (msgTokens[0].compareTo("moveB") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendMoveBackward(clientID);
            }

            if (msgTokens[0].compareTo("moveL") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendMoveLeft(clientID);
            }

            if (msgTokens[0].compareTo("moveR") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendMoveRight(clientID);
            }

//            if (msgTokens[0].compareTo("needNPC") == 0) {
//                UUID clientID = UUID.fromString(msgTokens[1]);
//                String[] position = {msgTokens[2], msgTokens[3], msgTokens[4]};
//                sendMoveMessages(clientID, position);
//            }
        }
    }

    public void sendJoinedMessage(UUID clientID, boolean success) {
        try {
            String message = new String("join,");
            if (success) {
                message += "success";
            } else {
                message += "failure";
            }
            sendPacket(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateMessages(UUID clientID, String[] position) {
        try {
            String message = new String("create," + clientID.toString());
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendByeMessages(UUID clientID) {
        try {
            String message = new String("bye," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendDetailsMsg(UUID clientID, UUID remoteID, String[] position) {
        try {
            String message = new String("dsfr," + clientID.toString());
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            sendPacket(message, remoteID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendWantsDetailsMessages(UUID clientID) {
        try {
            String message = new String("wsds," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveForward(UUID clientID) {
        try {
            String message = new String("moveF," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveBackward(UUID clientID) {
        try {
            String message = new String("moveB," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveLeft(UUID clientID) {
        try {
            String message = new String("moveL," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveRight(UUID clientID) {
        try {
            String message = new String("moveR," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNPCinfo(NPCController npcController) {
        for (int i = 0; i < npcController.getNumOfNPCs(); i++) {
            try {
                String message = new String("mnpc," + Integer.toString(i));
                Vector3 position = npcController.getPosition(i);
                message += "," + position.x();
                message += "," + position.y();
                message += "," + position.z();
                sendPacketToAll(message);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}