package net.hectorh30.games.writing.launcher;

import javax.microedition.khronos.opengles.GL10;

import net.hectorh30.games.writing.Utils;
import net.hectorh30.games.writing.WriteActivity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class WritingGameLauncher extends BaseGameActivity implements IOnMenuItemClickListener {
	
	// ===========================================================
	// Constants
	// ===========================================================

	private final int CAMERA_WIDTH  = Utils.CAMERA_WIDTH;
	private final int CAMERA_HEIGHT = Utils.CAMERA_HEIGHT;

	
	private static final int FONT_SIZE = 80;
	private static final int FONT_COLOR = Color.WHITE;
	
	private final int MENU_PLAY = 0;
	private final int MENU_QUIT = 1;
	private final int MENU_QUIT_YES = 2;
	private final int MENU_QUIT_NO = 3;

	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	
	protected Scene mainScene;
	protected Handler mHandler;
	
	protected MenuScene mainMenuScene;
	protected MenuScene subMenuScene;
	private boolean isExitScene = false;
	
	//**************************************************************************
	// Font variables
	public Font mMenuCrayonFont;
	protected Font mMenuBlankFont;
	protected Font mTitleCrayonFont;
	protected Font mQuitMessageFont;
	//**************************************************************************
	// Quit text
	protected Text mQuitText;
	//**************************************************************************
	
	//protected TextureRegion mMenuPlayTextureRegion;
	//protected TextureRegion mMenuReviewTextureRegion;
	//protected TextureRegion mMenuBlankTextureRegion;
	
	//**************************************************************************
    // parallax background
    private TextureRegion mParallaxLayerBack;
    private TextureRegion mParallaxLayerFront;
    //**************************************************************************
    
    //**************************************************************************
    // to save the menu item image
    public TextureRegion menuItemTextureRegion;
    //**************************************************************************
    //TextToSpeechSpanish tts;
    
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		mHandler = new Handler();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		
		//**********************************************************************
        // Load the TextToSpeech speaker
        //**********************************************************************
        //this.tts = new TextToSpeechSpanish(this);
        
		
		//**********************************************************************
        // Load the background images
        //**********************************************************************
		BitmapTextureAtlas mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 2048,
                TextureOptions.DEFAULT);
        BitmapTextureAtlas mAutoParallaxBackgroundTexture2 = new BitmapTextureAtlas(2048, 512,
                TextureOptions.DEFAULT);
        
        //**********************************************************************
        // Parallax background
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(Utils.GRAPHICS_PATH);
        
        this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                mAutoParallaxBackgroundTexture, this, "MainMenu/background-portrait.png", 0, 0);
        
        this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                mAutoParallaxBackgroundTexture2, this, "MainMenu/clouds.png", 0, 0);
        
        this.mEngine.getTextureManager().loadTextures(mAutoParallaxBackgroundTexture,mAutoParallaxBackgroundTexture2);
	    
        //**********************************************************************
        // MainMenu item image
        //**********************************************************************
        BitmapTextureAtlas mAutoParallaxBackgroundTexture3 = new BitmapTextureAtlas(512, 256,
                TextureOptions.DEFAULT);
        this.menuItemTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                mAutoParallaxBackgroundTexture3, this, "MainMenu/menu_item.png", 0, 0);
        this.mEngine.getTextureManager().loadTexture(mAutoParallaxBackgroundTexture3);
        
		
        //**********************************************************************
		// Load Font Textures
        //**********************************************************************
        BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMenuCrayonFont = FontFactory.createFromAsset(mFontTexture, this, "font/Crayon.ttf", FONT_SIZE, true, FONT_COLOR);
        
        BitmapTextureAtlas mFontTexture1 = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTitleCrayonFont = FontFactory.createFromAsset(mFontTexture1, this, "font/Crayon.ttf", 120, true, Color.BLACK);
        
        BitmapTextureAtlas mFontTexture11 = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mQuitMessageFont = FontFactory.createFromAsset(mFontTexture11, this, "font/Crayon.ttf", 50, true, Color.BLACK);
        
        // Load blank font
        BitmapTextureAtlas mFontTexture2 = new BitmapTextureAtlas(32, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMenuBlankFont = new Font(mFontTexture1,Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 50, true, Color.BLACK);
        
        // MenuActivity.mCrayonFont = new Font(mDroidFontTexture,Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, FONT_COLOR);
        this.mEngine.getTextureManager().loadTextures(mFontTexture, mFontTexture1, mFontTexture11, mFontTexture2);
        this.getFontManager().loadFonts(this.mMenuCrayonFont, mMenuBlankFont, mTitleCrayonFont, this.mQuitMessageFont);
        
        
        
    }

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		// parse the XML
//		this.parseXML();
		
		// DataBase
//		WritingGameLauncher.datasource = new DataSource(this);
//        MainObjects.datasource.open();
		
		// create the menu scene
		this.mainMenuScene = this.createMainMenuScene();
		
		// create the menu scene
        this.subMenuScene = this.createSubMenuQuitScene();
		
		// create the main scene
		this.mainScene = new Scene();
		
		//**********************************************************************
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 7);
		// add parallax background - back
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, 
                new Sprite(0, 0, this.mParallaxLayerBack)));
        
        // add parallax background - clouds
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-7.0f, 
                new Sprite(0, 50, this.mParallaxLayerFront)));
		
        // set the main scene background
        this.mainScene.setBackground(autoParallaxBackground);
        //**********************************************************************
        
        Text mText = new Text(0, 0, this.mTitleCrayonFont, "Escribe letras");
        mText.setPosition((CAMERA_WIDTH-mText.getWidth())/2, 50);
        
        this.mainScene.attachChild(mText);
        
        // attach the menu scene to the main scene 
		this.mainScene.setChildScene(mainMenuScene);
		
		return this.mainScene;
	}

//	private void parseXML() {
//	    
//	    Parser parser = new Parser();
//	    parser.parse(this, "xml/data.xml");
//        MainObjects.setHash_categories(parser.getHash_categories());
//        MainObjects.setHash_games(parser.getHash_games());
//        MainObjects.setHash_stageTypes(parser.getHash_stageTypes());
//        MainObjects.setWorlds(parser.getWorlds());
//        MainObjects.setHash_worlds(parser.getHash_worlds());
//        MainObjects.setCategories(parser.getCategories());
//        
//    }

    @Override
	public void onLoadComplete() {}

	@Override
	public void onPauseGame() {
		super.onPauseGame();
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){			
			if(this.isExitScene){
				this.isExitScene = false;
			    this.subMenuScene.back();
				return true;
			} else {
				this.mQuitText.setPosition((CAMERA_WIDTH - this.mQuitText.getWidth())/2, 230);
				this.isExitScene = true;
				mainMenuScene.setChildSceneModal(this.subMenuScene);
			    return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onMenuItemClicked(
			final MenuScene pMenuScene, 
			final IMenuItem pMenuItem, 
			final float pMenuItemLocalX, 
			final float pMenuItemLocalY) {
		
		switch(pMenuItem.getID()) {
			case MENU_PLAY: //Play
			    //mMainScene.registerEntityModifier(new ScaleAtModifier(0.5f, 1.0f, 0.0f, CAMERA_WIDTH/2, CAMERA_HEIGHT/2));
				//mStaticMenuScene.registerEntityModifier(new ScaleAtModifier(0.5f, 1.0f, 0.0f, CAMERA_WIDTH/2, CAMERA_HEIGHT/2));
				//mHandler.postDelayed(mLaunchLevel1Task,500);
				mHandler.post(launchWritingActivity);
				return true;

			//Review
//			case 1: 
				//mMainScene.registerEntityModifier(new ScaleAtModifier(0.5f, 1.0f, 0.0f, CAMERA_WIDTH/2, CAMERA_HEIGHT/2));
			    //mStaticMenuScene.registerEntityModifier(new ScaleAtModifier(0.5f, 1.0f, 0.0f, CAMERA_WIDTH/2, CAMERA_HEIGHT/2));
				//mHandler.postDelayed(mLaunchScoresTask, 500);
//				mHandler.post(mLaunchReviewTask);
//				return true;
				
			case MENU_QUIT:
			    mQuitText.setPosition((CAMERA_WIDTH-mQuitText.getWidth())/2, 230);
			    this.isExitScene = true;
			    pMenuScene.setChildSceneModal(this.subMenuScene);
                return true;
                
			case MENU_QUIT_YES:
                this.finishActivity();
                return true;
                
			case MENU_QUIT_NO:
				this.isExitScene = false;
			    this.subMenuScene.back();
                return true;
                
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	protected MenuScene createMainMenuScene() {
		MenuScene menuScene = new MenuScene(this.mCamera);
		
		// menu PLAY
		final IMenuItem playMenuItem = new ColorMenuItemDecorator(
				new TextSpriteMenuItem(this.MENU_PLAY,"Jugar",this.menuItemTextureRegion, this.mMenuCrayonFont),
				0.5f, 0.5f, 0.5f, // selected color 
				0.15f, 0.49f, 0.96f); // unselected color
		playMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(playMenuItem);
		
		//menu blank item for spacing purposes
		final IMenuItem tempMenuItem = new TextMenuItem(-1, this.mMenuBlankFont, "");
  		tempMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
  		menuScene.addMenuItem(tempMenuItem);
		
		// menu Quit
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator( 
				new TextSpriteMenuItem(this.MENU_QUIT,"Salir",this.menuItemTextureRegion,this.mMenuCrayonFont), 
				0.5f, 0.5f, 0.5f, // selected color 
				1.0f, 0.0f, 0.0f); // unselected color
        quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);
		
        // build animations, disable the background and set on the click listener
        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
		
		return menuScene;
	}
	
	protected MenuScene createSubMenuQuitScene() {
        MenuScene menuScene = new MenuScene(this.mCamera);
        
        //menu Sí
        final IMenuItem quitYesMenuItem = new ColorMenuItemDecorator( 
        		new TextSpriteMenuItem(this.MENU_QUIT_YES,"S"+((char)237),this.menuItemTextureRegion,this.mMenuCrayonFont), 
        		0.5f, 0.5f, 0.5f, 
        		0.0f, 1.0f, 0.0f);
        quitYesMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitYesMenuItem);
        
        //menu blank item for spacing purposes
  		final IMenuItem tempMenuItem = new TextMenuItem(-1, this.mMenuBlankFont, "");
  		tempMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
  		menuScene.addMenuItem(tempMenuItem);
        
        //menu No
        final IMenuItem reviewMenuItem = new ColorMenuItemDecorator( new TextSpriteMenuItem(this.MENU_QUIT_NO,"No",this.menuItemTextureRegion,this.mMenuCrayonFont), 
        		0.5f, 0.5f, 0.5f, 
        		1.0f, 0.0f, 0.0f);
        reviewMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(reviewMenuItem);
        
        
        // build animations, disable the background and set on the click listener
        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
        
        
        this.mQuitText = new Text(0, 0, this.mQuitMessageFont, ((char)191)+"Seguro que deseas salir?");
        this.mQuitText.setPosition((CAMERA_WIDTH-mQuitText.getWidth())/2, 230);
        
        menuScene.attachChild(mQuitText);
        return menuScene;
    }
	
	
	private Runnable launchWritingActivity = new Runnable() {
	    @Override
        public void run() {
            Bundle bundle = new Bundle();
//            World w1 = MainObjects.getWorlds().get(0);
//            Stage st1 = w1.getStages().get(1);
//            Word word1 = st1.getVocabulary().get(0);
//            
//            word1.getValue();
            
	    	// add the world id, stage id, and the world to send 
//            bundle.putString("world", w1.getId());
//            bundle.putString("stage", st1.getId());
//            bundle.putString("word", word1.getValue());
//            bundle.putBoolean("evaluation", false);
            
//            Intent myIntent = new Intent(WritingGameLauncher.this, ZooActivity.class);
            Intent myIntent = new Intent(WritingGameLauncher.this, WriteActivity.class);
//            myIntent.putExtras(bundle);
            WritingGameLauncher.this.startActivity(myIntent);
	    }
	};

//    private Runnable mLaunchReviewTask = new Runnable() {
//        @Override
//        public void run() {
//            
//        	
//            Bundle bundle = new Bundle();
//            Category cat = Utils.getCategories().get(0);
//            bundle.putString("category",cat.getId());
//            
//            Intent myIntent = new Intent(WritingGameLauncher.this, ReviewCategoryActivity.class);
//            myIntent.putExtras(bundle);
//            WritingGameLauncher.this.startActivity(myIntent);
//            
//            
//            World w1 = MainObjects.getWorlds().get(0);
//            Stage st1 = w1.getStages().get(0);
//            Word word1 = st1.getVocabulary().get(1);
//            bundle.putString("word1", st1.getVocabulary().get(0).getValue());
//            bundle.putString("word2", st1.getVocabulary().get(1).getValue());
//            bundle.putString("word3", st1.getVocabulary().get(2).getValue());
//            // add the world id, stage id, and the world to send 
//            bundle.putString("world", w1.getId());
//            Intent myIntent = new Intent(MenuActivity.this, GameMatchActivity.class);
//            bundle.putString("word", word1.getValue());
//            bundle.putBoolean("evaluation", false);
//            
//            Intent myIntent = new Intent(MenuActivity.this, ReviewCategoryActivity.class);
//            myIntent.putExtras(bundle);
//            MenuActivity.this.startActivity(myIntent);
//            
//        }
//    };
    
    
    public void setTransitions(){
    	//this.overridePendingTransition(R.anim.incoming, R.anim.outgoing);
    }
    
    public void finishActivity(){
        //this.tts.shutdown();
        this.finish();
        this.setTransitions();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}