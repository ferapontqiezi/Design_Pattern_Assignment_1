package BouncyBall;

import BouncyBall.Items.Ball;

abstract class Strategy {
    public abstract void AlgorithmInterface(Ball ball);
}

class ConcreteStrategyA extends Strategy {
    @Override
    public void AlgorithmInterface(Ball ball) {
        ball.setCircleRadius(0);
        ball.setAlive(0);
    }
}

class ConcreteStrategyB extends Strategy {
    @Override
    public void AlgorithmInterface(Ball ball) {
        ball.setCircleRadius(0);
        ball.setAlive(0);
    }
}

class ConcreteStrategyC extends Strategy {
    @Override
    public void AlgorithmInterface(Ball ball) {
        if (ball.getAlive() == 2) {
            ball.respawn();
            ball.setAlive(1);
        } else if (ball.getAlive() == 1) {
            ball.setCircleRadius(0);
            ball.setAlive(0);
        }
    }
}

class Context {
    Strategy strategy;
    Ball ball;
    public Context(Strategy strategy, Ball ball) {
        this.strategy = strategy;
        this.ball = ball;
    }

    public void ContextInterface() {
        strategy.AlgorithmInterface(ball);
    }
}