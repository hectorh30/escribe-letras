package org.hectorh30.games.writing;

import java.util.Random;

import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.pool.GenericPool;
import org.hectorh30.games.writing.sprites.StepSprite;

import android.util.Log;

import com.qwerjk.andengine.entity.sprite.PixelPerfectSprite;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegion;

public class StepsPool extends GenericPool<StepSprite>{

	private String tag = "StartActivity";
	
	PixelPerfectTextureRegion [] stepTextureRegions;
	Random gen;
	Font mFont; 
	PixelPerfectSprite letterSprite;
	int count = 0;
	
	public StepsPool(final PixelPerfectSprite letterSprite, final Font font, final PixelPerfectTextureRegion ... stepTextureRegions)
	{
		this.stepTextureRegions = stepTextureRegions;
		gen = new Random();
		this.mFont = font;
		this.letterSprite = letterSprite;
	}
	
	@Override
	protected StepSprite onAllocatePoolItem() {
		count = (count+1) % stepTextureRegions.length;
		//int index = gen.nextInt(this.stepTextureRegions.length);
		return new StepSprite(letterSprite, 0f, 0f, this.stepTextureRegions[count], this.mFont, "x", 1.0f,1.0f);
	}	
}
