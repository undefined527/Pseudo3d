import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.lang.Math;

public class GamePanel extends JPanel implements ActionListener
{
    static final Map MAP = App.currentMap;
    static final int[][] PARSED_MAP = MAP.parseMap();
    static final int SCREEN_SIZE = 800;
    static final int UNIT_SIZE = (int)(SCREEN_SIZE / MAP.size);
    static final int RESOLUTION = 100;
    static final int FOV = 90;
    static final int DELAY = 10;
    static final int PLAYER_SIZE = UNIT_SIZE / 2;
    static final int MOVE_STEP = 4; // Pixels
    static final int ROTATE_STEP = 2; // Degrees
    static final int RAYCAST_STEP = 2;
    static final int RAYCAST_DISTANCE = UNIT_SIZE * 10;

    double playerX = 1 * UNIT_SIZE + 0.25 * UNIT_SIZE;
    double playerY = 1 * UNIT_SIZE + 0.25 * UNIT_SIZE;
    double playerOrientation = 0;
    HashSet<Integer> keysPressed = new HashSet<>();
    boolean running = false;
    boolean threeD = true;

    Timer timer;
    Random random;

    GamePanel()
    {
        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new Controller());

        startGame();
    }
    
    public void startGame()
    {
        running = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        super.paintComponent(g);

        if (threeD)
        {
            draw(g, g2d);
        }
        else
        {
            draw2d(g, g2d);
        }
    }

    public double[] raycast(double x, double y, double orientation)
    {
        double lastX = x;
        double lastY = y;
        double currentX = x;
        double currentY = y;

        for (int i = 0; i < (RAYCAST_DISTANCE / RAYCAST_STEP); i++)
        {
            currentX += Math.cos(Math.toRadians(orientation)) * RAYCAST_STEP;
            currentY += Math.sin(Math.toRadians(orientation)) * RAYCAST_STEP;

            if (PARSED_MAP[(int)(lastX / UNIT_SIZE)][(int)(lastY / UNIT_SIZE)] == 1)
            {
                return new double[] {lastX, lastY};
            }

            lastX = currentX;
            lastY = currentY;
        }

        return new double[] {lastX, lastY};
    }

    public void draw2d(Graphics g, Graphics2D g2d)
    {
        if (running)
        {
            // Drawing map

            for (int x = 0; x < PARSED_MAP.length; x++)
            {
                for (int y = 0; y < PARSED_MAP[x].length; y++)
                {
                    if (PARSED_MAP[x][y] == 1)
                    {
                        g.setColor(Color.white);
                    }
                    else 
                    {
                        g.setColor(Color.black);
                    }

                    g.fillRect(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Drawing player

            AffineTransform originalTransform = g2d.getTransform();

            g2d.translate(playerX + PLAYER_SIZE / 2, playerY + PLAYER_SIZE / 2);
            g2d.rotate(Math.toRadians(playerOrientation));
            g2d.translate(-PLAYER_SIZE / 2, -PLAYER_SIZE / 2);
            g2d.fillRect(0, 0, PLAYER_SIZE, PLAYER_SIZE);

            g2d.setTransform(originalTransform);

            g.setColor(Color.red);

            for (int i = 0; i < RESOLUTION; i++)
            {
                double[] position = raycast(playerX + PLAYER_SIZE / 2, playerY + PLAYER_SIZE / 2, playerOrientation - FOV / 2 + ((double)FOV / RESOLUTION * i));

                if (position != null)
                {
                    g.fillRect((int)position[0], (int)position[1], 5, 5);
                }   
            }
        }
    }

    public void draw(Graphics g, Graphics2D g2d)
    {
        if (running)
        {
            // Ceiling Shading effect

            GradientPaint gpTop = new GradientPaint(new Point2D.Float(SCREEN_SIZE / 2, 0), Color.DARK_GRAY, new Point2D.Float((float) SCREEN_SIZE / 2, (float) SCREEN_SIZE / 2), Color.BLACK);
            GradientPaint gpBottom = new GradientPaint(new Point2D.Float(SCREEN_SIZE / 2, SCREEN_SIZE), Color.DARK_GRAY, new Point2D.Float((float) SCREEN_SIZE / 2, (float) SCREEN_SIZE / 2), Color.BLACK);
            
            g2d.setPaint(gpTop);
            g2d.fillRect(0, 0, SCREEN_SIZE, SCREEN_SIZE / 2);

            g2d.setPaint(gpBottom);
            g2d.fillRect(0, SCREEN_SIZE / 2, SCREEN_SIZE, SCREEN_SIZE / 2);

            // Wall rendering

            for (int i = 0; i < RESOLUTION; i++)
            {
                double relativeAngle = playerOrientation - FOV / 2 + ((double)FOV / RESOLUTION * i);
                double[] position = raycast(playerX + PLAYER_SIZE / 2, playerY + PLAYER_SIZE / 2, relativeAngle);
                
                if (position != null)
                {
                    double distance = Math.sqrt(Math.pow(playerX + PLAYER_SIZE / 2 - position[0], 2) + Math.pow(playerY + PLAYER_SIZE / 2 - position[1], 2));
                    double lensCorrectedDistance = distance * Math.cos(Math.toRadians(relativeAngle - playerOrientation));
                    double height = Math.min(20000 / distance, SCREEN_SIZE);
                    double lensCorrectedHeight = Math.min(20000 * Math.cos(Math.toRadians(relativeAngle - playerOrientation)) / lensCorrectedDistance - (20000 / RAYCAST_DISTANCE), SCREEN_SIZE);
    
                    g.setColor(Color.getHSBColor(0, 0, (float)(height / SCREEN_SIZE)));
                    g.fillRect(SCREEN_SIZE / RESOLUTION * i, (int)((SCREEN_SIZE - lensCorrectedHeight) / 2), SCREEN_SIZE / (RESOLUTION), (int)(lensCorrectedHeight));
                }
            }
        }
    }

    public void move()
    {
        if (running)
        {
            if (keysPressed.contains(KeyEvent.VK_W))
            {
                playerX += Math.cos(Math.toRadians(playerOrientation)) * MOVE_STEP;
                playerY += Math.sin(Math.toRadians(playerOrientation)) * MOVE_STEP;
            }

            if (keysPressed.contains(KeyEvent.VK_S))
            {
                playerX -= Math.cos(Math.toRadians(playerOrientation)) * MOVE_STEP;
                playerY -= Math.sin(Math.toRadians(playerOrientation)) * MOVE_STEP;
            }

            if (keysPressed.contains(KeyEvent.VK_A))
            {
                playerOrientation -= ROTATE_STEP;
            }

            if (keysPressed.contains(KeyEvent.VK_D))
            {
                playerOrientation += ROTATE_STEP;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if (running)
        {
            move();
        }

        repaint();
    }

    public class Controller extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent event)
        {
            keysPressed.add(event.getKeyCode());
        }  
        
        @Override
        public void keyReleased(KeyEvent event)
        {
            keysPressed.remove(event.getKeyCode());
        }
    }
}