package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import GameStates.GameState;
import GameStates.Playing;
import Main.GameEngine;
import Main.GamePanel;
import Utils.AudioPlayer;
import Utils.Constants.ObjectSizeData;

import static Utils.Constants.UIData.URMButtons.URM_SIZE;
import static Utils.Constants.Path.PAUSE_PANEL;

public class PausePanel {
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private URMButton menuB, replayB, unpauseB;

    public PausePanel(Playing playing) {
		this.playing = playing;
		loadBackground();
		createURMButtons();
	}
    
    private void createURMButtons() {
        int menuX = 217*2;
        int replayX = 292*2;
        int unpauseX = 367*2;
        int bY = 200*2;

        menuB = new URMButton(menuX, bY, URM_SIZE*2, URM_SIZE*2, 2);
        replayB = new URMButton(replayX, bY,URM_SIZE*2,URM_SIZE*2, 1);
        unpauseB = new URMButton(unpauseX, bY,URM_SIZE*2,URM_SIZE*2, 0);

    }

    private void loadBackground() {
        InputStream is = getClass().getResourceAsStream(PAUSE_PANEL);
        try {

            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bgW = ObjectSizeData.PAUSE_PANEL.w;
        bgH = ObjectSizeData.PAUSE_PANEL.h;
        bgX = GamePanel.GAME_WIDTH / 2 - bgW / 2;
        bgY = GamePanel.GAME_HEIGHT / 2 - bgH / 2;
    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, .5F));
        g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        // UrmButtons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameEngine.audioPlayer.playSong(AudioPlayer.BACKGROUND);
                GameState.state = GameState.MENU;
                playing.initClasses();
                playing.unpauseGame();

            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed())
            {
                playing.unpauseGame();
                playing.initClasses();
                System.out.println("replay lvl!");
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);

    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}