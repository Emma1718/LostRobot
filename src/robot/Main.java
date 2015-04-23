package robot;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static final int KEYBOARD_MOVEMENT_DELTA = 5;
    private static final Duration TRANSLATE_DURATION = Duration.seconds(0.25);
    World world;
    Canvas canvas;
    Pane root;

    public void createWorld() {
        world = new World();

    }

    public Scene createScene() {
        root = new Pane();
        canvas = new Canvas(World.WIDTH, World.HEIGHT);
        canvas.setFocusTraversable(true);

        root.getChildren().add(canvas);
        return new Scene(root, World.WIDTH, World.HEIGHT);
    }


    @Override
    public void start(Stage primaryStage) {

        createWorld();
        Scene scene = createScene();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        final Circle circle = new Circle(200, 150, 35, Color.BLUEVIOLET);
        circle.setOpacity(0.7);

        root.getChildren().add(circle);
        final MultiplePressedKeysEventHandler keyHandler =
                new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {

                    public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                        if (ke.isPressed(KeyCode.LEFT) || ke.isPressed(KeyCode.A)) {
                            world.interact(90);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.A)) {
                            world.interact(270);
                        }
                        if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.A)) {
                            world.interact(0);
                        }
                        if (ke.isPressed(KeyCode.LEFT) || ke.isPressed(KeyCode.A)) {
                            world.interact(180);
                        }
                        if (ke.isPressed(KeyCode.LEFT)  && ke.isPressed(KeyCode.UP)) {
                            world.interact(45);
                            circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.UP)) {
                            world.interact(315);

                            circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                        }

                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.DOWN)) {
                            world.interact(225);

                            circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.LEFT)  && ke.isPressed(KeyCode.DOWN)) {
                            world.interact(135);

                            circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
                        }
                    }
                });
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);



        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
