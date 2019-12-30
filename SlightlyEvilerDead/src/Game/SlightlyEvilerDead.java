package Game;

import GameControllers.CameraController;
import GameControllers.KeyboardController;
import GameEngine.*;
import GameObjects.GameLight;
import GameObjects.GameModel;

import ray.audio.*;
import com.jogamp.openal.ALFactory;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import physics.PhysicsEngine;
import physics.PhysicsEngineFactory;
import physics.PhysicsObject;

import ray.rage.Engine;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.game.Game;
import ray.rage.game.VariableFrameRateGame;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.Renderable;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.*;
import ray.rml.*;

import java.awt.*;
import java.io.IOException;

import java.util.UUID;

import static ray.rage.scene.SkeletalEntity.EndType.LOOP;


@SuppressWarnings("Duplicates")
public class SlightlyEvilerDead extends VariableFrameRateGame {
    // Game engine related stuff
    private RenderSystem renderSystem;
    private InputManager inputManager;
    private SceneManager sceneManager;
    private Engine engine;

    // Game controllers
    private RotationController rotationController;
    private KeyboardController keyboardController;
    private CameraController cameraController;
    private GameNetworking networkController;
    private GameScripts gameScripts;
    private GameSkybox skybox;
    private GameTerrain terrain;

    // Game objects and related scene nodes
    private Camera playerCamera;
    private GameModel playerAvatar, roadSection1, roadSection2, roadSection3, courtyard,
                      mansionFrontWall, mansionBackWall, mansionSideWall1, getMansionSideWall2, mansionBackWalkway, mansionSideWalkway1, mansionSideWalkway2, mansionStaircaseRamp;
    private SceneNode playerCameraNode, playerAvatarNode, roadSectionNode, roadSectionNode2, roadSectionNode3, courtyardNode,
                      mansionFrontWallNode, mansionBackWallNode, mansionSideWall1Node, getMansionSideWall2Node, mansionBackWalkwayNode, mansionSideWalkway1Node, mansionSideWalkway2Node,
                      mansionStaircaseRampNode, zombieNode, lightNode;

    // Physics engine
    private PhysicsEngine physicsEngine;
    private PhysicsObject playerAvatarPhys, roadSectionPhys1, roadSectionPhys2, roadSectionPhys3, courtyardPhys,
                          mansionFrontWallPhys, mansionBackWallPhys, mansionSideWall1Phys, getMansionSideWall2Phys, mansionBackWalkwayPhys, mansionSideWalkway1Phys, mansionSideWalkway2Phys,
                          mansionStaircaseRampPhys, bulletPhys, zombiePhys;

    // Game data
    private String keyboardName, serverAddress, serverPort, playerModel, dispStr;
    private float elapsedTime = 0.0f, speed, damage;

    private GameNPC zombie;

    IAudioManager audioMgr;
    Sound oceanSound, hereSound;
    boolean fullscreen, tf;
    Light planetOneLight;

    private SlightlyEvilerDead(String address, String port, String fScreen){
        super();
        serverAddress = address;
        serverPort = port;
        if(fScreen.equals("true"))
            fullscreen = true;
        else
            fullscreen = false;
    }

    public static void main(String[] args) {
        Game game = new SlightlyEvilerDead(args[0], args[1], args[2]);
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }

    @Override
    protected void setupWindow(RenderSystem renderSystem, GraphicsEnvironment graphicsEnvironment) {
        if(fullscreen){
            renderSystem.createRenderWindow(true);
        }
        else
            renderSystem.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
    }

    @Override
    protected void setupCameras(SceneManager sceneManager, RenderWindow renderWindow) {
        playerCamera = sceneManager.createCamera("playerCamera", Camera.Frustum.Projection.PERSPECTIVE);
        renderWindow.getViewport(0).setCamera(playerCamera);
        playerCameraNode = sceneManager.getRootSceneNode().createChildSceneNode("MainCameraNode");
        playerCameraNode.attachObject(playerCamera);
        playerCamera.setMode('n');
        playerCamera.getFrustum().setFarClipDistance(1000.0f);
    }

    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
        // Assign engine related stuff
        engine = eng;
        sceneManager = sm;
        renderSystem = engine.getRenderSystem();
        inputManager = new GenericInputManager();
        keyboardName = inputManager.getKeyboardName();
        // Create the script controller - used to control spawn rates and the general difficulty
        gameScripts = new GameScripts(engine, sceneManager, this);

        damage = 0.0f;
        rotationController = new RotationController();
        sceneManager.addController(rotationController);
        tf = true;

        //Add the sphere stuff for the hierarchical scenegraph
        Entity planetThreeEntity = sm.createEntity("mySpherE3", "sphere.obj");
        planetThreeEntity.setPrimitive(Renderable.Primitive.TRIANGLES);

        //Create the texture for planet three
        Material material = sm.getMaterialManager().getAssetByPath("earth.mtl");
        Texture texture = eng.getTextureManager().getAssetByPath("earth-day.jpeg");
        TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        tstate.setTexture(texture);
        planetThreeEntity.setRenderState(tstate);
        planetThreeEntity.setMaterial(material);

        //Create the node to attach planet three to
        SceneNode planetThreeNode = sceneManager.getRootSceneNode().createChildSceneNode(planetThreeEntity.getName() + "Node");
        planetThreeNode.attachObject(planetThreeEntity);
        planetThreeNode.scale(2.0f, 2.0f, 2.0f);
        planetThreeNode.moveForward(10.0f);
        planetThreeNode.moveLeft(15.0f);
        planetThreeNode.moveUp(10.0f);

        Entity moonEntity = sm.createEntity("moonEntity", "sphere.obj");
        moonEntity.setPrimitive(Renderable.Primitive.TRIANGLES);
        material = sm.getMaterialManager().getAssetByPath("earth.mtl");
        texture = eng.getTextureManager().getAssetByPath("earth-day.jpeg");
        tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        tstate.setTexture(texture);
        moonEntity.setRenderState(tstate);
        moonEntity.setMaterial(material);

        //Create a child node of planet for it's moon
        SceneNode planetThreeNodeChildNode = planetThreeNode.createChildSceneNode(moonEntity.getName() + "ChildNode");
        planetThreeNodeChildNode.attachObject(moonEntity);
        planetThreeNodeChildNode.scale(0.25f,0.25f,0.25f);
        planetThreeNodeChildNode.moveUp(1.0f);
        planetThreeNodeChildNode.moveLeft(1.0f);

        planetOneLight = sm.createLight("childLight", Light.Type.DIRECTIONAL);
        planetOneLight.setAmbient(new Color(.1f, .1f, .1f));
        planetOneLight.setDiffuse(new Color(1.0f, 1.0f, 1.0f));
        planetOneLight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        planetOneLight.setRange(15.0f);

        lightNode = planetThreeNodeChildNode.createChildSceneNode("lightChildNode");
        lightNode.attachObject(planetOneLight);
        lightNode.moveForward(2.0f);
        lightNode.moveUp(1.5f);

        rotationController.addNode(planetThreeNode);

        // Create the global ambient light
        sceneManager.getAmbientLight().setIntensity(new Color(0.8f,0.8f,0.8f));

        // Create the player object
        playerAvatar = new GameModel(Vector3f.createFrom(0.0f, 0.0f, 6.0f), Vector3f.createFrom(1.0f, 1.0f, 1.0f),"playerAvatar", playerModel);
        playerAvatarNode = playerAvatar.Create(sceneManager, sceneManager.getRootSceneNode(), engine);
        playerAvatarNode.moveUp(5.0f);

        // Create the road and courtyard
        roadSection1 = new GameModel(Vector3f.createFrom(0.0f, 0.0f, 0.0f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "roadSection", "CoolRoad.obj");
        roadSectionNode = roadSection1.Create(sceneManager, sceneManager.getRootSceneNode(), engine);

        roadSection2 = new GameModel(Vector3f.createFrom(55.0f, 0.0f, 25.0f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "roadSection2", "CoolRoad.obj");
        roadSectionNode2 = roadSection2.Create(sceneManager, sceneManager.getRootSceneNode(), engine);
        roadSectionNode2.yaw(Degreef.createFrom(90.0f));

        roadSection3 = new GameModel(Vector3f.createFrom(80.0f, 0.0f, 80.0f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "roadSection3", "CoolRoad.obj");
        roadSectionNode3 = roadSection3.Create(sceneManager, sceneManager.getRootSceneNode(), engine);

        courtyard = new GameModel(Vector3f.createFrom(80.0f, 0.0f, 170.0f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "courtyard", "CoolRoad2.obj");
        courtyardNode = courtyard.Create(sceneManager, sceneManager.getRootSceneNode(), engine);

        // Create the road decoration, courtyard decoration, and assign lights where necessary

        // Create the buildings that are along the road

        // Create the mansion
        mansionFrontWall = new GameModel(Vector3f.createFrom(80.0f, 0.0f, 141.53f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "mansionFrontWall", "FrontWall.obj");
        mansionFrontWallNode = mansionFrontWall.Create(sceneManager, sceneManager.getRootSceneNode(), engine);

        mansionStaircaseRamp = new GameModel(Vector3f.createFrom(80.0f, 4.611f, 177.426f), Vector3f.createFrom(1.0f, 1.0f, 1.0f), "mansionStaircaseRamp", "StaircaseRamp.obj");
        mansionStaircaseRampNode = mansionStaircaseRamp.Create(sceneManager, sceneManager.getRootSceneNode(), engine);
        mansionStaircaseRampNode.pitch(Degreef.createFrom(-25.0f));

        // Create the mansion decoration and assign lights where necessary

        // Create the skybox
        skybox = new GameSkybox(engine);

        // Create the terrain
        terrain = new GameTerrain(engine);
        SkeletalEntity manSE = sceneManager.createSkeletalEntity("manAv", "untitled.rkm", "untitled.rks");
        zombieNode = sceneManager.getRootSceneNode().createChildSceneNode("zombie");
        zombieNode.attachObject(manSE);
        zombieNode.moveUp(5.0f);
        zombieNode.moveBackward(5.0f);
        manSE.loadAnimation("walkAnimation", "untitled.rka");
        manSE.playAnimation("walkAnimation", 1.0f, LOOP, 0);
        // Create the physics system
        initPhysicsSystem();
        createRagePhysicsWorld();

        // Create the Network controller - if the game is started in multilayer mode.
        networkController = new GameNetworking(serverAddress, Integer.parseInt(serverPort), sceneManager, physicsEngine);
        networkController.setupNetworking();


        // Create the keyboard controller
        keyboardController = new KeyboardController(this, playerAvatarPhys, playerAvatarNode, keyboardName, cameraController, inputManager, networkController);
        // Create the mouse controller

        // Create the camera controller
        cameraController = new CameraController(playerCamera, playerCameraNode, playerAvatarNode, keyboardName, inputManager);

        // Create the sound controller
        initAudio(sceneManager);

        zombie = new GameNPC(zombieNode, playerAvatarNode, zombiePhys, this, speed, 1, 1);

    }

    @Override
    protected void update(Engine engine) {
        elapsedTime += engine.getElapsedTimeMillis();
        inputManager.update(elapsedTime);
        cameraController.updateCameraPosition();
        networkController.processNetworking(elapsedTime);

        dispStr = "Time: " + Math.round(elapsedTime)/1000 + "     Damage taken: " + damage;
        renderSystem.setHUD(dispStr, 15, 15);

        SkeletalEntity manSE = (SkeletalEntity) engine.getSceneManager().getEntity("manAv");
        manSE.update();
        zombie.checkDistance();

        float time = engine.getElapsedTimeMillis();
        Matrix4 mat;
        physicsEngine.update(time);

        for (SceneNode s : engine.getSceneManager().getSceneNodes()) {
            if (s.getPhysicsObject() != null) {
                mat = Matrix4f.createFrom(toFloatArray(s.getPhysicsObject().getTransform()));
                s.setLocalPosition(mat.value(0, 3), mat.value(1, 3), mat.value(2, 3));
            }
        }
        hereSound.setLocation(playerAvatarNode.getWorldPosition());
        oceanSound.setLocation(playerAvatarNode.getWorldPosition());
        setEarParameters(sceneManager);

        if(playerAvatarNode.getLocalPosition().z() <= -39.0f){
            float[] gravity = {0, 0, 0};
            physicsEngine.setGravity(gravity);
            SceneNode tessN = sceneManager.getSceneNode("tessellationNode");
            Tessellation tessE = ((Tessellation) tessN.getAttachedObject("tessellation"));
            Vector3 avatarWorldPos = playerAvatarNode.getWorldPosition();
            Vector3 avatarLocalPos = playerAvatarNode.getLocalPosition();
            Vector3 newAvatarPos = Vector3f.createFrom(avatarLocalPos.x(), tessE.getWorldHeight(avatarWorldPos.x(), avatarWorldPos.z()), avatarLocalPos.z());
            playerAvatarNode.setLocalPosition(newAvatarPos);
            double[] trans = playerAvatarPhys.getTransform();
            double[] newTrans = {trans[0], trans[1], trans[2], trans[3], trans[4], trans[5], trans[6], trans[7], newAvatarPos.x(), newAvatarPos.y() , newAvatarPos.z(), trans[11], trans[12], trans[13], trans[14], trans[15]};
            playerAvatarPhys.setTransform(newTrans);
        }
        else{
            float[] gravity = {0, -3.0f, 0};
            physicsEngine.setGravity(gravity);
        }
    }

    private void createRagePhysicsWorld() {
        double[] temp;
        float[] scale = {30.0f, 0.8f, 80.0f};
        float[] scale2 = {1.67f, 1.67f, 1.67f};
        float[] scale3 = {120f, 0.8f, 100f};
        float[] scale5 = {77.547f, 34.579f, 0.1f};
        float[] scale6 = {8.0f, 0.2f, 25.0f};

        temp = toDoubleArray(playerAvatarNode.getLocalTransform().toFloatArray());
        playerAvatarPhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 1.0f, temp, scale2);
        playerAvatarPhys.setBounciness(0.0f);
        playerAvatarPhys.setFriction(1.0f);
        playerAvatarPhys.setDamping(0.9f,1.0f);
        playerAvatarPhys.setSleepThresholds(0.0f,0.0f);
        playerAvatarPhys.setUserPointer(playerAvatarNode);
        playerAvatarNode.setPhysicsObject(playerAvatarPhys);

        temp = toDoubleArray(zombieNode.getLocalTransform().toFloatArray());
        zombiePhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 1.0f, temp, scale2);
        zombiePhys.setBounciness(0.0f);
        zombiePhys.setFriction(1.0f);
        zombiePhys.setDamping(0.9f,1.0f);
        zombiePhys.setSleepThresholds(0.0f,0.0f);
        zombiePhys.setUserPointer(zombieNode);
        zombieNode.setPhysicsObject(zombiePhys);

        temp = toDoubleArray(roadSectionNode.getLocalTransform().toFloatArray());
        roadSectionPhys1 = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale);
        roadSectionPhys1.setBounciness(0.0f);
        roadSectionPhys1.setUserPointer(roadSectionNode);
        roadSectionNode.setPhysicsObject(roadSectionPhys1);

        temp = toDoubleArray(roadSectionNode2.getLocalTransform().toFloatArray());
        roadSectionPhys2 = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale);
        roadSectionPhys2.setBounciness(0.0f);
        roadSectionPhys2.setUserPointer(roadSectionNode2);
        roadSectionNode2.setPhysicsObject(roadSectionPhys2);

        temp = toDoubleArray(roadSectionNode3.getLocalTransform().toFloatArray());
        roadSectionPhys3 = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale);
        roadSectionPhys3.setBounciness(0.0f);
        roadSectionPhys3.setUserPointer(roadSectionNode3);
        roadSectionNode3.setPhysicsObject(roadSectionPhys3);

        temp = toDoubleArray(courtyardNode.getLocalTransform().toFloatArray());
        courtyardPhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale3);
        courtyardPhys.setBounciness(0.0f);
        courtyardPhys.setUserPointer(courtyardNode);
        courtyardNode.setPhysicsObject(courtyardPhys);

        temp = toDoubleArray(mansionFrontWallNode.getLocalTransform().toFloatArray());
        mansionFrontWallPhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale5);
        mansionFrontWallPhys.setBounciness(0.0f);
        mansionFrontWallPhys.setUserPointer(mansionFrontWallNode);
        mansionFrontWallNode.setPhysicsObject(mansionFrontWallPhys);

        temp = toDoubleArray(mansionStaircaseRampNode.getLocalTransform().toFloatArray());
        mansionStaircaseRampPhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 0.0f, temp, scale6);
        mansionStaircaseRampPhys.setBounciness(0.0f);
        mansionStaircaseRampPhys.setFriction(0.5f);
        mansionStaircaseRampPhys.setUserPointer(mansionStaircaseRampNode);
        mansionStaircaseRampNode.setPhysicsObject(mansionStaircaseRampPhys);
    }

    private void initPhysicsSystem() {
        String engine = "physics.JBullet.JBulletPhysicsEngine";
        float[] gravity = {0, -3.0f, 0};
        physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEngine.initSystem(this);
        physicsEngine.setGravity(gravity);
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

    public void createBullet(SceneNode player) throws IOException{
        double[] temp;
        float[] scale = {1.0f, 0.1f, 1f};

        Entity bulletE = sceneManager.createEntity("bullet", "CoolRoad.obj");
        bulletE.setPrimitive(Renderable.Primitive.TRIANGLES);

        SceneNode bulletN = sceneManager.getRootSceneNode().createChildSceneNode("bullet" + UUID.randomUUID());
        bulletN.attachObject(bulletE);
        bulletN.scale(0.1f, 1.0f, 0.1f);
        bulletN.setLocalPosition(player.getLocalPosition().x(), player.getLocalPosition().y(), player.getLocalPosition().z() + 10.0f);

        temp = toDoubleArray(bulletN.getLocalTransform().toFloatArray());
        bulletPhys = physicsEngine.addBoxObject(physicsEngine.nextUID(), 1.0f, temp, scale);
        bulletPhys.setBounciness(0.0f);
        bulletPhys.setUserPointer(bulletN);
        bulletN.setPhysicsObject(bulletPhys);
        bulletPhys.applyForce(0, 500.0f, 0, 0.0f, 0.0f, 0.0f);
    }

    public void destroyBullet(SceneNode node){
        sceneManager.destroySceneNode(node.getName());
        physicsEngine.removeObject(node.getPhysicsObject().getUID());
    }

    public void setEarParameters(SceneManager sm) {
        SceneNode playerAvatarTemp = sm.getSceneNode("playerAvatarnode");
        Vector3 avDir = playerAvatarTemp.getWorldForwardAxis();

        audioMgr.getEar().setLocation(playerAvatarTemp.getWorldPosition());
        audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));
    }

    public void initAudio(SceneManager sm) {
        AudioResource resource1, resource2;
        audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
        System.out.println("Testing");
        if (!audioMgr.initialize())
        { System.out.println("Audio Manager failed to initialize!");
            return;
        }


        resource2 = audioMgr.createAudioResource("rain.wav", AudioResourceType.AUDIO_SAMPLE);
        oceanSound = new Sound(resource2,SoundType.SOUND_EFFECT, 10, true);
        oceanSound.initialize(audioMgr);
        oceanSound.setMaxDistance(10.0f);
        oceanSound.setMinDistance(0.5f);
        oceanSound.setRollOff(5.0f);
        SceneNode earthN = sm.getSceneNode("roadSectionnode");
        oceanSound.setLocation(earthN.getWorldPosition());

        resource1 = audioMgr.createAudioResource("bite.wav", AudioResourceType.AUDIO_SAMPLE);
        hereSound = new Sound(resource1,SoundType.SOUND_EFFECT, 10, false);
        hereSound.initialize(audioMgr);
        hereSound.setMaxDistance(10.0f);
        hereSound.setMinDistance(0.5f);
        hereSound.setRollOff(5.0f);
        SceneNode earthN2 = sm.getSceneNode("playerAvatarnode");
        hereSound.setLocation(earthN2.getWorldPosition());

        setEarParameters(sm);
        oceanSound.play();
    }

    public void playBiteSound(){
        if(!hereSound.getIsPlaying())
            hereSound.play();
    }

    public void setDamageTaken(){
        damage += 1.0f;
    }

    public void setSpeed(float s){
        speed = s;
    }

    public void setPlayerModel(String s){
        playerModel = s;
    }

    public void turnOffLight(){
        if(tf) {
            planetOneLight.setAmbient(new Color(0.0f, 0.0f, 0.0f));
            planetOneLight.setDiffuse(new Color(0.0f, 0.0f, 0.0f));
            planetOneLight.setSpecular(new Color(0.0f, 0.0f, 0.0f));
            tf = false;
        }
        else{
            planetOneLight.setAmbient(new Color(.1f, .1f, .1f));
            planetOneLight.setDiffuse(new Color(1.0f, 1.0f, 1.0f));
            planetOneLight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
            tf = true;
        }
    }
}
