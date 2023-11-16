package BouncyBall;

import BouncyBall.Items.Ball;
import BouncyBall.Items.Hole;
import BouncyBall.Items.Table;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    Drawable rootDrawables = new DummyDrawable();
    private List<Movable> movables = new ArrayList<>();
    private Table table;
    private List<Ball> balls;
    private List<Hole> holes;
    private final Canvas canvas;
    private Text textWin = new Text();
    private Text textLoss = new Text();

    public Game(Canvas canvas) {
        this.canvas = canvas;
        this.textWin.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        this.textLoss.setFont(Font.font("Times Romans", FontWeight.BOLD, 40));
        this.textWin.setText("You Win");
        this.textLoss.setText("You Loss");
        this.textWin.setX((canvas.getWidth() - textWin.getBoundsInLocal().getWidth()) / 2);
        this.textWin.setY((canvas.getHeight() - textWin.getBoundsInLocal().getHeight()) / 2);
        this.textLoss.setX((canvas.getWidth() - textLoss.getBoundsInLocal().getWidth()) / 2);
        this.textLoss.setY((canvas.getHeight() - textLoss.getBoundsInLocal().getHeight()) / 2);
        this.textWin.setOpacity(0);
        this.textLoss.setOpacity(0);
    }

    public void setTable(Table table) {
        this.table = table;
        this.table.getNode().setStroke(Color.valueOf("#402020"));
        this.table.getNode().setStrokeWidth(22);
        this.table.getNode().setStrokeType(StrokeType.OUTSIDE);
        this.table.getNode().setArcWidth(10);
        this.table.getNode().setArcHeight(10);
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
        movables.addAll(balls);
    }

    public void setHoles(List<Hole> holes) {
        this.holes = holes;
    }

    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);

        root.getChildren().add(textWin);
        root.getChildren().add(textLoss);

        for (Ball ball : balls) {
            ball.addToGroup(groupChildren);
            if (ball.getColour().matches("0xffffffff")){
                ball.registerMouseAction(root);
            }
        }

        for (Hole hole : holes) {
            hole.addToGroup(groupChildren);
        }

        rootDrawables.addToGroup(groupChildren);
    }

    // tick() is called every frame, handle main game logic here
    public void tick() {
        for (Movable movable : movables) {
            movable.move();
            useFriction(movable);
            handleTableCollision();
            handleBallsCollision();
            handleHolesCollision();
            winDetecion();
        }
    }

    /**
     * This uses the optimised physics algorithm discussed here:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     * which has been converted into Java/JavaFX
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA The mass of ball A (for the moment this should always be the same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB The mass of ball B (for the moment this should always be the same as ball A)
     *
     * @return A Pair<Point2D, Point2D> in which the first (key) Point2D is the new delta x,y vec
    tor for ball A, and the second (value) Point2D is the new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA, Point2D positionB, Point2D velocityB, double massB) {
        // Find the angle of the collision - basically where is ball B relative to ball A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();
        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);
        // If you don't detect the collision at just the right time, balls might collide again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the original velocities
        if (vB <= 0 && vA >= 0) {
            return new Pair<>(velocityA, velocityB);
        }
        // This is the optimisation function described in the gamasutra link. Rather than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);
        // Now we apply that calculated function to the pair of balls to obtain their final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));
        return new Pair<>(velAPrime, velBPrime);
    }

    private void handleHolesCollision() {
        for (int i = 0; i < 5; i++) {
            Ball ball = balls.get(i);
            if (ball.getAlive() != 0){
                for (int j = 0; j < 6; j++) {
                    Hole hole = holes.get(j);
                    double distance = calculateDistance(ball.getXPos(), ball.getYPos(), hole.getXPos(), hole.getYPos());
                    double radiusSum = 25;
                    if (distance < radiusSum) {
                        if (ball.getColour().matches("0xffffffff")) {
                            Context context1 = new Context(new ConcreteStrategyA(), ball);
                            context1.ContextInterface();
                            this.textLoss.setOpacity(1);
                        } else if (ball.getColour().matches("0xff0000ff")) {
                            Context context2 = new Context(new ConcreteStrategyB(), ball);
                            context2.ContextInterface();
                        } else if (ball.getColour().matches("0x0000ffff")) {
                            Context context3 = new Context(new ConcreteStrategyC(), ball);
                            context3.ContextInterface();
                            intersectDetecion(i);
                        }
                    }
                }
            }
        }
    }

    private void handleBallsCollision() {
        for (int i = 0; i < 5; i++) {
            Ball ballA = balls.get(i);
            if (ballA.getAlive() != 0) {
                for (int j = i + 1; j < 5; j++) {
                    Ball ballB = balls.get(j);
                    if (ballB.getAlive() != 0){
                        double distance = calculateDistance(ballA.getXPos(), ballA.getYPos(), ballB.getXPos(), ballB.getYPos());
                        double radiusSum = 20;

                        if (distance < radiusSum) {
                            Point2D positionA = new Point2D(ballA.getXPos(), ballA.getYPos());
                            Point2D velocityA = new Point2D(ballA.getXVel(), ballA.getYVel());
                            double massA = ballA.getMass();

                            Point2D positionB = new Point2D(ballB.getXPos(), ballB.getYPos());
                            Point2D velocityB = new Point2D(ballB.getXVel(), ballB.getYVel());
                            double massB = ballB.getMass();

                            Pair<Point2D, Point2D> newVelocities = calculateCollision(positionA, velocityA, massA, positionB, velocityB, massB);

                            // Update ball velocities after collision
                            ballA.setXVel(newVelocities.getKey().getX());
                            ballA.setYVel(newVelocities.getKey().getY());

                            ballB.setXVel(newVelocities.getValue().getX());
                            ballB.setYVel(newVelocities.getValue().getY());
                        }
                    }

                }
            }
        }
    }

    private void handleTableCollision() {
        Bounds tableBounds = table.getNode().getBoundsInLocal();
        for (Ball ball : balls){
            if (ball.getAlive() != 0) {
                Bounds  ballBounds = ball.getNode().getBoundsInLocal();
                if (ballBounds.getMaxX() >= (tableBounds.getMaxX()+100-22) ||
                        ballBounds.getMinX() <= (tableBounds.getMinX()+100+22)) {
                    ball.setXVel(-ball.getXVel());
                }
                if (ballBounds.getMaxY() >= (tableBounds.getMaxY()+50-22) ||
                        ballBounds.getMinY() <= (tableBounds.getMinY()+50+22)) {
                    ball.setYVel(-ball.getYVel());
                }
            }

        }
    }

    private void winDetecion() {
        for (int i = 0; i < 5; i++) {
            if (balls.get(i).getAlive() != 0 && !balls.get(i).getColour().matches("0xffffffff")) {
                return;
            }
        }
        this.textWin.setOpacity(1);
        for (int i = 0; i < 5; i++) {
            if (balls.get(i).getColour().matches("0xffffffff")) {
                balls.get(i).setAlive(0);
            }
        }
    }

    private void intersectDetecion(int i) {
        for (int j = 0; j < 5; j++) {
            if (j != i && balls.get(j).getAlive() != 0 && calculateDistance(balls.get(i).getXPos(),
                    balls.get(i).getYPos(), balls.get(j).getXPos(), balls.get(j).getYPos()) < 20) {
                balls.get(i).setXPos(balls.get(i).getXPos() + 40);
                balls.get(i).setYPos(balls.get(i).getYPos() + 40);
                intersectDetecion(i);
            }
        }
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void useFriction(Movable movable) {
        double friction = this.table.getFriction() * movable.getMass() * 9.8 * 0.2;
        double a = friction / movable.getMass();
        double vx = movable.getXVel();
        double vy = movable.getYVel();
        double speed = Math.sqrt(vx * vx + vy *vy);

        if (vx > 0) {
            double ax = -a * (vx / speed);
            vx = vx + ax * (1.0/60.0);
            if (vx < 0) {
                vx = 0;
            }
            movable.setXVel(vx);
        }else if (vx < 0) {
            double ax = -a * (vx / speed);
            vx = vx + ax * (1.0/60.0);
            if (vx > 0) {
                vx = 0;
            }
            movable.setXVel(vx);
        }

        if (vy > 0) {
            double ay = -a * (vy / speed);
            vy = vy + ay * (1.0/60.0);
            if (vy < 0) {
                vy = 0;
            }
            movable.setYVel(vy);
        }else if (vy < 0) {
            double ay = -a * (vy / speed);
            vy = vy + ay * (1.0/60.0);
            if (vy > 0) {
                vy = 0;
            }
            movable.setYVel(vy);
        }
    }

    class DummyDrawable implements Drawable {

        @Override
        public Node getNode() {
            return null;
        }

        @Override
        public void addToGroup(ObservableList<Node> group) {}
    }
}

