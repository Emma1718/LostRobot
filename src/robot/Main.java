package robot;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private static final int KEYBOARD_MOVEMENT_DELTA = 5;
    private static final Duration TRANSLATE_DURATION = Duration.seconds(0.25);
    World world;
    Canvas canvas;
    Pane root;
    List<Circle> landmarks = new ArrayList<>();

    public void createWorld() {
        world = new World();

    }

    public Scene createScene() {
        root = new Pane();
        canvas = new Canvas(World.WIDTH, World.HEIGHT);
        canvas.setFocusTraversable(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        root.getChildren().add(canvas);
        return new Scene(root, World.WIDTH, World.HEIGHT);
    }



    @Override
    public void start(Stage primaryStage) {

        createWorld();
        Scene scene = createScene();
        final Circle circle = new Circle(world.getRobot().getX(), world.getRobot().getY(), 30, Color.BLUEVIOLET);

        final MultiplePressedKeysEventHandler keyHandler =
                new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {

                    public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                        if (ke.isPressed(KeyCode.LEFT) || ke.isPressed(KeyCode.A)) {
                            System.out.println("LEFT");
                            world.interact(90);
                            circle.setCenterX(world.getRobot().getX());
                        }
                        if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.A)) {
                            System.out.println("RIGHT");
                            world.interact(270);
                            circle.setCenterX(world.getRobot().getX());
                        }
                        if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.A)) {
                            System.out.println("UP");
                            world.interact(0);
                            circle.setCenterY(world.getRobot().getY());
                        }
                        if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.A)) {
                            System.out.println("DOWN");
                            world.interact(180);
                            circle.setCenterY(world.getRobot().getY());
                        }
                        if (ke.isPressed(KeyCode.LEFT)  && ke.isPressed(KeyCode.UP)) {
                            System.out.println("LEFT + UP");
                            world.interact(45);
                            circle.setCenterX(world.getRobot().getX());
                            circle.setCenterY(world.getRobot().getY());
                            // circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.UP)) {
                            System.out.println("RIGHT + UP");
                            world.interact(315);
                            circle.setCenterX(world.getRobot().getX());
                            circle.setCenterY(world.getRobot().getY());
                            //circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                        }

                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.DOWN)) {
                            System.out.println("RIGHT + DOWN");
                            world.interact(225);
                            circle.setCenterX(world.getRobot().getX());
                            circle.setCenterY(world.getRobot().getY());
                           // circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.LEFT)  && ke.isPressed(KeyCode.DOWN)) {
                            System.out.println("LEFT + DOWN");
                            world.interact(135);
                            circle.setCenterX(world.getRobot().getX());
                            circle.setCenterY(world.getRobot().getY());
                           // circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
                        }
                    }
                });
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                createLandmark(event.getX(), event.getY());
            }
        });

        drawParticles();
        Random r = new Random();
        double x = (double)World.WIDTH * r.nextDouble();
        double y = (double)World.HEIGHT * r.nextDouble();
        createLandmark(x, y);
        root.getChildren().add(circle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createLandmark(double x, double y) {
        final Circle circle = new Circle(x, y, 10, Color.YELLOW);
        root.getChildren().add(circle);
        landmarks.add(circle);
        world.addLandmark(x, y);
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(circle);
                landmarks.remove(circle);
                world.removeLanmark(circle.getCenterX(), circle.getCenterY());
            }
        });
    }

    public void drawParticles() {
        for(Particle p: world.generateParticles()) {
            Circle circle = new Circle(p.getCoords().getX(), p.getCoords().getY(), 1, Color.RED);
            root.getChildren().add(circle);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
