package org.hectorh30.games.shapes;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.util.GLHelper;

public class Ellipse extends GLShape {
  private static final float LINEWIDTH_DEFAULT = 1.0f;
  private static final int SEGMENTS_DEFAULT = 50;
  private final EllipseVertexBuffer vertexBuffer;
  private int filledMode;
  private int segments;
  private float lineWidth;
  private float height;
  private float width;

  public Ellipse(float pX, float pY, float radius) { //3
    this(pX, pY, radius, radius);
  }
  
  public Ellipse(float pX, float pY, float width, float height) { //4
    this(pX, pY, width, height, LINEWIDTH_DEFAULT, false, SEGMENTS_DEFAULT); //->7
  }
  
  
  
  public Ellipse(float pX, float pY, float radius, float lineWidth, boolean filled, int segments) { //6
    this(pX, pY, radius, radius, lineWidth, filled, segments); //->7
  }
  
  public Ellipse(float pX, float pY, float radius, boolean filled) { //4
    this(pX, pY, radius, radius, filled, SEGMENTS_DEFAULT);
  }
  
  public Ellipse(int pX, int pY, int radius, float lineWidth, int segments) { //5
    this(pX, pY, radius, lineWidth, false, segments); //->6
  }
  	
  	public Ellipse(float pX, float pY, float radius, int segments)
	{
		this(pX,pY,radius,radius, LINEWIDTH_DEFAULT, true, segments);
	}
  	
  	public Ellipse(float pX, float pY, float width, float height, float lineWidth, boolean filled,int segments) { //7
	    super(pX, pY);
	    this.width = width;
	    this.height = height;
	    this.filledMode = GL10.GL_TRIANGLE_FAN;
	    this.segments = segments;
	    this.lineWidth = lineWidth;
	
	    vertexBuffer = new EllipseVertexBuffer(segments, GL11.GL_STATIC_DRAW,true);
	    
	    BufferObjectManager.getActiveInstance().loadBufferObject(vertexBuffer);
	    this.updateVertexBuffer();
	    
  	}

  @Override
  public float[] getSceneCenterCoordinates() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float getWidth() {
    return width;
  }

  @Override
  public float getHeight() {
    return height;
  }

  @Override
  public float getBaseWidth() {
    return width;
  }

  @Override
  public float getBaseHeight() {
    return height;
  }

  @Override
  public boolean collidesWith(IShape pOtherShape) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean contains(float pX, float pY) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public float[] convertSceneToLocalCoordinates(float pX, float pY) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float[] convertLocalToSceneCoordinates(float pX, float pY) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void onUpdateVertexBuffer() {
    vertexBuffer.update(segments, getWidth(), getHeight());
  }

  @Override
  protected EllipseVertexBuffer getVertexBuffer() {
    return vertexBuffer;
  }

  @Override
  protected boolean isCulled(Camera pCamera) {
    return false;
  }

  @Override
  protected void onInitDraw(final GL10 pGL) {
    super.onInitDraw(pGL);
    
    GLHelper.disableTextures(pGL);
    GLHelper.disableTexCoordArray(pGL);
    
    // enable for nicer lines, at the expense of a limited linewidth of 1
    // pGL.glEnable(GL10.GL_LINE_SMOOTH);
    
    GLHelper.lineWidth(pGL, lineWidth);
  }

  @Override
  protected void drawVertices(GL10 gl, Camera pCamera) {
	  gl.glDrawArrays(filledMode, 0, segments + 2);
  }

  public float getLineWidth() {
    return lineWidth;
  }

  public void setLineWidth(float lineWidth) {
    this.lineWidth = lineWidth;
  }

  public void setHeight(float height) {
    this.height = height;
    this.updateVertexBuffer();
  }

  public void setWidth(float width) {
    this.width = width;
    this.updateVertexBuffer();
  }

}
