import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener ,KeyListener{


    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x = x;
            this.y = y;
        }

    }
int boardWidth;
int boardHeight;
int tileSize = 25;

//Snake
Tile snakeHead;
ArrayList<Tile>snakeBody;

//Food
Tile food;
Random random;

//Game logic
Timer gameloop;
int VelocityX;
int VelocityY;
boolean gameOver = false;


SnakeGame(int boardWidth,int boardHeight){
this.boardWidth = boardWidth;
this.boardHeight = boardHeight;
setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
setBackground(Color.black);
addKeyListener(this);
setFocusable(true);

snakeHead = new Tile(5,5);
snakeBody = new ArrayList<Tile>();

food = new Tile(10,10);
random = new Random();
PlaceFood();

VelocityX = 0;
VelocityY = 0;

gameloop = new Timer(100,this);
gameloop.start();
}


    public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);
}

   private void draw(Graphics g) {
//    //Grid
//        for (int i = 0; i<boardWidth/tileSize ; i++){
//            //(x1,y1,x2,y2)
//            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
//            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
//
//        }
        //Food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);
        //Snake Head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize ,snakeHead.y * tileSize,tileSize,tileSize);

        //Snake body
        for (int i = 0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize,snakePart.y*tileSize,tileSize,tileSize);
        }

        //Score
        g.setFont(new Font("Arial" ,Font.PLAIN,25));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over : " +String.valueOf(snakeBody.size()),tileSize -16,tileSize);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverText = "Game Over";
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            int textHeight = g.getFontMetrics().getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2;
            g.drawString(gameOverText, x, y);
        }else {
            g.drawString("Score: " +String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
}



    public void PlaceFood() {
    food.x = random.nextInt(boardWidth/tileSize); // 600/25=24
    food.y = random.nextInt(boardHeight/tileSize);
    }
    public boolean collision(Tile tile1,Tile tile2){
    return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    private void move() {
        //eat
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            PlaceFood();
        }

        //snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += VelocityX;
        snakeHead.y += VelocityY;

        //game Over Conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //Collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
             snakeHead.y *tileSize <0 || snakeHead.y * tileSize > boardHeight){
            gameOver =true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gameOver){
        gameloop.stop();

    }
    }
    @Override
    public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP && VelocityY !=1 ){
        VelocityX = 0;
        VelocityY = -1;
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN  && VelocityY !=-1 ){
        VelocityX = 0;
        VelocityY = 1;
    }else if (e.getKeyCode() == KeyEvent.VK_LEFT  && VelocityX !=1 ){
        VelocityX = -1;
        VelocityY =0;
    }else if (e.getKeyCode() == KeyEvent.VK_RIGHT  && VelocityX !=-1 ){
        VelocityX = 1;
        VelocityY = 0;
    }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}