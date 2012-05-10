package org.hectorh30.games.writing.sprites;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.DelayModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ease.EaseBackOut;
import org.anddev.andengine.util.modifier.ease.EaseQuadIn;
import org.hectorh30.games.writing.StepsPool;

import android.util.Log;

import com.qwerjk.andengine.entity.sprite.PixelPerfectSprite;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegion;

public class StepSprite extends PixelPerfectSprite {
	
	public float showDelay = 0f;
	public float hideDelay = 0f;
	
	public float initialRelX, initialRelY, letterSpriteX, letterSpriteY; 
	public boolean visible = false;
	public boolean recyclable = false;
	
	ChangeableText number;
	
	final private String tag = "StartActivity";
	
	public StepSprite(
			PixelPerfectSprite letterSprite, 
			final float pX, 
			final float pY, 
			PixelPerfectTextureRegion pTextureRegion, 
			Font font, String text, 
			float showDelay, 
			float hideDelay
		) 
	{
		super(letterSprite.getX() + pX, letterSprite.getY() + pY, pTextureRegion);
		letterSpriteX = letterSprite.getX();
		letterSpriteY = letterSprite.getY();
		
		this.setInitialRelPosition(pX,pY);
		
		this.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		this.showDelay = showDelay;
		this.hideDelay = hideDelay; 
		
		number = new ChangeableText(0,0,font,text,HorizontalAlign.CENTER,"XX".length()){
			@Override
		    public void setAlpha(float pAlpha) {
		            super.setAlpha(pAlpha);
		            super.setColor(pAlpha, pAlpha, pAlpha); // <-- This is the trick !
		    }
		};
		
		
		number.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		number.setPosition((this.getWidth() - number.getWidth())/2 , (this.getHeight() - number.getHeight())/2);
		this.attachChild(number);
		this.setAlpha(0f);
	}
	
	public void animationHide()
	{
		if(this.visible){
			this.visible = false;
			SequenceEntityModifier modifier = 
					new SequenceEntityModifier(
						new IEntityModifierListener() {
							@Override
							public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) { }
		
							@Override
							public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
								StepSprite.this.recyclable = true;
							}
						},
						new DelayModifier(StepSprite.this.hideDelay),
						new ParallelEntityModifier(
							new SequenceEntityModifier(
								new ScaleModifier(0.3f, 1.0f, 1.1f,EaseBackOut.getInstance()),
								new ScaleModifier(0.4f, 1.1f, 0.5f,EaseQuadIn.getInstance())
							),
							new SequenceEntityModifier(
								new DelayModifier(0.1f),
								new AlphaModifier(0.5f, 1f, 0f)
							)
						)
					);
			this.registerEntityModifier(modifier);
		}
	}
	
	public void animationShow()
	{
		if(!this.visible)
		{
			this.recyclable = false;
			this.visible = true;
			
			//this.setPosition(this.getInitialX(),this.getInitialY());
			this.setVisible(true);
			
			SequenceEntityModifier showModifier = 
				new SequenceEntityModifier(
					new IEntityModifierListener() {
						@Override
						public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) { }
	
						@Override
						public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
							StepSprite.this.visible = true;
						}
					},
					new DelayModifier(StepSprite.this.showDelay),
					new ParallelEntityModifier(
						new SequenceEntityModifier(
								new ScaleModifier(0.5f, 0.7f, 1.2f,EaseQuadIn.getInstance()),
								new ScaleModifier(0.4f, 1.2f, 1.0f,EaseBackOut.getInstance())
						),
						new AlphaModifier(0.7f, 0f, 1f)
					)
				);
			this.registerEntityModifier(showModifier);
		}
	}
	
	public void setNumber(String text)
	{
		this.number.setText(text);
	}
	
	@Override
    public void setAlpha(float pAlpha) {
            super.setAlpha(pAlpha);
            super.setColor(pAlpha, pAlpha, pAlpha); // <-- This is the trick !
            number.setAlpha(pAlpha);
    }
	
	@Override
	public boolean collidesWith(IShape other)
	{
		if(this.visible)
			return super.collidesWith(other);
		else
			return false;
	}
	
	public float getShowDelay() {
		return showDelay;
	}

	public void setShowDelay(float showDelay) {
		this.showDelay = showDelay;
	}

	public float getHideDelay() {
		return hideDelay;
	}

	public void setHideDelay(float hideDelay) {
		this.hideDelay = hideDelay;
	}

	public void setInitialRelPosition(float initialX, float initialY) {
		this.initialRelX =  initialX;
		this.initialRelY = initialY;
		this.setPosition(letterSpriteX + initialRelX, letterSpriteY + initialRelY);
	}
	
	public float getInitialRelX() {
		return initialRelX;
	}
	
	public float getInitialRelY() {
		return initialRelY;
	}
		
	@Override
	public void setInitialPosition()
	{
		this.setPosition(initialRelX + letterSpriteX,initialRelY + letterSpriteY);
	}
}