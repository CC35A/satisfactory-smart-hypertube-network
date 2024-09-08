package src.main;

import src.vector.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class MapPanel extends JPanel implements Runnable{
    final int screenWidth = 1200;
    final int screenHeight = 800;

    int FPS = 60;

    private int camOffsetX;
    private int camOffsetY;
    private boolean isDragging;
    private int mouseBtn;
    private int startX;
    private int startY;
    private int mouseX;
    private int mouseY;
    Thread mapThread;

    public ArrayList<Node> nodes = new ArrayList<Node>();

    void populateNodes() {
        Node A = new Node("Station Alpha", new Vector2(0, 0));
        Node B = new Node("Station Bravo", new Vector2(200, 100));
        Node C = new Node("Station Charlie", new Vector2(100, 200));

        A.adjacentNodes.add(B);
        B.adjacentNodes.add(A);

        B.adjacentNodes.add(C);
        C.adjacentNodes.add(B);

        nodes.add(A);
        nodes.add(B);
        nodes.add(C);
    }

    public MapPanel() {
        populateNodes();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.requestFocusInWindow();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getButton());
                mouseBtn = e.getButton();
                if (e.getButton() == MouseEvent.BUTTON2) {
                    isDragging = true;
                    startX = e.getX();
                    startY = e.getY();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseBtn = 0;
                if (e.getButton() == MouseEvent.BUTTON2) {
                    isDragging = false;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int dx = e.getX() - startX;
                    int dy = e.getY() - startY;

                    camOffsetX += dx;
                    camOffsetY += dy;

                    startX = e.getX();
                    startY = e.getY();
                }
            }
        });
    }

    public void startMapThread(){
        mapThread = new Thread(this);
        mapThread.start();
    }

    @Override
    public void run(){

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (mapThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                // update: update game state
                update();

                // draw: draw game to screen
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000){
                System.out.printf("FPS: %d\n", drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point pos = this.getLocationOnScreen();

        mouseX = mouse.x - pos.x - camOffsetX;
        mouseY = mouse.y - pos.y - camOffsetY;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.GREEN);
        for (Node node : nodes) {
            g.fillOval((int) node.pos.x + camOffsetX, (int) node.pos.y + camOffsetY, 24, 24);
            g.drawString(node.label, (int) node.pos.x + camOffsetX, (int) node.pos.y + camOffsetY - 16);
            for (Node adjNode : node.adjacentNodes) {
                g.drawLine((int) node.pos.x + camOffsetX + 12, (int) node.pos.y + camOffsetY + 12, (int) adjNode.pos.x + camOffsetX + 12, (int) adjNode.pos.y + camOffsetY + 12);
            }
        }
    }
}
