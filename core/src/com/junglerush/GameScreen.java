package com.junglerush;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameScreen implements Screen {

    private final JungleRush game;
    private final MainMenuScreen menu;
    private Texture tRoad,tRiver,tMainCharacter;
    private Array<Texture> trees,backgrounds,animalsCrab;
    private Array<Rectangle> roadRect, treeRectLeft, treeRectRight,backgroundRect,animalsRect,roadBoarder;
    private Rectangle riverRect,mcRect;

    private final int TOTAL_TILES = 20,aniSpeed = 20,SPEED = 5,treeRandomFactor = 6,score = 2;
    private int ELEMENT_WIDTH,ELEMENT_HEIGHT,JUNGLE_FACTOR,ROAD_FACTOR,RIVER_FACTOR,JUNGLE_WIDTH,ROAD_WIDTH,BOARDER_WIDTH;
    private int aniTick = 0,aniIndex = 0;

    public GameScreen(final JungleRush game,final MainMenuScreen menu)
    {
        this.game = game;
        this.menu = menu;
        tRoad = new Texture("Road.jpeg");

        initializeBackground();


    }

    private void initializeBackground() {
        initializeVariables();
        //load all available trees
        loadTrees();
        loadBackground();
        loadAnimals();
        loadCars();
        initializeRectangles();


    }

    private void loadCars() {
        tMainCharacter = new Texture("mainCar.png");
    }

    private void loadAnimals() {
        animalsCrab = new Array<>();
        for(int i=1;i<=3;i++)
            animalsCrab.add(new Texture("Enemy/Living/Animal"+i+".png"));
    }


    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.3f, 1);

        menu.blinkingEffect();

        game.batch.begin();
        drawBackground();
        drawTrees();
        drawMainCharacter();
        drawAnimals();
        game.batch.end();

        update();
    }

    private void drawMainCharacter() {
        game.batch.draw(tMainCharacter,mcRect.x,mcRect.y,mcRect.width,mcRect.height);

        drawScore();

    }

    private void drawScore() {
        GlyphLayout g1 = new GlyphLayout(game.font,Integer.toString(score));
        float textX = mcRect.x+ ( mcRect.width- g1.width)/2f;
        float textY = mcRect.y + (mcRect.height+g1.height)/2f;
        game.font.draw(game.batch,g1,textX,textY);
    }

    private void drawAnimals() {
        game.batch.draw(animalsCrab.get(this.aniIndex),animalsRect.get(0).x,animalsRect.get(0).y);
    }

    private void update()
    {
        //simulate road movement
        updateRoad();
        //simulate random tree spawn and tree movement
        updateTrees();
        //simulate background image movement
        updateBackground();

        animateTrees(animalsCrab);


    }




    //all draw methods implementation
    private void drawTrees() {
        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectLeft.get(i).x,this.treeRectLeft.get(i).y,this.treeRectLeft.get(i).width,this.treeRectLeft.get(i).height);
        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectRight.get(i).x,this.treeRectRight.get(i).y,this.treeRectRight.get(i).width,this.treeRectRight.get(i).height);
    }

    private void drawBackground() {
        for(int i = 0; i < 2; i++)
            game.batch.draw(backgrounds.get(0), this.backgroundRect.get(i).x,this.backgroundRect.get(i).y,this.backgroundRect.get(i).width,this.backgroundRect.get(i).height);

        game.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        for(int i = 2; i < this.backgroundRect.size; i++)
            game.batch.draw(backgrounds.get(1), this.backgroundRect.get(i).x,this.backgroundRect.get(i).y,this.backgroundRect.get(i).width,this.backgroundRect.get(i).height);

        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        //draw river
        game.batch.draw(tRiver,riverRect.x,riverRect.y,riverRect.width,riverRect.height);

        //draw boarder
        for(Rectangle rect:roadBoarder)
            game.batch.draw(tRiver,rect.x,rect.y,rect.width,rect.height);

        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //draw road
        for(Rectangle rect:roadRect)
            game.batch.draw(tRoad,rect.x,rect.y,rect.width,rect.height);


    }



    //all update methods implementation
    private void updateRoad() {
        for(int i=0; i < this.roadRect.size;i++)
        {
            this.roadRect.get(i).y -= this.SPEED;
            if(this.roadRect.get(i).y <= -game.SCREEN_HEIGHT) this.roadRect.get(i).y = game.SCREEN_HEIGHT;
        }
    }

    private void updateBackground() {
        for(int i=0;i < this.backgroundRect.size/2;i++)
        {
            this.backgroundRect.get(i).y--;
            if(this.backgroundRect.get(i).y <= -game.SCREEN_HEIGHT) this.backgroundRect.get(i).y = game.SCREEN_HEIGHT;
        }
        for(int i=this.backgroundRect.size/2;i < this.backgroundRect.size;i++)
        {
            this.backgroundRect.get(i).x++;
            if(this.backgroundRect.get(i).x >= game.SCREEN_WIDTH) this.backgroundRect.get(i).x = -game.SCREEN_WIDTH;
        }
    }

    private void updateTrees() {
        for (int i = 0; i < this.treeRectLeft.size; i++)
        {
            this.treeRectLeft.get(i).y -= this.SPEED;
            this.treeRectRight.get(i).y -= this.SPEED;
            if(this.treeRectLeft.get(i).y <= -this.treeRectLeft.get(i).getHeight())
            {
                this.treeRectLeft.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectLeft.get(i).x = MathUtils.random(0, this.JUNGLE_WIDTH -trees.get(i).getWidth());
            }
            if(this.treeRectRight.get(i).y <= -this.treeRectRight.get(i).getHeight())
            {
                this.treeRectRight.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectRight.get(i).x = this.JUNGLE_WIDTH + this.ROAD_WIDTH + 2*this.BOARDER_WIDTH+ MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth());
            }
        }
    }

    private void animateTrees(Array<Texture> component) {
        this.aniTick++;
        if(this.aniTick >= aniSpeed)
        {
            this.aniTick = 0;
            this.aniIndex++;
            if(this.aniIndex >= component.size)
                this.aniIndex = 0;
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
        for(Texture t : trees)
            t.dispose();
    }








    //initialized methods
    private void initializeVariables() {
        this.ELEMENT_WIDTH = game.SCREEN_WIDTH/this.TOTAL_TILES;
        this.ELEMENT_HEIGHT = game.SCREEN_HEIGHT/this.TOTAL_TILES;
        this.JUNGLE_FACTOR = 6;
        this.ROAD_FACTOR = this.JUNGLE_FACTOR;
        this.RIVER_FACTOR = 1;
        this.BOARDER_WIDTH = (int)(0.5 * this.ELEMENT_WIDTH);
        this.JUNGLE_WIDTH = this.JUNGLE_FACTOR * this.ELEMENT_WIDTH;
        this.ROAD_WIDTH = this.JUNGLE_WIDTH;
    }

    private void initializeRectangles() {
        //road control rectangle
        roadRect = new Array<>();
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, 0, this.ROAD_WIDTH, game.SCREEN_HEIGHT));
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, game.SCREEN_HEIGHT, this.ROAD_WIDTH, game.SCREEN_HEIGHT));

        //tree control rectangle initialization
        treeRectLeft = new Array<>();
        treeRectRight = new Array<>();
        for (int i = 0; i < trees.size; i++)
        {
            treeRectLeft.add(new Rectangle(MathUtils.random(0, this.JUNGLE_WIDTH -trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
            treeRectRight.add(new Rectangle(this.JUNGLE_WIDTH + this.ROAD_WIDTH + 2*this.BOARDER_WIDTH+ MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
        }

        //background control rectangle initialization
        backgroundRect = new Array<>();
        backgroundRect.add(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(0,game.SCREEN_HEIGHT,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(-game.SCREEN_WIDTH,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));

        //river control rectangle
        riverRect = new Rectangle(this.JUNGLE_WIDTH*2+this.ROAD_WIDTH+this.BOARDER_WIDTH*2,0,this.ELEMENT_WIDTH*this.RIVER_FACTOR,game.SCREEN_HEIGHT);

        //road boarder control rectangle
        roadBoarder = new Array<>();
        roadBoarder.add(new Rectangle(this.JUNGLE_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));
        roadBoarder.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+this.ROAD_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));

        //animal control rectangle
        animalsRect = new Array<>();
        animalsRect.add(new Rectangle(this.JUNGLE_WIDTH+MathUtils.random(0,this.ROAD_WIDTH-this.animalsCrab.get(0).getWidth()),
                MathUtils.random(6*this.ELEMENT_HEIGHT,9*ELEMENT_HEIGHT),this.animalsCrab.get(0).getWidth(),this.animalsCrab.get(0).getHeight()));

        //main character control rectangle
        mcRect = new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+MathUtils.random(0,this.ROAD_WIDTH-this.tMainCharacter.getWidth()),
                0,this.tMainCharacter.getWidth(),tMainCharacter.getHeight());
    }

    private void loadTrees() {
        trees = new Array<>();

        trees.add(new Texture("Trees/Luminous_tree1.png"));
        trees.add(new Texture("Trees/Luminous_tree2.png"));
        trees.add(new Texture("Trees/Luminous_tree3.png"));
        trees.add(new Texture("Trees/Luminous_tree4.png"));

        trees.add(new Texture("Trees/Mega_tree1.png"));
        trees.add(new Texture("Trees/Mega_tree2.png"));

        trees.add(new Texture("Trees/Swirling_tree1.png"));
        trees.add(new Texture("Trees/Swirling_tree2.png"));
        trees.add(new Texture("Trees/Swirling_tree3.png"));

        trees.add(new Texture("Trees/Willow1.png"));
        trees.add(new Texture("Trees/Willow2.png"));
        trees.add(new Texture("Trees/Willow3.png"));

        trees.add(new Texture("Trees/Curved_tree1.png"));
        trees.add(new Texture("Trees/Curved_tree2.png"));
        trees.add(new Texture("Trees/Curved_tree3.png"));

        trees.add(new Texture("Trees/Blue-green_balls_tree1.png"));
        trees.add(new Texture("Trees/Blue-green_balls_tree2.png"));
        trees.add(new Texture("Trees/Blue-green_balls_tree3.png"));

        trees.add(new Texture("Trees/Beige_green_mushroom1.png"));
        trees.add(new Texture("Trees/Beige_green_mushroom2.png"));
        trees.add(new Texture("Trees/Beige_green_mushroom3.png"));

        trees.add(new Texture("Trees/Chanterelles1.png"));
        trees.add(new Texture("Trees/Chanterelles2.png"));
        trees.add(new Texture("Trees/Chanterelles3.png"));

        trees.add(new Texture("Trees/Light_balls_tree1.png"));
        trees.add(new Texture("Trees/Light_balls_tree2.png"));
        trees.add(new Texture("Trees/Light_balls_tree3.png"));

        trees.add(new Texture("Trees/White_tree1.png"));
        trees.add(new Texture("Trees/White_tree2.png"));

        trees.add(new Texture("Trees/White-red_mushroom1.png"));
        trees.add(new Texture("Trees/White-red_mushroom2.png"));
        trees.add(new Texture("Trees/White-red_mushroom3.png"));

        //bushes
        trees.add(new Texture("Trees/Fern1_1.png"));
        trees.add(new Texture("Trees/Fern1_2.png"));
        trees.add(new Texture("Trees/Fern1_3.png"));

        trees.add(new Texture("Trees/Fern2_1.png"));
        trees.add(new Texture("Trees/Fern2_2.png"));
        trees.add(new Texture("Trees/Fern2_3.png"));

        trees.add(new Texture("Trees/Snow_bush1.png"));
        trees.add(new Texture("Trees/Snow_bush2.png"));
        trees.add(new Texture("Trees/Snow_bush3.png"));

        trees.add(new Texture("Trees/Bush_red_flowers1.png"));
        trees.add(new Texture("Trees/Bush_red_flowers2.png"));
        trees.add(new Texture("Trees/Bush_red_flowers3.png"));

        trees.add(new Texture("Trees/Bush_simple1_1.png"));
        trees.add(new Texture("Trees/Bush_simple1_2.png"));
        trees.add(new Texture("Trees/Bush_simple1_3.png"));

        trees.add(new Texture("Trees/Bush_simple2_1.png"));
        trees.add(new Texture("Trees/Bush_simple2_2.png"));
        trees.add(new Texture("Trees/Bush_simple2_3.png"));

        trees.add(new Texture("Trees/Bush_blue_flowers1.png"));
        trees.add(new Texture("Trees/Bush_blue_flowers2.png"));
        trees.add(new Texture("Trees/Bush_blue_flowers3.png"));

        trees.add(new Texture("Trees/Bush_orange_flowers1.png"));
        trees.add(new Texture("Trees/Bush_orange_flowers2.png"));
        trees.add(new Texture("Trees/Bush_orange_flowers3.png"));

        trees.add(new Texture("Trees/Bush_pink_flowers1.png"));
        trees.add(new Texture("Trees/Bush_pink_flowers2.png"));
        trees.add(new Texture("Trees/Bush_pink_flowers3.png"));

        trees.add(new Texture("Trees/Cactus1_1.png"));
        trees.add(new Texture("Trees/Cactus1_2.png"));
        trees.add(new Texture("Trees/Cactus1_3.png"));

        trees.add(new Texture("Trees/Cactus2_1.png"));
        trees.add(new Texture("Trees/Cactus2_2.png"));
        trees.add(new Texture("Trees/Cactus2_3.png"));

        trees.add(new Texture("Trees/Palm_tree1_1.png"));
        trees.add(new Texture("Trees/Palm_tree1_2.png"));
        trees.add(new Texture("Trees/Palm_tree1_3.png"));

        trees.add(new Texture("Trees/Palm_tree2_1.png"));
        trees.add(new Texture("Trees/Palm_tree2_2.png"));
        trees.add(new Texture("Trees/Palm_tree2_3.png"));

        trees.add(new Texture("Trees/Snow_christmass_tree1.png"));
        trees.add(new Texture("Trees/Snow_christmass_tree2.png"));
        trees.add(new Texture("Trees/Snow_christmass_tree3.png"));

        trees.add(new Texture("Trees/Snow_tree1.png"));
        trees.add(new Texture("Trees/Snow_tree2.png"));
        trees.add(new Texture("Trees/Snow_tree3.png"));

        trees.add(new Texture("Trees/Tree1.png"));
        trees.add(new Texture("Trees/Tree2.png"));
        trees.add(new Texture("Trees/Tree3.png"));

        trees.add(new Texture("Trees/Moss_tree1.png"));
        trees.add(new Texture("Trees/Moss_tree2.png"));
        trees.add(new Texture("Trees/Moss_tree3.png"));

        trees.add(new Texture("Trees/Fruit_tree1.png"));
        trees.add(new Texture("Trees/Fruit_tree2.png"));
        trees.add(new Texture("Trees/Fruit_tree3.png"));

        trees.add(new Texture("Trees/Autumn_tree1.png"));
        trees.add(new Texture("Trees/Autumn_tree2.png"));
        trees.add(new Texture("Trees/Autumn_tree3.png"));

        trees.add(new Texture("Trees/Christmas_tree1.png"));
        trees.add(new Texture("Trees/Christmas_tree2.png"));
        trees.add(new Texture("Trees/Christmas_tree3.png"));

        trees.add(new Texture("Trees/Flower_tree1.png"));
        trees.add(new Texture("Trees/Flower_tree2.png"));
        trees.add(new Texture("Trees/Flower_tree3.png"));

        trees.add(new Texture("Trees/Blue-gray_ruins1.png"));
        trees.add(new Texture("Trees/Blue-gray_ruins2.png"));
        trees.add(new Texture("Trees/Blue-gray_ruins4.png"));

        trees.add(new Texture("Trees/Yellow_ruins1.png"));
        trees.add(new Texture("Trees/Yellow_ruins2.png"));
        trees.add(new Texture("Trees/Yellow_ruins3.png"));
        trees.add(new Texture("Trees/Yellow_ruins4.png"));
        trees.add(new Texture("Trees/Yellow_ruins5.png"));

        trees.add(new Texture("Trees/Blue_coral1_shadow1.png"));
        trees.add(new Texture("Trees/Blue_coral1_shadow2.png"));
        trees.add(new Texture("Trees/Blue_coral1_shadow3.png"));

        trees.add(new Texture("Trees/Blue_coral2_shadow1.png"));
        trees.add(new Texture("Trees/Blue_coral2_shadow2.png"));
        trees.add(new Texture("Trees/Blue_coral2_shadow3.png"));

        trees.add(new Texture("Trees/Blue-beige_coral1_shadow1.png"));
        trees.add(new Texture("Trees/Blue-beige_coral1_shadow2.png"));
        trees.add(new Texture("Trees/Blue-beige_coral1_shadow3.png"));

        trees.add(new Texture("Trees/Blue-beige_coral2_shadow1.png"));
        trees.add(new Texture("Trees/Blue-beige_coral2_shadow2.png"));
        trees.add(new Texture("Trees/Blue-beige_coral2_shadow3.png"));













    }
    private void loadBackground() {
        backgrounds = new Array<>();
        for(int i=0;i < 2;i++)
            backgrounds.add(new Texture("Background/water.bmp"));

        tRiver = new Texture("Background/River.bmp");
    }


}
