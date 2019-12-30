package physics.JBullet;

import physics.PhysicsConstraint;
import physics.PhysicsObject;

public abstract class JBulletConstraint implements PhysicsConstraint {
    private int uid;
    private JBulletPhysicsObject bodyA;
    private JBulletPhysicsObject bodyB;

    public JBulletConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB) {
        this.uid = uid;
        this.bodyA = bodyA;
        this.bodyB = bodyB;
    }

    public PhysicsObject getBodyA() {
        return this.bodyA;
    }

    public PhysicsObject getBodyB() {
        return this.bodyB;
    }

    public int getUID() {
        return this.uid;
    }
}
