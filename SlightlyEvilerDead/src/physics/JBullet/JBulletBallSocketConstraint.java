package physics.JBullet;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import javax.vecmath.Vector3f;
import physics.PhysicsBallSocketConstraint;

public class JBulletBallSocketConstraint extends JBulletConstraint implements PhysicsBallSocketConstraint {
    private Point2PointConstraint p2pConstraint;

    public JBulletBallSocketConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB) {
        super(uid, bodyA, bodyB);
        RigidBody rbA = bodyA.getRigidBody();
        RigidBody rbB = bodyB.getRigidBody();
        float[] pivotInA = new float[]{0.0F, 0.0F, 0.0F};
        float[] pivotInB = new float[]{(float)(bodyA.getTransform()[12] - bodyB.getTransform()[12]), (float)(bodyA.getTransform()[13] - bodyB.getTransform()[13]), (float)(bodyA.getTransform()[14] - bodyB.getTransform()[14])};
        this.p2pConstraint = new Point2PointConstraint(rbA, rbB, new Vector3f(pivotInA), new Vector3f(pivotInB));
        rbA.addConstraintRef(this.p2pConstraint);
        rbB.addConstraintRef(this.p2pConstraint);
    }

    public Point2PointConstraint getConstraint() {
        return this.p2pConstraint;
    }
}
