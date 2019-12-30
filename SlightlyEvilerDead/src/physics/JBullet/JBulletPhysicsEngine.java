package physics.JBullet;

import Game.SlightlyEvilerDead;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import java.util.Iterator;
import java.util.Vector;
import javax.vecmath.Vector3f;
import physics.PhysicsBallSocketConstraint;
import physics.PhysicsEngine;
import physics.PhysicsEngineFactory;
import physics.PhysicsHingeConstraint;
import physics.PhysicsObject;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;

public class JBulletPhysicsEngine implements PhysicsEngine {
    private static final int MAX_PHYSICS_OBJECTS = 1024;
    private static int nextUID;
    private DefaultCollisionConfiguration collisionConfiguration;
    private CollisionDispatcher dispatcher;
    private SequentialImpulseConstraintSolver solver;
    private AxisSweep3 overlappingPairCache;
    private DiscreteDynamicsWorld dynamicsWorld;
    private Vector<PhysicsObject> objects;
    private SlightlyEvilerDead game;

    static {
        PhysicsEngineFactory.registerPhysicsEngine("physics.JBullet.JBulletPhysicsEngine", JBulletPhysicsEngine.class);
    }

    public JBulletPhysicsEngine() {
    }

    public void initSystem(SlightlyEvilerDead g) {
        game = g;
        this.collisionConfiguration = new DefaultCollisionConfiguration();
        this.dispatcher = new CollisionDispatcher(this.collisionConfiguration);
        Vector3f worldAabbMin = new Vector3f(-10000.0F, -10000.0F, -10000.0F);
        Vector3f worldAabbMax = new Vector3f(10000.0F, 10000.0F, 10000.0F);
        this.overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, 1024);
        SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
        this.solver = sol;
        this.dynamicsWorld = new DiscreteDynamicsWorld(this.dispatcher, this.overlappingPairCache, this.solver, this.collisionConfiguration);
        float[] gravity_vector = new float[]{0.0F, 0.0F, 0.0F};
        this.setGravity(gravity_vector);
        this.objects = new Vector(50, 25);
    }

    public void setGravity(float[] gravity_vector) {
        this.dynamicsWorld.setGravity(new Vector3f(gravity_vector));
    }

    public PhysicsObject addBoxObject(int uid, float mass, double[] transform, float[] size) {
        float[] temp = new float[size.length];

        for(int i = 0; i < size.length; ++i) {
            temp[i] = size[i] / 2.0F;
        }

        JBulletBoxObject boxObject = new JBulletBoxObject(uid, mass, transform, temp);
        this.dynamicsWorld.addRigidBody(boxObject.getRigidBody());
        this.objects.add(boxObject);
        return boxObject;
    }

    public PhysicsObject addSphereObject(int uid, float mass, double[] transform, float radius) {
        JBulletSphereObject sphereObject = new JBulletSphereObject(uid, mass, transform, radius);
        this.dynamicsWorld.addRigidBody(sphereObject.getRigidBody());

        this.objects.add(sphereObject);
        return sphereObject;
    }

    public PhysicsObject addConeObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletConeObject coneObject = new JBulletConeObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(coneObject.getRigidBody());
        this.objects.add(coneObject);
        return coneObject;
    }

    public PhysicsObject addConeXObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletConeXObject coneObject = new JBulletConeXObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(coneObject.getRigidBody());
        this.objects.add(coneObject);
        return coneObject;
    }

    public PhysicsObject addConeZObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletConeZObject coneObject = new JBulletConeZObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(coneObject.getRigidBody());
        this.objects.add(coneObject);
        return coneObject;
    }

    public PhysicsObject addCylinderObject(int uid, float mass, double[] transform, float[] halfExtents) {
        JBulletCylinderObject cylinderObject = new JBulletCylinderObject(uid, mass, transform, halfExtents);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addCylinderXObject(int uid, float mass, double[] transform, float[] halfExtents) {
        JBulletCylinderXObject cylinderObject = new JBulletCylinderXObject(uid, mass, transform, halfExtents);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addCylinderZObject(int uid, float mass, double[] transform, float[] halfExtents) {
        JBulletCylinderZObject cylinderObject = new JBulletCylinderZObject(uid, mass, transform, halfExtents);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addCapsuleObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletCapsuleObject cylinderObject = new JBulletCapsuleObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addCapsuleXObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletCapsuleXObject cylinderObject = new JBulletCapsuleXObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addCapsuleZObject(int uid, float mass, double[] transform, float radius, float height) {
        JBulletCapsuleZObject cylinderObject = new JBulletCapsuleZObject(uid, mass, transform, radius, height);
        this.dynamicsWorld.addRigidBody(cylinderObject.getRigidBody());
        this.objects.add(cylinderObject);
        return cylinderObject;
    }

    public PhysicsObject addStaticPlaneObject(int uid, double[] transform, float[] up_vector, float plane_constant) {
        JBulletStaticPlaneObject planeObject = new JBulletStaticPlaneObject(uid, transform, up_vector, plane_constant);
        this.dynamicsWorld.addRigidBody(planeObject.getRigidBody());
        this.objects.add(planeObject);
        return planeObject;
    }

    public void removeObject(int uid) {
        JBulletPhysicsObject target_object = null;
        Iterator var4 = this.objects.iterator();

        while(var4.hasNext()) {
            PhysicsObject object = (PhysicsObject)var4.next();
            if (object.getUID() == uid) {
                target_object = (JBulletPhysicsObject)object;
            }
        }

        if (target_object != null) {
            this.dynamicsWorld.removeRigidBody(target_object.getRigidBody());
        }

    }

    public void update(float nanoseconds) {
        if (this.dynamicsWorld != null) {
            this.dynamicsWorld.stepSimulation(nanoseconds / 1000.0F);

            this.dynamicsWorld.setInternalTickCallback(new InternalTickCallback() {
                @Override
                public void internalTick(com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld, float timeStep) {
                    int manifoldCount = dispatcher.getNumManifolds();
                    for (int i = 0; i < manifoldCount; i++) {
                        PersistentManifold manifold = dispatcher.getManifoldByIndexInternal(i);
                        RigidBody obj1 = (RigidBody)manifold.getBody0();
                        RigidBody obj2 = (RigidBody)manifold.getBody1();
                        SceneNode physicsObject1 = (SceneNode) obj1.getUserPointer();
                        SceneNode physicsObject2 = (SceneNode) obj2.getUserPointer();
                        boolean hit = false;
                        for (int j = 0; j < manifold.getNumContacts(); j++) {
                            ManifoldPoint contactPoint = manifold.getContactPoint(j);
                            if (contactPoint.getDistance() < 0.0f) {
                                hit = true;
                                break;
                            }
                        }
                        if (hit) {
                            // Collision handling for bullets
                            if(physicsObject1.getName().contains("bullet")){
                                game.destroyBullet(physicsObject1);
                                if(physicsObject2.getName().contains("zombie")){
                                }
                                else if(physicsObject2.getName().contains("player")){

                                }
                            }
                            else if(physicsObject2.getName().contains("bullet")){
                                game.destroyBullet(physicsObject2);
                                if(physicsObject1.getName().contains("zombie")){
                                }
                                else if(physicsObject1.getName().contains("player")){

                                }
                            }

                            // Collision handling for monsters
                            if(physicsObject1.getName().contains("zombie")){
                                if(physicsObject2.getName().contains("player")){
                                    game.playBiteSound();
                                    game.setDamageTaken();
                                }
                            }
                            else if(physicsObject2.getName().contains("zombie")){
                                if(physicsObject1.getName().contains("player")){
                                    game.playBiteSound();
                                    game.setDamageTaken();
                                }
                            }
                        }
                    }
                }
            }, null);
        }
    }

    public int nextUID() {
        int temp = nextUID++;
        return temp;
    }

    public PhysicsHingeConstraint addHingeConstraint(int uid, PhysicsObject bodyA, PhysicsObject bodyB, float axisX, float axisY, float axisZ) {
        JBulletHingeConstraint hingeConstraint = new JBulletHingeConstraint(uid, (JBulletPhysicsObject)bodyA, (JBulletPhysicsObject)bodyB, axisX, axisY, axisZ);
        this.dynamicsWorld.addConstraint(hingeConstraint.getConstraint());
        return hingeConstraint;
    }

    public PhysicsBallSocketConstraint addBallSocketConstraint(int uid, PhysicsObject bodyA, PhysicsObject bodyB) {
        JBulletBallSocketConstraint ballSocketConstraint = new JBulletBallSocketConstraint(uid, (JBulletPhysicsObject)bodyA, (JBulletPhysicsObject)bodyB);
        this.dynamicsWorld.addConstraint(ballSocketConstraint.getConstraint());
        return ballSocketConstraint;
    }
}
