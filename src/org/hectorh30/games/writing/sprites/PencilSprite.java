package org.hectorh30.games.writing.sprites;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.MathUtils;
import org.hectorh30.games.shapes.Ellipse;
import org.hectorh30.games.writing.WriteActivity;

import android.util.Log;

import com.qwerjk.andengine.entity.sprite.PixelPerfectSprite;

public class PencilSprite extends Sprite {
	
	public WriteActivity activity;
	public float 
		deltaX,
		deltaY,
		lineInitPosX,
		lineInitPosY,
		lineEndPosX,
		lineEndPosY,
		letterX,
		letterY,
		initialX,
		initialY,
		distX,
		distY,
		angle;
	private String tag = "StartActivity";
	private float LINE_WIDTH = 3f;
	private float CIRCLE_RATE = 10f; // pixeles por circulo
	public Sprite pointer;
	
	public PencilSprite(
			PixelPerfectSprite letterSprite, 
			final float pX, 
			final float pY, 
			TextureRegion pTextureRegion, 
			WriteActivity activity,
			PixelPerfectSprite pointer) {
		
		super(
			letterSprite.getX() + pX, 
			letterSprite.getY() + pY, 
			pTextureRegion);
		
		this.letterX = letterSprite.getX();
		this.letterY = letterSprite.getY();
		
//		Log.d(tag,"In constructor. Letter: "+this.letterX+", "+this.letterY);
		
		
		this.activity = activity;
		this.pointer = pointer;
		
		this.setInitialRelPosition(pX, pY);
		this.setToInitialPosition();
		
//		this.lineInitPosX  = this.getX() + 2f;
//		this.lineInitPosY  = this.getY() + this.getHeight() - 2f;
	}
	
	public void setPointer(PixelPerfectSprite pointer)
	{
		this.pointer = pointer;
	}
	
	@Override
	public boolean onAreaTouched(
		final TouchEvent pAreaTouchEvent,
		final float pTouchAreaLocalX,
		final float pTouchAreaLocalY
	){
		switch (pAreaTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			deltaX = pAreaTouchEvent.getX() - this.getX();
			deltaY = pAreaTouchEvent.getY() - this.getY();
			
//			this.lineInitPosX = pAreaTouchEvent.getX() - deltaX;
//			this.lineInitPosY = pAreaTouchEvent.getY() - deltaY;
			
			break;
		
		case TouchEvent.ACTION_MOVE:
			if(activity.allowPencilMove){
								
				this.lineEndPosX = pAreaTouchEvent.getX() - deltaX + 2f;
				this.lineEndPosY = pAreaTouchEvent.getY() - deltaY + this.getHeight() - 2f;
				
				float dist = MathUtils.distance(lineEndPosX, lineEndPosY, lineInitPosX, lineInitPosY);
				
				
				
				distX = lineEndPosX - lineInitPosX;
				distY = lineEndPosY - lineInitPosY;
				angle = (float)Math.atan(distY/distX);
				
				if(distX < 0)
					angle += MathUtils.PI;
					
//				Log.d(tag,"Before the loop. distX: "+distX+"; distY: "+distY);
//				Log.d(tag,"Distance: "+dist+"; angle: "+angle);
				for (float i = 0; i < dist; i += CIRCLE_RATE)
				{
					
					Ellipse circle = new Ellipse(
							lineInitPosX + ((float)(Math.cos(angle))) * i,
							lineInitPosY + ((float)(Math.sin(angle))) * i,
							20f,40);	
					circle.setColor(0.992156862745098f, 0.717647058823529f, 0.043137254901961f);
					activity.drawingScene.attachChild(circle);
//					Log.d(tag,"looping for: "+i+"; origin: "+(lineInitPosX + ((float)(Math.cos(angle))) * i)+", "+(lineInitPosY + ((float)(Math.sin(angle))) * i));
				}
//				Log.d(tag,"out of the loop");
				
				this.setPosition(
					pAreaTouchEvent.getX() - deltaX, 
					pAreaTouchEvent.getY() - deltaY
				);
				
				
			}
			break;
		case TouchEvent.ACTION_UP:
			activity.allowPencilMove = true;
			deltaX = 0f;
			deltaY = 0f;
			break;
		}
		return true;
	}
	
	@Override
	public void setPosition(final float pX, final float pY) {
		super.setPosition(pX,pY);
		
		// Update pointer position
		this.pointer.setPosition(this.getX() - 9f,this.getY() + this.getHeight() - 20f);
		
//		Log.d(tag,"pencil sprite setPosition: "+pX+", "+pY);
		
		// Update lineInitPos
		this.lineInitPosX  = this.getX() + 2f;
		this.lineInitPosY  = this.getY() + this.getHeight() - 2f;
	}
	
	public void setInitialRelPosition(float pX, float pY) {
		this.initialX = letterX + pX;
		this.initialY = letterY + pY;
//		this.setToInitialPosition();
	}
	
	@Override
	public float getInitialX() {
		return initialX;
	}
	
	@Override
	public float getInitialY() {
		return initialY;
	}
		
	public void setToInitialPosition()
	{
		this.setPosition(this.initialX ,this.initialY);
	}
}
