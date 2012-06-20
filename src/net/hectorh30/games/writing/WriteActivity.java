package net.hectorh30.games.writing;

import java.util.ArrayList;

import net.hectorh30.games.writing.sprites.BackTextSprite;
import net.hectorh30.games.writing.sprites.PencilSprite;
import net.hectorh30.games.writing.sprites.StepSprite;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.IModifier;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.qwerjk.andengine.entity.sprite.PixelPerfectSprite;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegion;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegionFactory;

public class WriteActivity extends BaseGameActivity {

	private final int CAMERA_WIDTH = Utils.CAMERA_WIDTH;
	private final int CAMERA_HEIGHT = Utils.CAMERA_HEIGHT;

	private Camera mCamera;
	private BitmapTextureAtlas 
		pencilTexture, 
		pointerTexture, 
//		letterTexture,
		mFontTexture,
		mFontTextureBack,
		greenStepTexture,
		redStepTexture,
		blueStepTexture,
		backButtonTexture;
	
	private ArrayList<BitmapTextureAtlas> letterTextures;
	
	private PixelPerfectTextureRegion 
//		letterTextureRegion, 
		pointerTextureRegion,
		greenStepTextureRegion,
		redStepTextureRegion, 
		blueStepTextureRegion;
	
	private ArrayList<PixelPerfectTextureRegion> letterTextureRegions;
//	private ArrayList<LetterPath> letterPaths;
	private ArrayList<ArrayList<LetterPath>> lettersLetterPaths;
	LetterPath currentPath;
	
	private StepsPool stepsPool;
	
	private TextureRegion 
		pencilTextureRegion,
		backButtonTextureRegion;

	private PencilSprite pencil;
	private Rectangle rectangle; 
	
	private Font mFont,mFontBack;
	private int FONT_SIZE = 60;
	private int currentLetterIndex = 0;
	
	private PixelPerfectSprite letter, pointer;
	final private String tag = "StartActivity";
	
	public Scene scene, drawingScene;
	public boolean allowPencilMove = true;
	
	private Entity stepsLayerEntity, letterLayer;
	private ArrayList<StepSprite> stepSprites;
	
	@Override
	public Engine onLoadEngine() {
		//mHandler = new Handler();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);		
		return new Engine(
			new EngineOptions(
					true, 
					ScreenOrientation.PORTRAIT, 
					new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
			this.mCamera));
	}

	@Override
	public void onLoadResources() {
		
		// Data from XML
//		this.letterTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.letterTextures = new ArrayList<BitmapTextureAtlas>();
		this.letterTextureRegions = new ArrayList<PixelPerfectTextureRegion>();
		
		BitmapTextureAtlas lTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		letterTextures.add(lTexture);
		PixelPerfectTextureRegion l = PixelPerfectTextureRegionFactory.createFromAsset(lTexture, this, "gfx/Letra-L-M-inverso.png", 0, 0);
		this.letterTextureRegions.add(l);
		
		BitmapTextureAtlas cTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		letterTextures.add(cTexture);
		PixelPerfectTextureRegion c = PixelPerfectTextureRegionFactory.createFromAsset(cTexture, this, "gfx/Letra-C-M-inverso.png", 0, 0);
		this.letterTextureRegions.add(c);
		
		BitmapTextureAtlas bTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		letterTextures.add(bTexture);
		PixelPerfectTextureRegion b = PixelPerfectTextureRegionFactory.createFromAsset(bTexture, this, "gfx/Letra-B-M-inverso.png", 0, 0);
		this.letterTextureRegions.add(b);	
		
		
		this.mFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(mFontTexture, this, "font/Crayon.ttf", FONT_SIZE, true, Color.WHITE);
		
		this.mFontTextureBack = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFontBack = FontFactory.createFromAsset(mFontTextureBack, this, "font/Crayon.ttf", 60, true, Color.WHITE);
		
		this.pointerTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.pointerTextureRegion = PixelPerfectTextureRegionFactory.createFromAsset(this.pointerTexture, this, "gfx/pointer-black.png", 0, 0);
		
		this.pencilTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.pencilTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.pencilTexture, this, "gfx/lapiz.png", 0, 0); 		
				
		this.greenStepTexture = new BitmapTextureAtlas(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.greenStepTextureRegion = PixelPerfectTextureRegionFactory.createFromAsset(this.greenStepTexture, this, "gfx/circulo-verde.png",0,0);
		
		this.redStepTexture = new BitmapTextureAtlas(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.redStepTextureRegion = PixelPerfectTextureRegionFactory.createFromAsset(this.redStepTexture, this, "gfx/circulo-rosado.png",0,0);
		
		this.blueStepTexture = new BitmapTextureAtlas(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.blueStepTextureRegion = PixelPerfectTextureRegionFactory.createFromAsset(this.blueStepTexture, this, "gfx/circulo-celeste.png",0,0);

		this.backButtonTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.backButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backButtonTexture, this, Utils.GRAPHICS_PATH+"Games/back-short.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTexture(this.pencilTexture);
		this.mEngine.getTextureManager().loadTexture(this.pointerTexture);
		
		for (BitmapTextureAtlas lt : letterTextures)
		{
			this.mEngine.getTextureManager().loadTexture(lt);
		}
		
		this.mEngine.getTextureManager().loadTexture(this.blueStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.redStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.greenStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getTextureManager().loadTexture(this.backButtonTexture);		
		
		this.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		scene = new Scene();
		scene.setBackground(new ColorBackground(0.2f, 0.545098039f, 0.862745098f));
		
		Sprite backButton = new BackTextSprite(Utils.ACTIVITY_MARGIN_TOP,Utils.ACTIVITY_MARGIN_LEFT,this.backButtonTextureRegion,this.mFontBack,""){
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                finishActivity();
                return true;
            }
        };
		
		drawingScene = new Scene();
		drawingScene.setBackgroundEnabled(false);
		
		letterLayer = new Entity();
		letter = new PixelPerfectSprite(
				(CAMERA_WIDTH - this.letterTextureRegions.get(this.currentLetterIndex).getWidth()) / 2,
				(CAMERA_HEIGHT - this.letterTextureRegions.get(this.currentLetterIndex).getHeight())/2,
				this.letterTextureRegions.get(this.currentLetterIndex));
		
		letter.setColor(0.2f, 0.545098039f, 0.862745098f);
		letterLayer.attachChild(letter);
				
		rectangle = new Rectangle(letter.getX(),letter.getY(),letter.getWidth(),letter.getHeight());
		rectangle.setColor(0f,0.37254902f,0.717647059f);
		stepsPool = new StepsPool(letter,this.mFont,redStepTextureRegion,blueStepTextureRegion,greenStepTextureRegion);
		
		// Data from XML
		
		lettersLetterPaths = new ArrayList<ArrayList<LetterPath>>();
		
//		Letra-L-M-inverso.png paths
		lettersLetterPaths.add(new ArrayList<LetterPath>());
		LetterPath path1 = new LetterPath(165f,-42f);
		path1.addStep(102f,114f);
		path1.addStep(102f,336f);
		path1.addStep(102f,530f);
		path1.addStep(268f,530f);
		path1.addStep(420f,530f);
		lettersLetterPaths.get(0).add(path1);		
		
//		Letra-C-M-inverso.png paths
		lettersLetterPaths.add(new ArrayList<LetterPath>());
		path1 = new LetterPath(478f,-15f);
		path1.addStep(245f,0f);
		path1.addStep(97f,102f);
		path1.addStep(60f,290f);
		path1.addStep(129f,483f);
		path1.addStep(336f,535f);
		lettersLetterPaths.get(1).add(path1);
		
//		Letra-B-M-inverso.png paths
		lettersLetterPaths.add(new ArrayList<LetterPath>());
		path1 = new LetterPath(132f,-36f);
		path1.addStep(62f, 140f);
		path1.addStep(62f, 327f);
		path1.addStep(62f, 510f);
		lettersLetterPaths.get(2).add(path1);
		
		path1 = new LetterPath(140f,-40f);
		path1.addStep(180f, 10f);
		path1.addStep(335f, 34f);
		path1.addStep(375f, 175f);
		path1.addStep(238f, 256f);
		path1.addStep(83f, 259f);
		lettersLetterPaths.get(2).add(path1);
		
		path1 = new LetterPath(132f,196f);
		path1.addStep(238f, 256f);
		path1.addStep(389f, 325f);
		path1.addStep(397f, 483f);
		path1.addStep(250f, 532f);
		path1.addStep(84f, 532f);
		lettersLetterPaths.get(2).add(path1);
		

		currentPath = lettersLetterPaths.get(this.currentLetterIndex).get(0);
		pointer = new PixelPerfectSprite(0f, 0f, this.pointerTextureRegion);
		pencil = new PencilSprite(letter, currentPath.getInitialRelX(), currentPath.getInitialRelY(), this.pencilTextureRegion,this,pointer);
		
		stepsLayerEntity = new Entity();
		stepSprites = new ArrayList<StepSprite>();

		// Logic for letter steps (paths)
		showPath(currentPath);
		
		// Scene attaching
		scene.attachChild(pointer);
		scene.attachChild(rectangle);
		scene.attachChild(letterLayer);
		scene.attachChild(stepsLayerEntity);
		scene.attachChild(drawingScene);
		scene.attachChild(pencil);
        scene.attachChild(backButton);
        
        
        
        
		
		
		//Touch registering and collision logic
		scene.registerTouchArea(pencil);
		scene.registerTouchArea(backButton);
		scene.setTouchAreaBindingEnabled(true);
		
		
		scene.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void reset(){ }
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				if (pointer.collidesWith(letter) || !pointer.collidesWith(rectangle))
				{
					resetLetter();
				}
				
				for (StepSprite stepSprite : stepSprites)
				{
					if (stepSprite.collidesWith(pointer))
					{						
						if(stepSprite.getInitialRelX() == currentPath.getLastStepRelX() && stepSprite.getInitialRelY() == currentPath.getLastStepRelY())
						{
							stepSprite.animationHide();
							nextPath();
						} else {
							stepSprite.animationHide();
						}
					}
				}
			}
		});
		
		return scene;
	}

	@Override
	public void onLoadComplete() {
		
	}
	
	public void resetLetter(){		
		this.allowPencilMove  = false;
		this.pencil.moveStarted = false; 
		
		runOnUpdateThread(new Runnable() {
			@Override
	        public void run() {
				WriteActivity.this.drawingScene.detachChildren();		    	
		    }
		});
		
		this.pencil.setToInitialPosition();
		
		float currentX  = letter.getX();
    	LoopEntityModifier lem = 
			new LoopEntityModifier(
				new SequenceEntityModifier(
					new MoveXModifier(0.05f, currentX, currentX + 12),
					new MoveXModifier(0.05f, currentX + 12, currentX - 24),
					new MoveXModifier(0.05f, currentX - 24, currentX)
				),3,
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) { }

					@Override
					public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
						if (WriteActivity.this.currentPath == lettersLetterPaths.get(WriteActivity.this.currentLetterIndex).get(0))
						{
							showStepSprites();
							WriteActivity.this.allowPencilMove = true;
						} else {
							for (StepSprite stepSprite : stepSprites)
							{
								stepSprite.visible = false;
								stepSprite.setAlpha(0f);
								stepsPool.recyclePoolItem(stepSprite);
							}
							WriteActivity.this.currentPath = lettersLetterPaths.get(WriteActivity.this.currentLetterIndex).get(0);
							showPath(WriteActivity.this.currentPath);
							WriteActivity.this.allowPencilMove = true;
						}
					}
				}
			);
    	lem.setRemoveWhenFinished(true);
		letter.registerEntityModifier(lem);
    	
    	LoopEntityModifier lem2 =
			new LoopEntityModifier(
				new SequenceEntityModifier(
					new MoveXModifier(0.05f, currentX, currentX + 12),
					new MoveXModifier(0.05f, currentX + 12, currentX - 24),
					new MoveXModifier(0.05f, currentX - 24, currentX)
				),3
			);
    	rectangle.registerEntityModifier(lem2);
    }
	
	public void showStepSprites()
	{
		for (StepSprite stepSprite : stepSprites){
			stepSprite.animationShow();
		}
	}
	
	public void hideStepSprites()
	{
		for (StepSprite stepSprite : stepSprites){
			stepSprite.animationHide();
		}
	}
	
	public void showPath(LetterPath path)
	{
		stepSprites.clear();
		this.pencil.moveStarted = false;
		
		this.pencil.setInitialRelPosition(path.getInitialRelX(),path.getInitialRelY());
		this.pencil.setToInitialPosition();
		
		for (int i = 0; i < path.stepsX.size(); i++)
		{
			StepSprite step = stepsPool.obtainPoolItem();
			step.setInitialRelPosition(path.stepsX.get(i),path.stepsY.get(i));
			
			step.setNumber(""+(i+1));
			step.setShowDelay(0.3f * (i+1));
			step.setHideDelay(0.05f);
			stepSprites.add(step);
			
			if (stepsLayerEntity.getChildIndex(step) == -1)
				stepsLayerEntity.attachChild(step);
		}
		showStepSprites();
		
		this.allowPencilMove  = true;
	}
	
	public void nextPath()
	{
		for (StepSprite stepSprite : stepSprites){
			if (stepSprite.recyclable)
				stepsPool.recyclePoolItem(stepSprite);
		}
		if(lettersLetterPaths.get(this.currentLetterIndex).indexOf(currentPath) + 1 < lettersLetterPaths.get(this.currentLetterIndex).size())
		{
			currentPath = lettersLetterPaths.get(this.currentLetterIndex).get(lettersLetterPaths.get(this.currentLetterIndex).indexOf(currentPath) + 1);
			showPath(currentPath);
		} else {
			WriteActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(WriteActivity.this, "Oh yeah!", Toast.LENGTH_SHORT).show();
				}
			});
			nextLetter();
		}
	}

	public void nextLetter()
	{
		if(currentLetterIndex + 1 < lettersLetterPaths.size()){
			this.currentLetterIndex += 1;
			this.letter = new PixelPerfectSprite(
					(CAMERA_WIDTH - this.letterTextureRegions.get(this.currentLetterIndex).getWidth()) / 2,
					(CAMERA_HEIGHT - this.letterTextureRegions.get(this.currentLetterIndex).getHeight())/2,
					this.letterTextureRegions.get(this.currentLetterIndex));
			this.letter.setColor(0.2f, 0.545098039f, 0.862745098f);
			this.letterLayer.attachChild(this.letter);
			
			runOnUpdateThread(new Runnable() {
				@Override
		        public void run() {
					WriteActivity.this.letterLayer.detachChildren();
					WriteActivity.this.letterLayer.attachChild(WriteActivity.this.letter);
					WriteActivity.this.drawingScene.detachChildren();
			    }
			});
			
//			rectangle = new Rectangle(letter.getX(),letter.getY(),letter.getWidth(),letter.getHeight());
//			rectangle.setColor(0f,0.37254902f,0.717647059f);
			
			currentPath = lettersLetterPaths.get(this.currentLetterIndex).get(0);
			showPath(currentPath);
		} else {
			finishActivity();
		}
	}
	
	public void finishActivity(){
//        this.tts.shutdown();
        this.finish();
//        this.setTransitionsOut();
    }
}