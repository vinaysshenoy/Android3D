package com.vinaysshenoy.core3d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class Core3d extends ApplicationAdapter {

    private static final String TAG = "Core3d";

    private ModelInstance modelInstance;
    private Core3DCallbacks callbacks;
    private PerspectiveCamera perspectiveCamera;
    private Model model;
    private ModelBatch modelBatch;
    private Environment environment;
    private Color glClearColor;

    public Core3d(Core3DCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public Core3DCallbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Core3DCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void create() {

        glClearColor = new Color(0.88F, 0.88F, 0.88F, 1.0F);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        perspectiveCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        perspectiveCamera.position.set(10f, 10f, 10f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();

        final ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);

        if (callbacks != null) {
            callbacks.onCreate(this);
        }
    }

    @Override
    public void resize(int width, int height) {

        perspectiveCamera.viewportWidth = width;
        perspectiveCamera.viewportHeight = height;
        perspectiveCamera.update();

        if (callbacks != null) {
            callbacks.onResize(this, width, height);
        }
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        glClearColor.set(clamp(r, 0.0F, 1.0F), clamp(g, 0.0F, 1.0F), clamp(b, 0.0F, 1.0F), clamp(a, 0.0F, 1.0F));
    }

    public void setModelRotation(final float xDegrees, final float yDegrees, final float zDegrees) {

        post(new Runnable() {
            @Override
            public void run() {
                final Array<Node> nodes = modelInstance.nodes;
                for (int i = 0; i < nodes.size; i++) {
                    nodes.get(i).rotation.setEulerAngles(yDegrees, xDegrees, zDegrees);
                }
                modelInstance.calculateTransforms();
            }
        });


    }

    @Override
    public void render() {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(glClearColor.r, glClearColor.g, glClearColor.b, glClearColor.a);

        modelBatch.begin(perspectiveCamera);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();


        if (callbacks != null) {
            callbacks.onRender(this);
        }
    }

    @Override
    public void pause() {

        if (callbacks != null) {
            callbacks.onPause(this);
        }
    }

    @Override
    public void resume() {

        if (callbacks != null) {
            callbacks.onResume(this);
        }
    }

    @Override
    public void dispose() {

        modelBatch.dispose();
        model.dispose();

        if (callbacks != null) {
            callbacks.onDispose(this);
        }
    }

    public void post(Runnable runnable) {
        Gdx.app.postRunnable(runnable);
    }

    public interface Core3DCallbacks {

        void onCreate(Core3d core3d);

        void onResize(Core3d core3d, int width, int height);

        void onRender(Core3d core3d);

        void onPause(Core3d core3d);

        void onResume(Core3d core3d);

        void onDispose(Core3d core3d);
    }
}
