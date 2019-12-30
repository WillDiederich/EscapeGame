package GameEngine;

import Networking.GhostAvatar;
import Networking.GhostNPC;
import Networking.NPC;
import Networking.ProtocolClient;
import physics.PhysicsEngine;
import physics.PhysicsObject;
import ray.networking.IGameConnection.ProtocolType;
import ray.rage.rendersystem.Renderable;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

public class GameNetworking {

    private String serverAddress;
    private int serverPort;
    private ProtocolType serverProtocol;
    private ProtocolClient protocolClient;
    private boolean isClientConnected;
    private Vector<UUID> gameObjectsToRemove;
    private SceneManager sceneManager;
    private PhysicsEngine physicsEngine;

    public GameNetworking(String address, int port, SceneManager sm, PhysicsEngine phys){
        serverAddress = address;
        serverPort = port;
        sceneManager = sm;
        this.serverProtocol = ProtocolType.UDP;
        physicsEngine = phys;
    }

    public void setupNetworking(){
        gameObjectsToRemove = new Vector<>();
        isClientConnected = false;
        try{
            protocolClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(protocolClient == null){
            System.out.println("Missing protocol host.");
        }
        else{
            protocolClient.sendJoinMessage();
        }
    }

    public void processNetworking(float elapsTime){
        if(protocolClient != null) {
            protocolClient.processPackets();
        }
        Iterator<UUID> iterator = gameObjectsToRemove.iterator();
        while(iterator.hasNext()){
            sceneManager.destroySceneNode(iterator.next().toString());
        }
        gameObjectsToRemove.clear();
    }

    public Vector3 getPlayerPosition(){
        SceneNode playerAvatarNode = sceneManager.getSceneNode("playerAvatarnode");
        return playerAvatarNode.getWorldPosition();
    }

    public void addGhostAvatarToGameWorld(GhostAvatar avatar, Vector3 ghostPosition) throws IOException {
        if(avatar != null){
            Entity ghostE = sceneManager.createEntity("ghost", "human.obj");
            ghostE.setPrimitive(Renderable.Primitive.TRIANGLES);

            SceneNode ghostN = sceneManager.getRootSceneNode().createChildSceneNode(avatar.getId().toString());
            ghostN.attachObject(ghostE);
            ghostN.setLocalPosition(ghostPosition);

            double[] temp = toDoubleArray(ghostN.getLocalTransform().toFloatArray());
            float[] scale = {1.67f, 1.67f, 1.67f};
            System.out.println(physicsEngine);
            PhysicsObject ghostP = physicsEngine.addBoxObject(physicsEngine.nextUID(), 1.0f, temp, scale);
            ghostP.setBounciness(0.0f);
            ghostP.setFriction(1.0f);
            ghostP.setDamping(0.9f,1.0f);
            ghostP.setSleepThresholds(0.0f,0.0f);
            ghostP.setUserPointer(ghostN);
            ghostN.setPhysicsObject(ghostP);

            avatar.setNode(ghostN);
            avatar.setEntity(ghostE);

        }
    }

    public void addGhostNPCToGameWorld(GhostNPC avatar) throws IOException {
        if(avatar != null){
            Entity ghostE = sceneManager.createEntity("NPC", "human.obj");
            ghostE.setPrimitive(Renderable.Primitive.TRIANGLES);

            SceneNode ghostN = sceneManager.getRootSceneNode().createChildSceneNode(avatar.getId().toString());
            ghostN.attachObject(ghostE);
            ghostN.setLocalPosition(0.0f, 6.0f, 0.0f);

            avatar.setNode(ghostN);
            avatar.setEntity(ghostE);
        }
    }

    public void removeGhostAvatarFromGameWorld(GhostAvatar avatar){
        if(avatar != null){
            gameObjectsToRemove.add(avatar.getId());
        }
    }

    public void moveForward(UUID avatar){
        SceneNode ghostAvatar = sceneManager.getSceneNode(avatar.toString());
        ghostAvatar.getPhysicsObject().applyForce(ghostAvatar.getWorldForwardAxis().x() * -10.0f, 0.0f, ghostAvatar.getWorldForwardAxis().z() * -10.0f, 0.0f, 0.0f, 0.0f);
    }

    public void moveBackward(UUID avatar){
        SceneNode ghostAvatar = sceneManager.getSceneNode(avatar.toString());
        ghostAvatar.getPhysicsObject().applyForce(ghostAvatar.getWorldForwardAxis().x() * 10.0f, 0.0f, ghostAvatar.getWorldForwardAxis().z() * 10.0f, 0.0f, 0.0f, 0.0f);
    }

    public void moveLeft(UUID avatar){
        SceneNode ghostAvatar = sceneManager.getSceneNode(avatar.toString());
        ghostAvatar.getPhysicsObject().applyForce(ghostAvatar.getWorldRightAxis().x() * -10.0f, 0.0f, ghostAvatar.getWorldRightAxis().z() * -10.0f, 0.0f, 0.0f, 0.0f);
    }

    public void moveRight(UUID avatar){
        SceneNode ghostAvatar = sceneManager.getSceneNode(avatar.toString());
        ghostAvatar.getPhysicsObject().applyForce(ghostAvatar.getWorldRightAxis().x() * 10.0f, 0.0f, ghostAvatar.getWorldRightAxis().z() * 10.0f, 0.0f, 0.0f, 0.0f);
    }

    public boolean isClientConnected() {
        return isClientConnected;
    }

    public void setClientConnected(boolean clientConnected) {
        System.out.println("Hello from somewhere~");
        isClientConnected = clientConnected;
    }

    public void sendMoveForwardMessage(){
        protocolClient.sendMoveForward();
    }

    public void sendMoveBackwardMessage(){
        protocolClient.sendMoveBackward();
    }

    public void sendMoveLeftMessage(){
        protocolClient.sendMoveLeft();
    }

    public void sendMoveRightMessage(){
        protocolClient.sendMoveRight();
    }

    private float[] toFloatArray(double[] arr)
    {   if (arr == null) return null;
        int n = arr.length;
        float[] ret = new float[n];
        for (int i = 0; i < n; i++)
        {   ret[i] = (float)arr[i];
        }
        return ret;
    }

    private double[] toDoubleArray(float[] arr)
    {   if (arr == null) return null;
        int n = arr.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++)
        {   ret[i] = (double)arr[i];
        }
        return ret;
    }
}
