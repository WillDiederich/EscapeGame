package GameEngine;

import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.scene.SkyBox;
import ray.rage.util.Configuration;

import java.awt.geom.AffineTransform;
import java.io.IOException;

public class GameSkybox {
    public GameSkybox(Engine engine) throws IOException {
        Configuration configuration = engine.getConfiguration();

        TextureManager textureManager = engine.getTextureManager();
        textureManager.setBaseDirectoryPath(configuration.valueOf("assets.skyboxes.path"));

        Texture front = textureManager.getAssetByPath("BluePinkNebular_front.jpg");
        Texture back = textureManager.getAssetByPath("BluePinkNebular_back.jpg");
        Texture left = textureManager.getAssetByPath("BluePinkNebular_right.jpg");
        Texture right = textureManager.getAssetByPath("BluePinkNebular_left.jpg");
        Texture top = textureManager.getAssetByPath("BluePinkNebular_top.jpg");
        Texture bottom = textureManager.getAssetByPath("BluePinkNebular_bottom.jpg");

        textureManager.setBaseDirectoryPath(configuration.valueOf("assets.textures.path"));

        AffineTransform xform = new AffineTransform();
        xform.translate(0, front.getImage().getHeight());
        xform.scale(1d, -1d);

        front.transform(xform);
        back.transform(xform);
        left.transform(xform);
        right.transform(xform);
        top.transform(xform);
        bottom.transform(xform);

        SkyBox sb = engine.getSceneManager().createSkyBox("Skybox");
        sb.setTexture(front, SkyBox.Face.FRONT);
        sb.setTexture(back, SkyBox.Face.BACK);
        sb.setTexture(left, SkyBox.Face.LEFT);
        sb.setTexture(right, SkyBox.Face.RIGHT);
        sb.setTexture(top, SkyBox.Face.TOP);
        sb.setTexture(bottom, SkyBox.Face.BOTTOM);
        engine.getSceneManager().setActiveSkyBox(sb);
    }
}
