package BouncyBall.Items;

import BouncyBall.Drawable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Table implements Drawable{
    private final double friction;
    private final Rectangle rectangle;

    private Table(Constructor cons) {
        this.friction = cons.friction;
        this.rectangle = cons.rectangle;
    }

    @Override
    public Rectangle getNode() {
        return this.rectangle;
    }

    @Override
    public void addToGroup(ObservableList<Node> group) {
        group.add(getNode());
    }

    // Getter methods
    public Double getFriction() {
        return this.friction;
    }

    public static class Constructor {
        private double friction;
        private Rectangle rectangle;

        public Constructor(String colour, long sizeX, long sizeY, double friction) {
            this.friction = friction;
            this.rectangle = new Rectangle(sizeX,sizeY,Color.valueOf(colour));
            rectangle.setTranslateX(100);
            rectangle.setTranslateY(50);
        }

        public Table build() {
            return new Table(this);
        }
    }
}
