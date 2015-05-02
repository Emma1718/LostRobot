package robot;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final int KEYBOARD_MOVEMENT_DELTA = 5;
    private static final Duration TRANSLATE_DURATION = Duration.seconds(0.25);
    World world;
    Canvas canvas;
    Pane root;
    List<Circle> landmarks = new ArrayList<>();
    Circle circle;
    List<Circle> particles = new ArrayList<>();

    public void createWorld() {
        world = new World();
    }

    public Scene createScene() {
        root = new Pane();
        canvas = new Canvas(World.WIDTH, World.HEIGHT);
        canvas.setFocusTraversable(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        root.getChildren().add(canvas);
        return new Scene(root, World.WIDTH, World.HEIGHT);
    }

    public void initRobotAndParticles() {
        for (Particle p : world.getParticles()) {
            Circle circle = new Circle(p.getCoords().getX(), p.getCoords().getY(), p.getSize(), Color.RED);
            particles.add(circle);
            root.getChildren().add(circle);
        }
        circle = new Circle(world.getRobot().getX(), world.getRobot().getY(), 15, Color.BLACK);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        createWorld();
        Scene scene = createScene();
        initRobotAndParticles();
        final MultiplePressedKeysEventHandler keyHandler =
                new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {

                    public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                        if (ke.isPressed(KeyCode.LEFT) || ke.isPressed(KeyCode.A)) {
                            refreshWorld(90);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.A)) {
                            refreshWorld(270);
                        }
                        if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.A)) {
                            refreshWorld(0);
                        }
                        if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.A)) {
                            refreshWorld(180);
                        }
                        if (ke.isPressed(KeyCode.LEFT) && ke.isPressed(KeyCode.UP)) {
                            refreshWorld(45);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.UP)) {
                            refreshWorld(315);
                        }

                        if (ke.isPressed(KeyCode.RIGHT) && ke.isPressed(KeyCode.DOWN)) {
                            refreshWorld(225);
                        }
                        if (ke.isPressed(KeyCode.LEFT) && ke.isPressed(KeyCode.DOWN)) {
                            refreshWorld(135);
                        }
                    }
                });
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                createLandmark(event.getX(), event.getY());
                world.refresh();
                drawParticles();
            }
        });

        root.getChildren().add(circle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void refreshWorld(int angle) {
        world.interact(angle);
        drawParticles();

        circle.setCenterX(world.getRobot().getX());
        circle.setCenterY(world.getRobot().getY());
    }

    public void createLandmark(double x, double y) {
        final Circle circle = new Circle(x, y, 9, Color.YELLOW);
        root.getChildren().add(circle);
        landmarks.add(circle);
        world.addLandmark(x, y);
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(circle);
                landmarks.remove(circle);
                world.removeLandmark(circle.getCenterX(), circle.getCenterY());
                world.refresh();
                drawParticles();
            }
        });
    }

    public void drawParticles() {
        for (Circle c : particles) {
            root.getChildren().remove(c);
        }
        particles.clear();
        for (Particle p : world.getParticles()) {
            Circle circle = new Circle(p.getCoords().getX(), p.getCoords().getY(), p.getSize(), Color.RED);
            particles.add(circle);
            root.getChildren().add(circle);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
