package physics.JBullet;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import javax.vecmath.Vector3f;
import physics.PhysicsHingeConstraint;

public class JBulletHingeConstraint extends JBulletConstraint implements PhysicsHingeConstraint {
    private HingeConstraint hingeConstraint;
    private float[] axis;

    public JBulletHingeConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB, float axisX, float axisY, float axisZ) {
        super(uid, bodyA, bodyB);
        RigidBody rigidA = bodyA.getRigidBody();
        RigidBody rigidB = bodyB.getRigidBody();
        float[] pivotInA = new float[]{0.0F, 0.0F, 0.0F};
        float[] pivotInB = new float[]{(float)(bodyA.getTransform()[12] - bodyB.getTransform()[12]), (float)(bodyA.getTransform()[13] - bodyB.getTransform()[13]), (float)(bodyA.getTransform()[14] - bodyB.getTransform()[14])};
        this.axis = new float[]{axisX, axisY, axisZ};
        this.hingeConstraint = new HingeConstraint(rigidA, rigidB, new Vector3f(pivotInA), new Vector3f(pivotInB), new Vector3f(axisX, axisY, axisZ), new Vector3f(axisX, axisY, axisZ));
        rigidA.addConstraintRef(this.hingeConstraint);
        rigidB.addConstraintRef(this.hingeConstraint);
    }

    public HingeConstraint getConstraint() {
        return this.hingeConstraint;
    }

    public float getAngle() {
        return this.hingeConstraint.getHingeAngle();
    }

    public float[] getAxis() {
        return this.axis;
    }
}
