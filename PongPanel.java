import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class PongPanel extends JPanel implements ActionListener, KeyListener {

    private final static Color BACKGROUND_COLOUR = Color.WHITE;
    private final static int TIMER_DELAY = 5;
    private final static int BALL_MOVEMENT_SPEED = 1;
    Ball ball;
    Paddle paddle1, paddle2;
    private final static int POINTS_TO_WIN = 5;
    int player1Score = 0;
    int player2Score =0;
    Player gameWinner;
    GameState gameState = GameState.Initialising;
    private final static String WINNER_TEXT ="Win";
    private final static int WINNER_TEXT_X=200;
    private final static int WINNER_TEXT_Y=200;
    private final static int WINNER_FONT_SIZE=40;
    private final static String WINNER_FONT_FAMILY= "serif";




    public void createObjects() {
        ball = new Ball(getWidth(), getHeight());
        paddle1 = new Paddle(Player.One, getWidth(), getHeight());
        paddle2 = new Paddle(Player.Two, getWidth(), getHeight());


    }

    public PongPanel() {
        setBackground(BACKGROUND_COLOUR);
        Timer timer = new Timer(TIMER_DELAY, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);

    }
    private void addScore(Player player){
        if(player ==Player.One)
        {
            player1Score++;
        }
        if(player == Player.Two){
            player2Score++;
        }


    }
    private void paintWinner(Graphics g){
        if(gameWinner !=null)
        {
            Font winnerFont = new Font (WINNER_FONT_FAMILY, Font.BOLD, WINNER_FONT_SIZE);
            g.setFont(winnerFont);
            int xPosition = getWidth()/2;
            if (gameWinner == Player.One){
                xPosition -=WINNER_TEXT_X;}
            else if (gameWinner == Player.Two){
                xPosition += WINNER_TEXT_X;
            }
                g.drawString(WINNER_TEXT,xPosition, WINNER_TEXT_Y);

        }
    }

    private void checkWin(){
        if (player1Score >=POINTS_TO_WIN)
        {
            gameWinner = Player.One;
            gameState = GameState.GameOver;

        }
        if (player2Score >= POINTS_TO_WIN)
        {
            gameWinner=Player.Two;
            gameState = GameState.GameOver;
        }
    }

    private void paintDottedLine(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.setPaint(Color.WHITE);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g2d.dispose();
    }

    private void update() {
        switch (gameState) {
            case Initialising: {
                createObjects();
                gameState = gameState.Playing;
                ball.setxVelocity(BALL_MOVEMENT_SPEED);
                ball.setyVelocity(BALL_MOVEMENT_SPEED);
                break;
            }
            case Playing: {
                moveObject(paddle1);
                moveObject(paddle2);
                moveObject(ball);
                checkWallBounce();
                checkPaddleBounce();
                checkWin();
                break;
            }
            case GameOver: {

                break;
            }
        }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(20, 100, 20, 100);
        paintDottedLine(g);
        if (gameState != GameState.Initialising) {
            paintSprite(g, ball);
            paintSprite(g, paddle1);
            paintSprite(g, paddle2);
            paintScores(g);

        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode() ==KeyEvent.VK_UP)
        {paddle2.setyVelocity(-1);}
        else if (event.getKeyCode() == KeyEvent.VK_DOWN)
        {paddle2.setyVelocity(1);}

        if(event.getKeyCode() == KeyEvent.VK_W)
        {paddle1.setyVelocity(-1);}
        else if (event.getKeyCode() ==KeyEvent.VK_S)
        {paddle1.setyVelocity(1);}
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() ==KeyEvent.VK_DOWN){
            paddle2.setyVelocity(0);
        }
        if (event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_S){
            paddle1.setyVelocity(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        update();
        repaint();
    }

    public void paintSprite(Graphics g, Sprite sprite) {
        g.setColor(sprite.getColour());
        g.fillRect(sprite.getxPosition(), sprite.getyPosition(),
                sprite.getWidth(), sprite.getHeight());
    }


    private void moveObject(Sprite obj) {
        obj.setxPosition(obj.getxPosition() + obj.getxVelocity(), getWidth());
        obj.setyPosition(obj.getyPosition() + obj.getyVelocity(), getHeight());


    }

    private void checkWallBounce() {
        if (ball.getxPosition() <= 0) {
            ball.setxVelocity(-ball.getxVelocity());
            addScore(Player.One);
            resetBall(); // hit left side of screen i.e player 1
        } else if (ball.getxPosition() >= getWidth() - ball.getWidth()) {
            ball.setxVelocity(-ball.getxVelocity());
            addScore(Player.Two);
            resetBall(); // hit right side of screen i.e player 2
        }

        if (ball.getyPosition() <= 0 || ball.getyPosition() >= getHeight() - ball.getHeight()) {
            ball.setyVelocity((-ball.getxVelocity()));
        }

    }

    private void resetBall() {
    ball.resetToInitialPosition();
    }

    private void checkPaddleBounce() {
    if(ball.getxVelocity() <0 && ball.getRectangle().intersects(paddle1.getRectangle())){
        ball.setxVelocity(BALL_MOVEMENT_SPEED);
    }
    if(ball.getxVelocity() >0 && ball.getRectangle().intersects(paddle2.getRectangle())){
     ball.setxVelocity((-BALL_MOVEMENT_SPEED));
    }
    }
    private void paintScores(Graphics g){
        int xPadding = 100;
        int yPadding = 100;
        int fontSize = 50;
        Font scoreFont = new Font ("Serif", Font.BOLD, fontSize);
        String leftScore = Integer.toString(player1Score);
        String rightScore = Integer.toString(player2Score);
        g.setFont(scoreFont);
        g.drawString(leftScore, xPadding,yPadding);
        g.drawString(rightScore, getWidth()-xPadding,yPadding);
    }
}