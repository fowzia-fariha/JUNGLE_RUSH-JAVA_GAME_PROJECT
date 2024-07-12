package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    private final Player player;
    private final GameScreen gameScreen;
    private final JungleRush game;
    private final Score gameOverText;
    private Texture backgroundTexture1;
    private Texture backgroundTexture2;
    private Rectangle gameOverRectangle,scoreTreeRect;

    public GameOverScreen(Player player, JungleRush game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        backgroundTexture1 = new Texture("Background/fin3.jpg");
        backgroundTexture2 = new Texture("Background/bgImage1.png");
        this.gameScreen = gameScreen;
        gameOverText = new Score("Fonts/robotoMonoRegular.ttf",32,1);
        int rectWidth = 800,rectHeight = 500;

        gameOverRectangle = new Rectangle((game.SCREEN_WIDTH-rectWidth)/2f,(game.SCREEN_HEIGHT-rectHeight)/2f,rectWidth,rectHeight);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.2f,1);
        int treeWidth = 200;
        scoreTreeRect = new Rectangle(game.SCREEN_WIDTH-treeWidth, game.SCREEN_HEIGHT-100, treeWidth, 25);
        game.batch.begin();
        // first background
        game.batch.draw(backgroundTexture1, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        // second background
        game.batch.draw(backgroundTexture2, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT - 350);

        gameOverText.blinkingEffect(0.04f,0.03f,0.3f);
        gameOverText.setRectangle(gameOverRectangle);
        gameOverText.setColor(new Color(gameOverText.getOpacity(),1,1,gameOverText.getOpacity()));
        gameOverText.draw(game.batch,"Tap Anywhere or Press Space To Restart");
        player.drawScoreTree(game.batch,scoreTreeRect,Color.WHITE);
        gameOverText.setRectangle(new Rectangle((game.SCREEN_WIDTH-600)/2f, game.SCREEN_HEIGHT - 400,600,400 ));
        gameOverText.draw(game.batch,"        Game Over\nHighest Score Achieved: "+player.getMaxScore());
        game.batch.end();

        update();
    }

    private void update()
    {
        if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
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
        backgroundTexture1.dispose();
        backgroundTexture2.dispose();

    }
}
