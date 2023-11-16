package BouncyBall;

import BouncyBall.Items.Ball;
import BouncyBall.Items.Board;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

public class Game {
    private static final double GRAVITY = 10.0 / 60;
    private final Ball ball;
    private final Board board;
    private final Canvas canvas;
    private final double[] canvasDim = {0.0, 0.0};

    public Game(double canvasDimX, double canvasDimY, Canvas canvas) {
        canvasDim[0] = canvasDimX;
        canvasDim[1] = canvasDimY;
        ball = new Ball(100.0, 100.0);
        reset();
        board = new Board(canvasDimX, canvasDimY);
        this.canvas = canvas;
    }

    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        ball.addToGroup(groupChildren);
        board.addToGroup(groupChildren);
        board.registerMouseAction();
    }

    // tick() is called every frame, handle main game logic here
    public void tick() {
        ball.setYVel(ball.getYVel() + GRAVITY);
        handleCollision();
        ball.move();
    }

    private void handleCollision() {
        Bounds canvasBounds = canvas.getBoundsInLocal();
        Bounds ballBounds = ball.getNode().getBoundsInLocal();
        Bounds boardBounds = board.getNode().getBoundsInLocal();
        if (ballBounds.intersects(boardBounds)) {
            ball.setYVel(-ball.getYVel());
        }
        if (ballBounds.getMinX() <= canvasBounds.getMinX() ||
                ballBounds.getMaxX() >= canvasBounds.getMaxX()) {
            ball.setXVel(-ball.getXVel());
        }
        if (ballBounds.getMaxY() >= canvasBounds.getMaxY()) {
            reset();
        }
    }

    public void reset() {
        ball.setXPos(100);
        ball.setYPos(100);
        ball.setXVel(50.0 / 60);
        ball.setYVel(0);
    }
}

