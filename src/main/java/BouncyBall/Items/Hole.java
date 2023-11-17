package BouncyBall.Items;

import BouncyBall.Drawable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Hole implements Drawable{
    private final Circle shape;

    Hole(Constructor cons) {
        this.shape = cons.shape;
    }

    @Override
    public Node getNode() {
        return shape;
    }

    @Override
    public void addToGroup(ObservableList<Node> group) {
        group.add(getNode());
    }

    public double getXPos() {
        return shape.getCenterX();
    }

    public double getYPos() {
        return shape.getCenterY();
    }

    public static class Constructor {
        private final Circle shape;

        public Constructor(double positionX, double positionY) {
            this.shape = new Circle(positionX + 100, positionY + 50, 30, Color.BLACK);
        }

        public Hole build() {
            return new Hole(this);
        }
    }
}
