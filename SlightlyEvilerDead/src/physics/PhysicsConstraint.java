package physics;

public interface PhysicsConstraint {
    int getUID();

    PhysicsObject getBodyA();

    PhysicsObject getBodyB();
}
