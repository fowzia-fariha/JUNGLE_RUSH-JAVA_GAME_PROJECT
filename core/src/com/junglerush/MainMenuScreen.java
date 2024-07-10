package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final JungleRush game;
    private final Texture logoImage;
    private final Texture tigImage;
    private final Texture lioImage;
    private final Texture deerImage;
    private float opacityControl = 0.3f;
    private boolean increase = true;
    public MainMenuScreen(JungleRush game)
    {
        this.game = game;
        this.logoImage = new Texture(Gdx.files.internal("Background/logoImage.png"));
        this.tigImage = new Texture(Gdx.files.internal("Background/tigf.png"));
        this.lioImage = new Texture(Gdx.files.internal("Background/lionf.png"));
        this.deerImage = new Texture(Gdx.files.internal("Background/deer.png"));
    }

    public void blinkingEffect(float lowerLimit, BitmapFont font,float increaseValue,float decreaseValue)
    {
        font.setColor(this.opacityControl,1,1, this.opacityControl);
        if(increase)
            this.opacityControl +=increaseValue;
        else
            this.opacityControl -= decreaseValue;
        if(this.opacityControl >= 1) increase = false;
        else if(this.opacityControl <= lowerLimit) increase = true;
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.2f,1);
        String welcome = "Welcome To Jungle Rush!",
                play = "Tap Anywhere Or Press Space To Play";
       blinkingEffect(0.3f,game.fontRegular,0.04f,0.03f);

        game.batch.begin();

        float logoY = (game.SCREEN_HEIGHT - logoImage.getHeight()) / 2 + 50;
        float logoWidth = ((game.SCREEN_WIDTH +150)/ 2)  ;
        game.batch.draw(logoImage, (game.SCREEN_WIDTH - logoWidth) / 2, logoY - 5, logoWidth, logoImage.getHeight());

        // Scaling factors for the images
        float scaleTig = 1.4f;
        float scaleLio = 0.75f;
        float scaleDeer = 1.3f;

        //  tig, lio, and deer images
        game.batch.draw(tigImage, 500, -30, tigImage.getWidth() * scaleTig, tigImage.getHeight() * scaleTig);
        game.batch.draw(lioImage, 1000, 0, lioImage.getWidth() * scaleLio, lioImage.getHeight() * scaleLio);
        game.batch.draw(deerImage, -200, -100, deerImage.getWidth() * scaleDeer, deerImage.getHeight() * scaleDeer);

        GlyphLayout g1 = new GlyphLayout(game.fontRegular,welcome);
        float textX = (game.SCREEN_WIDTH - g1.width)/2;
        float textY = ((game.SCREEN_HEIGHT - g1.height)/2) +50;
        game.fontRegular.draw(game.batch,g1,textX,textY);

        g1 = new GlyphLayout(game.fontRegular,play);
        textX = (game.SCREEN_WIDTH - g1.width)/2;
        textY = (game.SCREEN_HEIGHT - g1.height)/2;
        game.fontRegular.draw(game.batch,g1,textX,textY);

        game.batch.end();

        if(Gdx.input.isTouched()||Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            game.setScreen(new GameScreen(game));
            dispose();
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
        tigImage.dispose();
        lioImage.dispose();
        deerImage.dispose();

    }
}
