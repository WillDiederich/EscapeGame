package GameControllers;

import net.java.games.input.Component;
import net.java.games.input.Event;
import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class CameraController {
	public Camera cam;
	private SceneNode cameraN;
	private SceneNode target;
	private float cameraAzimuth;
	private float cameraElevation;
	private float radius;
	private Vector3 worldUpVec;

	public CameraController(Camera playerCamera, SceneNode playerCameraNode, SceneNode playerAvatarNode, String controllerName, InputManager inputManager){
		cam = playerCamera;
		cameraN = playerCameraNode;
		target = playerAvatarNode;
		cameraAzimuth = 225.0f;
		cameraElevation = 20.0f;
		radius = 2.0f;
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		setupInput(inputManager, controllerName);
		updateCameraPosition();
	}
	
	public void updateCameraPosition() {
		double theta = Math.toRadians(cameraAzimuth);
		double phi = Math.toRadians(cameraElevation);
		double x = radius * Math.cos(phi) * Math.sin(theta);
		double y = radius * Math.sin(phi);
		double z = radius * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom((float)x, (float)y, (float)z).add(target.getWorldPosition()));
		cameraN.lookAt(target, worldUpVec);
	}
	
	public void setupInput(InputManager im, String cn){
		Action orbitAroundAction = new OrbitAroundAction();
		Action zoomAction = new ZoomAction();
		Action changeElevation = new ChangeElevation();

		im.associateAction(cn, Component.Identifier.Key.LEFT,
	    		orbitAroundAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn, Component.Identifier.Key.RIGHT,
        		orbitAroundAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn, Component.Identifier.Key.UP,
        		changeElevation, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn, Component.Identifier.Key.DOWN,
        		changeElevation, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn, Component.Identifier.Key.LSHIFT,
        		zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn, Component.Identifier.Key.LCONTROL,
        		zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

	private class OrbitAroundAction extends AbstractInputAction{
		@Override
		public void performAction(float time, Event e) {
			float rotAmount;
			if (e.getComponent().toString().equals("Left") || e.getValue() < -0.2) {
				rotAmount = -1.5f;
			}
			else {
				if (e.getComponent().toString().equals("Right") || e.getValue() > 0.2)
					rotAmount=1.5f;
				else
					rotAmount=0.0f;
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}

	private class ZoomAction extends AbstractInputAction{
		@Override
		public void performAction(float time, Event e) {
			float rotAmount;
			if ((e.getComponent().toString().equals("Left Shift") || e.getComponent().toString().equals("Z Axis") && e.getValue() > 0.2f) && radius < 5.0f) {
				rotAmount = 0.1f;
			}
			else {
				if ((e.getComponent().toString().equals("Left Control") || e.getComponent().toString().equals("Z Axis") && e.getValue() > 0.2f) && radius > 1.5f)
					rotAmount = -0.1f;
				else
					rotAmount = 0.0f;
			}
			radius += rotAmount;
			updateCameraPosition();
		}
	}

	private class ChangeElevation extends AbstractInputAction{
		@Override
		public void performAction(float time, Event e) {
			float rotAmount;
			if ((e.getComponent().toString().equals("Up") || e.getComponent().toString().equals("X Rotation") && e.getValue() > 0.2f) && cameraElevation < 85.0f) {
				rotAmount = 1.5f;
			}
			else {
				if ((e.getComponent().toString().equals("Down") || e.getComponent().toString().equals("X Rotation") &&e.getValue() < -0.2f) && cameraElevation > 0.0f)
					rotAmount = -1.5f;
				else
					rotAmount = 0.0f;
			}
			cameraElevation += rotAmount;
			updateCameraPosition();
		}
	}
}

