package BouncyBall.Items;

import BouncyBall.Drawable;
import BouncyBall.Movable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Ball implements Movable, Drawable {
    // x方向上的速度
    private double velocityX;
    // y方向上的速度
    private double velocityY;
    // 质量
    private final double mass;
    // 桌球形状，绘制圆圈，画圆
    private final Circle shape;
    // 击打白球的拖拽线
    private final Line dragLine;
    // 是否在拖拽
    private boolean dragging = false;
    // 球的存活命数，0表示进洞死亡，1表示红球或者死过一次的蓝球，2表示一次没死的蓝球
    private int alive;
    // 初始位置坐标(X,Y)
    private double initialPostionX;
    private double initialPostionY;
    // 球的半径
    private static final double RADIUS = 15.0;
    // 球的速度(Vx,Vy)
    private final double[] velocity = {0.0, 0.0};

    public Ball(Constructor cons) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = cons.mass;
        this.shape = cons.shape;
        this.dragLine = cons.dragLine;
        this.alive = cons.alive;
        this.initialPostionX = cons.initialPositionX;
        this.initialPostionY = cons.initialPositionY;
    }


    @Override
    public Node getNode() {
        return this.shape;
    }

    @Override
    public void addToGroup(ObservableList<Node> group) {
        group.add(this.shape);
    }

    // 击打白球的拖拽线
    public void registerMouseAction(Group root) {
        root.getChildren().add(dragLine);

        this.shape.setOnMousePressed(event -> {
            if (getXVel() == 0 && getYVel() == 0){
                dragLine.setOpacity(1);
                dragLine.setStartX(shape.getCenterX());
                dragLine.setStartY(shape.getCenterY());
                dragLine.setEndX(shape.getCenterX());
                dragLine.setEndY(shape.getCenterY());
                this.dragging = true;
            }
        });

        this.shape.setOnMouseDragged(event -> {
            if (this.dragging) {
                dragLine.setEndX(event.getSceneX());
                dragLine.setEndY(event.getSceneY());
            }
        });

        root.setOnMouseReleased(event -> {
            if (this.dragging) {
                dragLine.setOpacity(0);
                this.dragging = false;
                double dragX = event.getSceneX() - this.shape.getCenterX();
                double dragY = event.getSceneY() - this.shape.getCenterY();
                this.setXVel(-dragX / 10);
                this.setYVel(-dragY / 10);
            }
        });
    }

    // 蓝球复活
    public void respawn() {
        setXPos(initialPostionX);
        setYPos(initialPostionY);
        setXVel(0);
        setYVel(0);
    }

    // get构造器
    public String getColour() {
        return this.shape.getFill().toString();
    }

    @Override
    public double getXPos() {
        return this.shape.getCenterX();
    }

    @Override
    public double getYPos() {
        return this.shape.getCenterY();
    }

    @Override
    public double getXVel() {
        return this.velocity[0];
    }

    @Override
    public double getYVel() {
        return this.velocity[1];
    }

    public double getMass() {
        return mass;
    }

    public int getAlive() {
        return alive;
    }


    // set构造器
    // 以下都是根据json文件读取到的配置进行设置，共6个方法，包括设置位置(X,Y)，速度(X,Y)，半径，是否存活
    @Override
    public void setXPos(double xPos) {
        this.shape.setCenterX(xPos);
    }

    @Override
    public void setYPos(double yPos) {
        this.shape.setCenterY(yPos);
    }

    @Override
    public void setXVel(double xVel) {
        velocityX = xVel;
    }

    @Override
    public void setYVel(double yVel) {
        velocityY = yVel;
    }

    public void setCircleRadius(double radius) {
        shape.setRadius(radius);
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }


    @Override
    public void move() {
        double xPos = getXPos() + getXVel();
        double yPos = getYPos() + getYVel();
        setXPos(xPos);
        setYPos(yPos);
    }

    // 构造器
    public static class Constructor{
        private Circle shape;
        private Line dragLine;
        private double velocityX;
        private double velocityY;
        private double mass;
        private int alive;
        private double initialPositionX;
        private double initialPositionY;

        public Constructor(String colour, double positionX, double positionY, double velocityX, double velocityY, double mass){
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.mass = mass;
            this.shape = new Circle(positionX + 100, positionY + 50, 15, Color.valueOf(colour));
            this.dragLine = new Line();
            if (colour.matches("blue")) {
                this.alive = 2;
            } else {
                this.alive = 1;
            }
            this.initialPositionX = positionX + 100;
            this.initialPositionY = positionY + 50;
        }

        public Ball build(){
            return new Ball(this);
        }
    }
}
