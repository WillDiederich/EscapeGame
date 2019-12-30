package GameEngine;

import ray.rage.scene.controllers.AbstractController;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

public class RotationController extends AbstractController{
		
	@Override
	protected void updateImpl(float elapsedTimeMillis) {
		for (Node n : super.controlledNodesList) {
		   Angle rotAmt = Degreef.createFrom(1.0f);
		   n.roll(rotAmt);
		   if(n.getChildCount() != 0) {
			   rotAmt = Degreef.createFrom(2.0f);
			   n.getChild(0).roll(rotAmt);
		   }
		} 
	}
}