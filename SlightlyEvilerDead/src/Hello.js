var JavaPackages = new JavaImporter(
    Packages.ray.rage.scene.SceneManager,
    Packages.ray.rage.scene.Light,
    Packages.ray.rage.scene.Light.Type,
    Packages.ray.rage.scene.Light.Type.POINT,
    Packages.java.awt.Color
);

with (JavaPackages)
{
            sm.getAmbientLight().setIntensity(new Color(0.8, 0.8, 0.8));
            game.setSpeed(10);
            game.setPlayerModel("humangreen.obj")
} 