package GameEngine;

import java.io.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import Game.SlightlyEvilerDead;
import ray.rage.Engine;
import ray.rage.scene.SceneManager;

public class GameScripts {
    public GameScripts(Engine engine, SceneManager sm, SlightlyEvilerDead game){
        ScriptEngineManager factory = new ScriptEngineManager();
        java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
        ScriptEngine jsEngine = factory.getEngineByName("js");

        File scriptFile1 = new File("Hello.js");
        jsEngine.put("sm", sm);
        jsEngine.put("game", game);

        executeScript(jsEngine, scriptFile1.getPath());
    }

    private void executeScript(ScriptEngine engine, String scriptFileName){
        try{
            FileReader fileReader = new FileReader(scriptFileName);
            engine.eval(fileReader);
            fileReader.close();
        } catch (FileNotFoundException e1) {
            System.out.println(scriptFileName + " not found " + e1);
        } catch (IOException e2) {
            System.out.println("IO problem with " + scriptFileName + e2);
        } catch (ScriptException e3) {
            System.out.println("ScriptException in " + scriptFileName + e3);
        } catch (NullPointerException e4) {
            System.out.println ("Null ptr exception in " + scriptFileName + e4); }
    }
}
