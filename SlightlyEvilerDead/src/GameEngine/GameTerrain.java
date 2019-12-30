package GameEngine;

import ray.rage.Engine;
import ray.rage.scene.SceneNode;
import ray.rage.scene.Tessellation;

public class GameTerrain {
    public GameTerrain(Engine engine){
        Tessellation tessellationEntity = engine.getSceneManager().createTessellation("tessellation", 6);
        tessellationEntity.setSubdivisions(32.0f);

        SceneNode tessellationNode = engine.getSceneManager().getRootSceneNode().createChildSceneNode("tessellationNode");
        tessellationNode.attachObject(tessellationEntity);

        tessellationNode.setLocalPosition(0.0f, 0.0f, -130.0f);
        tessellationNode.scale(180.0f, 360.0f, 180.0f);
        tessellationEntity.setHeightMap(engine, "scribble.png");
        tessellationEntity.setTexture(engine, "grass.jpg");
    }

    private void updateVerticalPosition(){

    }
}
