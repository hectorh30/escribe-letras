package net.hectorh30.games.writing.launcher;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class TextSpriteMenuItem extends Sprite  implements IMenuItem {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

//    public static int counterID = 0; 
    
    private Text mText;
    private Sprite mParent;
    private final int mID;
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    public TextSpriteMenuItem(int id, final String pText, final TextureRegion pTextureRegion, Font font) {
        
        super(0, 0, pTextureRegion);
        this.mText = new Text(0, 0, font, pText);
        //this.mText.setColor(255, 224, 0);
        this.setPosition(0, 0);
        
        this.mID = id;
//        TextSpriteMenuItem.counterID++;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getID() {
        return this.mID;
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
        
        if(mText.getWidth() >= this.getWidth()){
            float scale = ((float)this.getWidth())/((float)mText.getWidth());
            mText.setScale(0.90f*scale);
        }
        
        float x_text = (this.getWidth()-mText.getWidth())/2.0f;
        float y_text = (this.getHeight()-mText.getHeight())/2.0f;
        
        if(mText != null)
            mText.setPosition(pX + x_text, pY + y_text);
        
    }
    
    public float getWidth(){
        return Math.max(super.getWidth(), mText.getWidth());
    }
    
    public float getHeight(){
        return Math.max(super.getHeight(), mText.getHeight());
    }
    
    public void setScale(float pScale){
        super.setScale(pScale);
        this.mText.setScale(pScale);
    }
    
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public void onSelected() {
    }
    
    public void onUnselected() {
    }

}