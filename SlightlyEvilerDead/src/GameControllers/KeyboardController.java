package GameControllers;

import Game.SlightlyEvilerDead;
import GameEngine.GameNetworking;
import Networking.ProtocolClient;
import net.java.games.input.Component;
import net.java.games.input.Event;
import physics.PhysicsObject;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.input.InputManager;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;
import ray.rml.Vector3f;


public class KeyboardController {

    private SlightlyEvilerDead game;
    private PhysicsObject playerAvatarPhys;
    private SceneNode playerAvatarNode;
    private CameraController cameraController;
    private GameNetworking gameNetworking;

    public KeyboardController(SlightlyEvilerDead g, PhysicsObject playerPhys, SceneNode playerNode, String kbName, CameraController cameraCtrl, InputManager inputManager, GameNetworking gNetworking) {
        game = g;
        playerAvatarPhys = playerPhys;
        playerAvatarNode = playerNode;
        cameraController = cameraCtrl;
        gameNetworking = gNetworking;
        setupInputs(kbName, inputManager);
    }

    private void setupInputs(String kbName, InputManager inputManager) {
        Action strafeX = new strafeX();
        Action strafeZ = new strafeZ();
        Action turn = new turn();
        Action fireBullet = new fireBullet();
        Action lightToggle = new LightToggle();

        inputManager.associateAction(kbName, Component.Identifier.Key.W,
                strafeZ, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.S,
                strafeZ, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.A,
                strafeX, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.D,
                strafeX, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.Q,
                turn, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.E,
                turn, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        inputManager.associateAction(kbName, Component.Identifier.Key.F,
                lightToggle, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    }

    // Handle player movement in the X direction (Left/Right) using the World Right Axis
    private class strafeX extends AbstractInputAction {
        @Override
        public void performAction(float time, Event e) {
            if (e.getComponent().toString().equals("A")) {
                playerAvatarPhys.applyForce(playerAvatarNode.getWorldRightAxis().x() * -10.0f, 0.0f, playerAvatarNode.getWorldRightAxis().z() * -10.0f, 0.0f, 0.0f, 0.0f);
                gameNetworking.sendMoveLeftMessage();
            } else if (e.getComponent().toString().equals("D")) {
                playerAvatarPhys.applyForce(playerAvatarNode.getWorldRightAxis().x() * 10.0f, 0.0f, playerAvatarNode.getWorldRightAxis().z() * 10.0f, 0.0f, 0.0f, 0.0f);
                gameNetworking.sendMoveRightMessage();
            }

        }
    }

    // Handle player movement in the Z direction (Forward/Backward) using the World Right Axis
    private class strafeZ extends AbstractInputAction {
        @Override
        public void performAction(float time, Event e) {
            if (e.getComponent().toString().equals("W")) {
                playerAvatarPhys.applyForce(playerAvatarNode.getWorldForwardAxis().x() * -10.0f, 0.0f, playerAvatarNode.getWorldForwardAxis().z() * -10.0f, 0.0f, 0.0f, 0.0f);
                gameNetworking.sendMoveForwardMessage();
            } else if (e.getComponent().toString().equals("S")) {
                playerAvatarPhys.applyForce(playerAvatarNode.getWorldForwardAxis().x() * 10.0f, 0.0f, playerAvatarNode.getWorldForwardAxis().z() * 10.0f, 0.0f, 0.0f, 0.0f);
                gameNetworking.sendMoveBackwardMessage();
            }
        }
    }

    // Handle player rotation
    private class turn extends AbstractInputAction {
        @Override
        public void performAction(float time, Event e) {
            if (e.getComponent().toString().equals("Q")) {
                playerAvatarNode.yaw(Degreef.createFrom(1.0f));
            } else if (e.getComponent().toString().equals("E")) {
                playerAvatarNode.yaw(Degreef.createFrom(-1.0f));
            }
        }
    }

    private class fireBullet extends AbstractInputAction {
        @Override
        public void performAction(float time, Event e) {
            if (e.getComponent().toString().equals("Q")) {
                playerAvatarNode.yaw(Degreef.createFrom(1.0f));
            } else if (e.getComponent().toString().equals("E")) {
                playerAvatarNode.yaw(Degreef.createFrom(-1.0f));
            }
        }
    }

    private class LightToggle extends AbstractInputAction {
        @Override
        public void performAction(float time, Event e) {
            game.turnOffLight();
        }
    }
}