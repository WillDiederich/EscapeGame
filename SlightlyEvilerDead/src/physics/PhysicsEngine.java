package physics;

import Game.SlightlyEvilerDead;

public interface PhysicsEngine {
    float DEFAULT_GRAVITY_X = 0.0F;
    float DEFAULT_GRAVITY_Y = -10.0F;
    float DEFAULT_GRAVITY_Z = 0.0F;

    void initSystem(SlightlyEvilerDead g);

    void setGravity(float[] var1);

    PhysicsObject addBoxObject(int var1, float var2, double[] var3, float[] var4);

    PhysicsObject addSphereObject(int var1, float var2, double[] var3, float var4);

    PhysicsObject addConeObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addConeXObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addConeZObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addCapsuleObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addCapsuleXObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addCapsuleZObject(int var1, float var2, double[] var3, float var4, float var5);

    PhysicsObject addCylinderObject(int var1, float var2, double[] var3, float[] var4);

    PhysicsObject addCylinderXObject(int var1, float var2, double[] var3, float[] var4);

    PhysicsObject addCylinderZObject(int var1, float var2, double[] var3, float[] var4);

    PhysicsObject addStaticPlaneObject(int var1, double[] var2, float[] var3, float var4);

    PhysicsBallSocketConstraint addBallSocketConstraint(int var1, PhysicsObject var2, PhysicsObject var3);

    PhysicsHingeConstraint addHingeConstraint(int var1, PhysicsObject var2, PhysicsObject var3, float var4, float var5, float var6);

    void removeObject(int var1);

    void update(float var1);

    int nextUID();
}
