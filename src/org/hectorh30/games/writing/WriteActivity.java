package org.hectorh30.games.writing;

import java.util.ArrayList;

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
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.IModifier;
import org.hectorh30.games.shapes.Ellipse;
import org.hectorh30.games.writing.sprites.PencilSprite;
import org.hectorh30.games.writing.sprites.StepSprite;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.qwerjk.andengine.entity.sprite.PixelPerfectSprite;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegion;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegionFactory;

public class WriteActivity extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 1280;

	private Camera mCamera;
	private BitmapTextureAtlas 
		pencilTexture, 
		pointerTexture, 
		letterTexture,
		mFontTexture,
		greenStepTexture,
		redStepTexture,
		blueStepTexture;
	
	private PixelPerfectTextureRegion 
		letterTextureRegion, 
		pointerTextureRegion,
		greenStepTextureRegion,
		redStepTextureRegion, 
		blueStepTextureRegion;
	
	private ArrayList<LetterPath> letterPaths;
	LetterPath currentPath;
	
	private StepsPool stepsPool;
	
	private TextureRegion 
		pencilTextureRegion;

	private PencilSprite pencil;
	private Rectangle rectangle; 
	
	private Font mFont;
	private int FONT_SIZE = 60;
	
	private PixelPerfectSprite letter, pointer;
	private String tag = "StartActivity";
	
	public Scene scene, drawingScene;
	public boolean allowPencilMove = true;
	
	private Entity stepsLayerEntity;
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
		this.letterTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.letterTextureRegion = PixelPerfectTextureRegionFactory.createFromAsset(this.letterTexture, this, "gfx/Letra-B-M-inverso.png", 0, 0);
		
		
		
		this.mFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(mFontTexture, this, "font/Crayon.ttf", FONT_SIZE, true, Color.WHITE);
		
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

		this.mEngine.getTextureManager().loadTexture(this.pencilTexture);
		this.mEngine.getTextureManager().loadTexture(this.pointerTexture);
		this.mEngine.getTextureManager().loadTexture(this.letterTexture);
		this.mEngine.getTextureManager().loadTexture(this.blueStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.redStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.greenStepTexture);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		
		this.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		letterPaths = new ArrayList<LetterPath>();
		
		scene = new Scene();
		scene.setBackground(new ColorBackground(0.2f, 0.545098039f, 0.862745098f));
		
		drawingScene = new Scene();
		drawingScene.setBackgroundEnabled(false);
		
		letter = new PixelPerfectSprite((CAMERA_WIDTH - this.letterTextureRegion.getWidth()) / 2,(CAMERA_HEIGHT - this.letterTextureRegion.getHeight())/2,this.letterTextureRegion);
		letter.setColor(0.2f, 0.545098039f, 0.862745098f);
		rectangle = new Rectangle(letter.getX(),letter.getY(),letter.getWidth(),letter.getHeight());
		rectangle.setColor(0f,0.37254902f,0.717647059f);
		
		stepsPool = new StepsPool(letter,this.mFont,redStepTextureRegion,blueStepTextureRegion,greenStepTextureRegion);
		
		// Data from XML
		final LetterPath path1 = new LetterPath(140f,-40f);
//		final LetterPath path1 = new LetterPath(340f,200f);
		path1.addStep(62f, 140f);
		path1.addStep(62f, 327f);
		path1.addStep(62f, 510f);
		letterPaths.add(path1);
		
		final LetterPath path2 = new LetterPath(140f,-40f);
		path2.addStep(215f, 8f);
		path2.addStep(360f, 52f);
		path2.addStep(370f, 185f);
		path2.addStep(245f, 255f);
		path2.addStep(100f, 255f);
		letterPaths.add(path2);
		
		final LetterPath path3 = new LetterPath(140f,-40f);
		path3.addStep(215f, 308f);
		path3.addStep(360f, 352f);
		path3.addStep(370f, 485f);
		path3.addStep(245f, 555f);
		path3.addStep(100f, 555f);
		letterPaths.add(path3);
		
		currentPath = path1;
		
		//pencil = new PencilSprite(letter, 140f, -40f, this.pencilTextureRegion,this);
		pointer = new PixelPerfectSprite(0f, 0f, this.pointerTextureRegion);
		pencil = new PencilSprite(letter, currentPath.getInitialRelX(), currentPath.getInitialRelY(), this.pencilTextureRegion,this,pointer);
//		pencil = new PencilSprite(letter, 140f, -40f, this.pencilTextureRegion,this,pointer);
		
		stepsLayerEntity = new Entity();
		stepSprites = new ArrayList<StepSprite>();

		// Logic for letter steps (paths)
//		drawPath(currentPath);
		
		// Scene attaching
//		scene.attachChild(pointer);
		scene.attachChild(rectangle);
		scene.attachChild(letter);
		scene.attachChild(stepsLayerEntity);
		scene.attachChild(drawingScene);
		scene.attachChild(pencil);
		scene.attachChild(pointer);
		
		/*
		for(int i = (int)letter.getX(); i < CAMERA_WIDTH; i+=50)
		{
			Line linea = new Line(i,0,i,CAMERA_HEIGHT,2f);
			scene.attachChild(linea);
		}
		
		for(int i = (int)letter.getY(); i < CAMERA_HEIGHT; i+=50)
		{
			Line linea = new Line(0,i,CAMERA_WIDTH,i,2f);
			scene.attachChild(linea);
		}
		*/
		
		//Touch registering and collision logic
		scene.registerTouchArea(pencil);
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
						stepSprite.animationHide();
						if(stepSprite.getInitialRelX() == currentPath.getLastStepRelX() && stepSprite.getInitialRelY() == currentPath.getLastStepRelY())
						{
//							Log.d(tag,"Is last step");
							//drawPath(path2);
							resetLetter();
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
		
		runOnUpdateThread(new Runnable() {
			@Override
	        public void run() {
				WriteActivity.this.drawingScene.detachChildren();		    	
		    }
		});
		
		this.pencil.setToInitialPosition();
		
		//pointer.setPosition(pointer.getInitialX(),pointer.getInitialY());
		
		float currentX  = letter.getX();        
    	LoopEntityModifier lem = 
			new LoopEntityModifier(
				new SequenceEntityModifier(
					new IEntityModifierListener() {
						@Override
						public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) { }
	
						@Override
						public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
							showStepSprites();
							//drawPath(letterPaths.get(0));
							//LetterActivity.this.allowPencilMove = true;
						}
					},
					new MoveXModifier(0.05f, currentX, currentX + 12),
					new MoveXModifier(0.05f, currentX + 12, currentX - 24),
					new MoveXModifier(0.05f, currentX - 24, currentX)
				),3
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
	
	public void drawPath(LetterPath path)
	{
		stepSprites.clear();
		
		this.allowPencilMove  = false;
		
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
			stepsLayerEntity.attachChild(step);
		}
		showStepSprites();
		this.allowPencilMove  = true;
	}
}