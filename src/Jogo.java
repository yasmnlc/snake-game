import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Jogo extends JPanel implements ActionListener, KeyListener{

    int boardWidth;    
    int boardHeight;
    int tileSize = 25;

    private Image backgroundImage;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    private Image headImage;
    private Color corCobra;

    Tile food;
    Random random;
    private Image foodImage;

    Timer gameLoop;
    int velocityX; //direção horizontal
    int velocityY; //direção vertical
    boolean gameOver = false;

    private String name;

    private Clip eatSound;
    private Clip gameOverSound;
    private Clip movementSoundUp;
    private Clip movementSoundDown;
    private Clip movementSoundLeft;
    private Clip movementSoundRight;

    private void loadSounds(){
        try {
            AudioInputStream eatSoundStream = AudioSystem.getAudioInputStream(new File("assets\\comer.wav"));
            eatSound = AudioSystem.getClip();
            eatSound.open(eatSoundStream);

            AudioInputStream gameOverSoundStream = AudioSystem.getAudioInputStream(new File("assets\\gameover.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverSoundStream);

            AudioInputStream movementSoundUpStream = AudioSystem.getAudioInputStream(new File("assets\\mov_up.wav"));
            movementSoundUp = AudioSystem.getClip();
            movementSoundUp.open(movementSoundUpStream);

            AudioInputStream movementSoundDownStream = AudioSystem.getAudioInputStream(new File("assets\\mov_down.wav"));
            movementSoundDown = AudioSystem.getClip();
            movementSoundDown.open(movementSoundDownStream);

            AudioInputStream movementSoundLeftStream = AudioSystem.getAudioInputStream(new File("assets\\mov_left.wav"));
            movementSoundLeft = AudioSystem.getClip();
            movementSoundLeft.open(movementSoundLeftStream);

            AudioInputStream movementSoundRightStream = AudioSystem.getAudioInputStream(new File("assets\\mov_right.wav"));
            movementSoundRight = AudioSystem.getClip();
            movementSoundRight.open(movementSoundRightStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Jogo(int boardWidth, int boardHeight, String name, Color corCobra){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.name = name;
        
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));

        try {
            backgroundImage = ImageIO.read(new File("assets\\fundo.png"));
            headImage = ImageIO.read(new File("assets\\head.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        this.corCobra = corCobra;

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

        try {
            foodImage = ImageIO.read(new File("assets\\maça.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadSounds();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, this);

        draw(g);
    }

    public void setCorCobra(Color corCobra) {
        this.corCobra = corCobra;
    }

    public void draw(Graphics g){
        int foodImageSize = 30;
        g.drawImage(foodImage, food.x * tileSize, food.y * tileSize, foodImageSize, foodImageSize, this);

        g.setColor(corCobra);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        g.drawImage(headImage, snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, this);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.black);
        g.drawString("Jogador: " + name, tileSize - 16, tileSize * 2);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        if(gameOver){
            g.setColor(Color.red);
            String gameOverText = "Game Over: " + String.valueOf(snakeBody.size());
            g.drawString(gameOverText, tileSize - 16, tileSize);

            g.setColor(Color.black);
            String restartText = "Pressione a tecla R para jogar novamente";
            int restartTextWidth = g.getFontMetrics().stringWidth(restartText);
            int restartTextX = (boardWidth - restartTextWidth) / 2;
            int restartTextY = boardHeight / 2;
            g.drawString(restartText, restartTextX, restartTextY + g.getFontMetrics().getHeight());
        }
        else{
            g.setColor(Color.black);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood(){
        //600/25 = 24 ladrilhos de largura e altura
        food.x = random.nextInt(boardWidth/tileSize); 
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            playEatSound();
            placeFood();
        }

        for(int i=snakeBody.size()-1; i>=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }
        if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
           snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver){
            playGameOverSound();
            gameLoop.stop();
        }
    }

    public void restartGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
    
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
    
        gameLoop.restart();
    }

    private void playEatSound() {
        if (eatSound != null) {
            eatSound.setFramePosition(0);
            eatSound.start();
        }
    }

    private void playMovementSoundUp() {
        if (movementSoundUp != null) {
            movementSoundUp.setFramePosition(0);
            movementSoundUp.start();
        }
    }
    private void playMovementSoundDown() {
        if (movementSoundDown != null) {
            movementSoundDown.setFramePosition(0);
            movementSoundDown.start();
        }
    }
    private void playMovementSoundLeft() {
        if (movementSoundLeft != null) {
            movementSoundLeft.setFramePosition(0);
            movementSoundLeft.start();
        }
    }
    private void playMovementSoundRight() {
        if (movementSoundRight != null) {
            movementSoundRight.setFramePosition(0);
            movementSoundRight.start();
        }
    }

    private void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
            playMovementSoundUp();
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
            playMovementSoundDown();
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
            playMovementSoundLeft();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
            playMovementSoundRight();
        }
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    //Funçoes obrigatorias de KeyListener
    //Não é necessario implementar
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}