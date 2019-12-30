package Networking;

import java.io.IOException;

public class NetworkingServer {
    private GameServerUDP thisUDPServer;
    private NPCController npcController;
    private float startTime;
    private float lastUpdateTime;

    public NetworkingServer(int serverPort) {
        startTime = System.nanoTime();
        lastUpdateTime = startTime;
        npcController = new NPCController();
        try {
            thisUDPServer = new GameServerUDP(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        npcController.setupNPCs();
        npcLoop();
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]));
        }
    }

    public void npcLoop(){
        while (true)
        {
            long frameStartTime = System.nanoTime();
            float elapMilSecs =(frameStartTime-lastUpdateTime)/(1000000.0f);
            if (elapMilSecs >= 50.0f){
                lastUpdateTime = frameStartTime;
                npcController.updateNPCs();
                thisUDPServer.sendNPCinfo(npcController);
            }
            Thread.yield();
        }
    }
}
