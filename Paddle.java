import java.awt.*;

public class Paddle extends Sprite{
    private static final int paddleWidth = 10;
    private static final int paddleHeight= 100;
    private static final Color paddleColour = Color.white;
    private static final int distanceFromEdge = 40;

    public Paddle(Player player, int panelWidth, int panelHeight)
    {
        setHeight(paddleHeight);
        setWidth(paddleWidth);
        setColour(paddleColour);
        int xPos;
        if(player == Player.One){
            xPos = distanceFromEdge;
        }
        else{
            xPos = panelWidth - distanceFromEdge - getWidth();
        }
        setinitialPosition(xPos, panelHeight/2 -(getHeight()/2));
        resetToInitialPosition();
    }

}
