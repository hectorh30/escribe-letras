package net.hectorh30.games.writing.sprites;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import javax.microedition.khronos.opengles.GL10;


import org.anddev.andengine.engine.camera.Camera;

public class BackTextSprite extends Sprite {

    private Text mText;
    private Sprite mParent;
    private boolean color_dafult;
    
    public BackTextSprite(float pX, float pY, TextureRegion pTextureRegion, Font mFont, String text) {
        super(pX, pY, pTextureRegion);
        
        this.mText = new Text(pX, pY, mFont, text);
        this.setPosition(pX, pY);
        this.setColorReset();
        //this.setColor(0, 0, 1);
    }

    public void setColorPressed(){
        if(this.color_dafult == false){
            return;
        }
        this.setColor(0.5f, 0.5f, 0.5f);
        this.color_dafult = false;
    }
    
    public void setColorReset(){
        if(this.color_dafult == true){
            return;
        }
//        this.setColor(0.15f, 0.49f, 0.96f);
        this.setColor(0.850980392f, 0.850980392f, 0.37254902f);
        this.color_dafult = true;
    }
    
    public void setParent(Sprite pParent) {
        this.mParent = pParent;
    }
   
   @Override
   protected void onManagedDraw(GL10 pGL, Camera pCamera) {
       super.onManagedDraw(pGL, pCamera);
       if(mText != null){
           mText.onDraw(pGL, pCamera);
       }
   }
 
    public void setPosition(final float pX, final float pY){
        super.setPosition(pX, pY);
        
        int var = 75;
        
        if(mText.getWidth() >= this.getWidth()-var){
            float scale = ((float)this.getWidth()-var-10)/((float)mText.getWidth());
            mText.setScale(scale);
            System.out.println("BackTextSprite: setPosition: scaled!");
        }
        
        float x_text = (this.getWidth()-mText.getWidth()-var-10)/2.0f + var;
        float y_text = (this.getHeight()-mText.getHeight())/2.0f;
        
        if(mText != null)
            mText.setPosition(pX + x_text, pY + y_text);
        
    }
    
    public float getWidth(){
        return super.getWidth();
        //return Math.max(super.getWidth(), mText.getWidth());
    }
    
    public float getHeight(){
        return super.getHeight();
        //return Math.max(super.getHeight(), mText.getHeight());
    }
    
    public void setScale(float pScale){
        super.setScale(pScale);
        this.mText.setScale(pScale);
    }
    
    @Override
    public void setAlpha(float pAlpha) {
            super.setAlpha(pAlpha);
            super.setColor(pAlpha, pAlpha, pAlpha); // <-- This is the trick !
            mText.setAlpha(pAlpha);
    }
}
