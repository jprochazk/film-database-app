import com.linkedin.flashback.SceneAccessLayer;
import com.linkedin.flashback.matchrules.DummyMatchRule;
import com.linkedin.flashback.matchrules.MatchRule;
import com.linkedin.flashback.scene.DummyScene;
import com.linkedin.flashback.scene.Scene;
import com.linkedin.flashback.smartproxy.FlashbackRunner;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

public final class FlashbackContainer implements Closeable
{
    private final Logger logger = LoggerFactory.getLogger(FlashbackContainer.class);

    /** State variables */
    private FlashbackRunner currentFlashbackInstance;
    private Scene currentScene;
    private MatchRule currentMatchRule;
    private Boolean running = false;
    private Boolean dirty = false;

    private String host;
    private int port;

    public FlashbackContainer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /** Start the Flashback scene. */
    public void startScene() throws InterruptedException {
        if(currentFlashbackInstance == null || this.dirty) rebuildInstance();
        if(this.running) throw new IllegalStateException("Already running");

        logger.info("Starting scene "+currentScene.getName().replace(".json", ""));
        currentFlashbackInstance.start();
        this.running = true;
    }

    /** Stop the Flashback scene. */
    public void stopScene() {
        if(currentFlashbackInstance == null) throw new IllegalStateException("Scene not set");
        if(!this.running) throw new IllegalStateException("Not running");

        logger.info("Stopping scene "+currentScene.getName().replace(".json", "")+" and saving to "+currentScene.getSceneRoot()+"/"+currentScene.getName());
        currentFlashbackInstance.stop();
        this.running = false;
    }

    /** Set the current scene. */
    public void setScene(Scene scene) {
        this.currentScene = scene;
        this.dirty = true;
    }

    /** Set the current match rule. */
    public void setMatchRule(MatchRule rule) {
        this.currentMatchRule = rule;
        this.dirty = true;
    }

    /** Get the HttpHost of the Flashback proxy server. */
    public HttpHost getProxy() {
        return new HttpHost(host, port);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void close() {
        currentFlashbackInstance.close();
    }

    private FlashbackRunner buildInstance(String host, int port, Scene scene, MatchRule matchRule) {
        if(host == null) throw new IllegalArgumentException("Host cannot be null!");
        if(scene == null) scene = new DummyScene();
        if(matchRule == null) matchRule = new DummyMatchRule();

        FlashbackRunner instance = new FlashbackRunner.Builder()
                .sceneAccessLayer(new SceneAccessLayer(scene, matchRule)).host(host).port(port).mode(scene.getSceneMode()).build();
        this.dirty = false;
        return instance;
    }

    private void rebuildInstance() {
        this.currentFlashbackInstance = buildInstance(host, port, this.currentScene, this.currentMatchRule);
    }
}
