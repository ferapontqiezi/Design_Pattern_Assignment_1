import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import BouncyBall.Items.Ball;
import BouncyBall.Items.Hole;
import BouncyBall.Items.Table;
import BouncyBall.Game;

import java.util.List;

public class Main extends Application {
    private static final String TITLE = "Table Tennis Game";
    private static final double DIM_X = 1000.0;
    private static final double DIM_Y = 800.0;
    private static final double FRAMETIME = 1.0 / 60.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Table table = ConfigReader.createTable("src/main/resources/config.json");
        List<Ball> balls = ConfigReader.createBalls("src/main/resources/config.json");
        List<Hole> holes = ConfigReader.createHoles("src/main/resources/config.json");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setWidth(DIM_X);
        primaryStage.setHeight(DIM_Y);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();

        Canvas canvas = new Canvas(DIM_X, DIM_Y);
        root.getChildren().add(canvas);

        Game game = new Game(canvas);
        game.setTable(table);
        game.setBalls(balls);
        game.setHoles(holes);
        game.addDrawables(root);

        // setup frames
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.seconds(FRAMETIME), (actionEvent) -> game.tick());
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }
}