package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.css.Rect;

import java.math.BigInteger;
import java.util.Objects;

public class GameOverScreen implements Screen {
    private final Player player;
    private final JungleRush game;
    private final Score gameOverText;
    private Texture logoImage;
    private Texture logoImage1;
    private Texture logoImage2;
    private Texture logoImage3;
    private final ScoreManager scoreManager;

    public GameOverScreen(Player player, JungleRush game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        scoreManager = new ScoreManager();
        logoImage = new Texture("Background/logoImage.png");
        logoImage1 = new Texture("Background/logoImage1.png");
        logoImage2 = new Texture("Background/logoImage.png");
        logoImage3 = new Texture("Background/logoImage1.png");
        gameOverText = new Score("Fonts/robotoMonoRegular.ttf", 32, 1);
        gameOverText.setColor(Color.WHITE);

        scoreManager.addScore(JungleRush.playerName, player.getMaxScore(), 14);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        game.batch.begin();

        // Draw logo images
        float logoWidth = (game.SCREEN_WIDTH / 2) - 20;
        float logoWidth2 = (game.SCREEN_WIDTH / 2) + 200;
        float logoY = game.SCREEN_HEIGHT - logoImage.getHeight() - 100;
        //game.batch.draw(logoImage, (int) (game.SCREEN_WIDTH / 2 - 490), (int) (logoY + logoImage.getHeight() / 2) - 50, logoWidth, logoImage.getHeight() - 100);
        float logoY2 = logoY - logoImage2.getHeight() - 40;
        //game.batch.draw(logoImage2, (game.SCREEN_WIDTH - logoWidth) / 2 - 285, logoY2 + 140, logoWidth2, logoImage2.getHeight() - 130);
        game.batch.draw(logoImage3, game.SCREEN_WIDTH - 410, game.SCREEN_HEIGHT - 75, 400, 45);

        // Draw the "Tap Anywhere or Press Space To Restart" text
        gameOverText.blinkingEffect(0.04f, 0.03f, 0.3f);
        gameOverText.setColor(new Color(gameOverText.getOpacity(), 1, 1, gameOverText.getOpacity()));
        gameOverText.drawXY(game.batch, "Tap Anywhere or Press Space To Restart", game.SCREEN_WIDTH / 2 - 180, game.SCREEN_HEIGHT / 2);

        // Draw the "Game Over" and highest score text
        gameOverText.setColor(new Color(gameOverText.getOpacity(), 1, 1, 1));
        gameOverText.drawXY(game.batch, "        Game Over\nHighest Score Achieved: " + player.getMaxScore(), game.SCREEN_WIDTH / 2 - 180, game.SCREEN_HEIGHT / 2 + 200);

        // Draw the player scores with logo background
        Array<PlayerData> players = scoreManager.getPlayersData();
        Rectangle nameRect = new Rectangle(game.SCREEN_WIDTH - 410, game.SCREEN_HEIGHT - 100, 300, 50);
        Rectangle scoreRect = new Rectangle(nameRect.x + nameRect.width, nameRect.y, game.SCREEN_WIDTH - (nameRect.x + nameRect.width), 50);
        gameOverText.setRectangle(new Rectangle(game.SCREEN_WIDTH - 410, game.SCREEN_HEIGHT - 100, 410, 100));
        gameOverText.setColor(new Color(gameOverText.getOpacity(), 1, 1, 1));
        gameOverText.draw(game.batch, "All Time Best Scores");

        int cnt = 0;
        float logoHeight = logoImage1.getHeight();
        float yPosition = nameRect.y;

        for (PlayerData player : players) {
            // Draw logo background for each text line
            game.batch.draw(logoImage1, nameRect.x - 30, yPosition + 7 - (logoHeight - 80) / 2, 450, 40);
            gameOverText.setColor(new Color(1, gameOverText.getOpacity(), 1, 1));
            if (cnt == 0 && Objects.equals(player.getPlayerName(), JungleRush.playerName) && Objects.equals(player.getScore(), this.player.getMaxScore())) {
                cnt++;
                gameOverText.setColor(new Color(0, 1, gameOverText.getOpacity(), 1));
            }

            String scoreValue = player.getScore().toString();
            if (player.getScore().compareTo(BigInteger.valueOf(10000)) >= 0)
                scoreValue = NumberFormatter.formatBigInteger(player.getScore());

            gameOverText.setRectangle(nameRect);
            gameOverText.draw(game.batch, player.getPlayerName(), false);
            gameOverText.setRectangle(scoreRect);
            gameOverText.draw(game.batch, ": " + scoreValue, false);

            yPosition -= 50;
            nameRect.y = yPosition;
            scoreRect.y = yPosition;
        }

        game.batch.end();
        update();
    }

    private void update() {
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        logoImage.dispose();
        logoImage1.dispose();
        logoImage2.dispose();
        logoImage3.dispose();
    }
}
