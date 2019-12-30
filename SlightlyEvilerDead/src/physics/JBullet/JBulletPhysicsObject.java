package physics.JBullet;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import javax.vecmath.Vector3f;
import physics.PhysicsObject;

public abstract class JBulletPhysicsObject implements PhysicsObject {
    private int uid;
    private float mass;
    protected Transform transform;
    private CollisionShape shape;
    private RigidBody body;
    private boolean isDynamic;
    private Vector3f localInertia;
    private DefaultMotionState myMotionState;
    private RigidBodyConstructionInfo rbInfo;

    public JBulletPhysicsObject(int uid, float mass, double[] xform, CollisionShape shape) {
        this.uid = uid;
        this.mass = mass;
        this.transform = new Transform();
        this.transform.setFromOpenGLMatrix(JBulletUtils.double_to_float_array(xform));
        this.isDynamic = mass != 0.0F;
        this.shape = shape;
        this.localInertia = new Vector3f(0.0F, 0.0F, 0.0F);
        if (this.isDynamic) {
            shape.calculateLocalInertia(mass, this.localInertia);
        }

        this.myMotionState = new DefaultMotionState(this.transform);
        this.rbInfo = new RigidBodyConstructionInfo(mass, this.myMotionState, shape, this.localInertia);
        this.body = new RigidBody(this.rbInfo);
        this.body.setSleepingThresholds(0.05F, 0.05F);
        this.body.setDamping(0.1F, 0.1F);
    }

    public int getUID() {
        return this.uid;
    }

    public void setTransform(double[] xform) {
        synchronized(this) {
            this.transform.setFromOpenGLMatrix(JBulletUtils.double_to_float_array(xform));
            this.body.setWorldTransform(this.transform);
        }
    }

    public double[] getTransform() {
        synchronized(this) {
            float[] new_xform = new float[16];
            this.body.getWorldTransform(this.transform).getOpenGLMatrix(new_xform);
            return JBulletUtils.float_to_double_array(new_xform);
        }
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        this.isDynamic = mass != 0.0F;
    }

    public RigidBody getRigidBody() {
        return this.body;
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public float getFriction() {
        return this.body.getFriction();
    }

    public void setFriction(float friction) {
        this.body.setFriction(friction);
    }

    public float getBounciness() {
        return this.body.getRestitution();
    }

    public void setBounciness(float bounciness) {
        this.body.setRestitution(bounciness);
    }

    public float[] getLinearVelocity() {
        Vector3f out = new Vector3f();
        this.body.getLinearVelocity(out);
        float[] velocity = new float[]{out.x, out.y, out.z};
        return velocity;
    }

    public void setLinearVelocity(float[] velocity) {
        this.body.setLinearVelocity(new Vector3f(velocity));
    }

    public float[] getAngularVelocity() {
        Vector3f out = new Vector3f();
        this.body.getAngularVelocity(out);
        float[] velocity = new float[]{out.x, out.y, out.z};
        return velocity;
    }

    public void setAngularVelocity(float[] velocity) {
        this.body.setAngularVelocity(new Vector3f(velocity));
    }

    public void setSleepThresholds(float linearThreshold, float angularThreshold) {
        this.body.setSleepingThresholds(linearThreshold, angularThreshold);
    }

    public float getLinearSleepThreshold() {
        return this.body.getLinearSleepingThreshold();
    }

    public float getAngularSleepThreshold() {
        return this.body.getAngularSleepingThreshold();
    }

    public void setDamping(float linearDamping, float angularDamping) {
        this.body.setDamping(linearDamping, angularDamping);
    }

    public float getLinearDamping() {
        return this.body.getLinearDamping();
    }

    public float getAngularDamping() {
        return this.body.getAngularDamping();
    }

    public void applyForce(float fx, float fy, float fz, float px, float py, float pz) {
        this.body.applyForce(new Vector3f(fx, fy, fz), new Vector3f(px, py, pz));
    }

    public void applyTorque(float fx, float fy, float fz) {
        this.body.applyTorque(new Vector3f(fx, fy, fz));
    }

    public void setUserPointer(Object pointer){
        this.body.setUserPointer(pointer);
    }
}

