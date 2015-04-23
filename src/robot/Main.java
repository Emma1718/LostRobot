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

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        final Circle circle = new Circle(200, 150, 50, Color.BLUEVIOLET);
        circle.setOpacity(0.7);
        final TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, circle);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                circle.setCenterX(circle.getTranslateX() + circle.getCenterX());
                circle.setCenterY(circle.getTranslateY() + circle.getCenterY());
                circle.setTranslateX(0);
                circle.setTranslateY(0);
            }
        });
        drawShapes(gc);
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.setFocusTraversable(true);
        root.getChildren().add(canvas);
        root.getChildren().add(circle);
        final MultiplePressedKeysEventHandler keyHandler =
                new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {

                    public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                        if (ke.isPressed(KeyCode.LEFT) || ke.isPressed(KeyCode.A)) {
                            circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)) {
                            circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                        }

                        if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.W)) {
                            circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
                        }
                        if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)) {
                            circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
                        }
                    }
                });
        Scene scene = new Scene(root, 800, 800);
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
