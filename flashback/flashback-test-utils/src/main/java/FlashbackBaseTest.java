import com.linkedin.flashback.factory.SceneFactory;
import com.linkedin.flashback.scene.Scene;
import com.linkedin.flashback.scene.SceneConfiguration;
import com.linkedin.flashback.scene.SceneMode;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * Base class with Flashback functionality.
 * Extend this class, create a scene using createScene(), set it using setScene() and start() the proxy.
 * When you send any http requests, proxy them through the host you get from getProxy().
 * When you're done, stop() the proxy server.
 *
 * Initialize each scene at the start of a test. Do not re-use scenes. There is no way to change SceneMode on the go,
 * which means that the Flashback instance has to be re-created each time you change the scene.
 *
 * @author Jan Procházka
 */
public class FlashbackBaseTest {
    private static final String DEFAULT_PROXY_HOST = "localhost";
    private static final int DEFAULT_PROXY_PORT = 5556;
    private static final String DEFAULT_SCENE_PATH = System.getProperty("user.dir").replace("\\", "/") + "/flashback_scenes";

    private final Logger logger = LoggerFactory.getLogger(FlashbackBaseTest.class);

    /** Settings - override these before you start any tests. */
    protected String proxyHost = DEFAULT_PROXY_HOST;
    protected int proxyPort = DEFAULT_PROXY_PORT;
    protected String scenePath = DEFAULT_SCENE_PATH;

    protected FlashbackContainer Flashback;

    /** Call before anything else */
    public void initializeFlashback() {
        this.Flashback = new FlashbackContainer(proxyHost, proxyPort);
        logger.info("Flashback initialized at "+proxyHost+":"+proxyPort);
    }

    /** Call after everything else */
    public void cleanupFlashback() {
        if(this.Flashback.isRunning())
            this.Flashback.stopScene();

        this.Flashback.close();
    }

    /**
     * Used to create a Scene object.
     * Detects if scene already exists at System.getProperty("user.dir") + "/flashback_scenes" and sets the scene mode accordingly.
     *
     * @param sceneName name of the scene (e.g. "GoogleOAuth2AuthCodeRequest")
     * @param scenePath Optional. The full path to the scene directory.
     *                  Default: defaultScenePath
     */
    protected Scene createFlashbackScene(String sceneName, String scenePath) throws IOException {
        sceneName = sceneName + ".json";
        SceneMode sceneMode = (findScene(scenePath, sceneName)) ? SceneMode.PLAYBACK : SceneMode.RECORD;
        logger.info("Running with SceneMode: "+sceneMode.name().toUpperCase());

        return SceneFactory.create(new SceneConfiguration(scenePath, sceneMode, sceneName));
    }

    protected Scene createFlashbackScene(String sceneName) throws IOException {
        sceneName = sceneName + ".json";
        SceneMode sceneMode = (findScene(scenePath, sceneName)) ? SceneMode.PLAYBACK : SceneMode.RECORD;

        return SceneFactory.create(new SceneConfiguration(scenePath, sceneMode, sceneName));
    }

    private boolean findScene(String scenePath, String sceneName) {
        boolean found = new File(scenePath+"/"+sceneName).exists();
        if(found) logger.info("Scene file found: "+scenePath+"/"+sceneName);
        else logger.info("Scene file not found, new file will be created and saved when scene stops.");
        return found;
    }
}
