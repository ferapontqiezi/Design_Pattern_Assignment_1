package BouncyBall;

public interface Movable {
    double getXPos();
    double getYPos();
    double getXVel(); // Vel stands for velocity
    double getYVel();
    double getMass();
    void setXPos(double xPos);
    void setYPos(double yPos);
    void setXVel(double xVel);
    void setYVel(double yVel);
    void move();
}
