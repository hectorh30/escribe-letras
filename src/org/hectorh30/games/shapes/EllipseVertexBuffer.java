package org.hectorh30.games.shapes;


import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.vertex.VertexBuffer;
import org.anddev.andengine.util.MathUtils;

import android.util.Log;

public class EllipseVertexBuffer extends VertexBuffer {

	
	public EllipseVertexBuffer(int segments, int pDrawType, boolean pManaged) {
		super((segments + 2) * 2, pDrawType, pManaged);
//	  	super(segments * 2, pDrawType, true);
	}

  public synchronized void update(int segments, float width, float height) {
	  final int[] vertices = this.mBufferData;

    int count = 0;
    
    vertices[count++] = Float.floatToRawIntBits(0f);
    vertices[count++] = Float.floatToRawIntBits(0f);
    
    for (float i = 0; i < 360.0f; i += (360.0f / segments)) {
    	vertices[count++] = Float.floatToRawIntBits((float) (Math.cos(MathUtils.degToRad(i)) * width));
    	vertices[count++] = Float.floatToRawIntBits((float) (Math.sin(MathUtils.degToRad(i)) * height * -1));
    }
    
    vertices[count++] = Float.floatToRawIntBits((float) (Math.cos(MathUtils.degToRad(0f)) * width));
    vertices[count++] = Float.floatToRawIntBits((float) (Math.sin(MathUtils.degToRad(0f)) * height * -1));

    final FastFloatBuffer buffer = this.getFloatBuffer();
    buffer.position(0);
    buffer.put(vertices);
    buffer.position(0);

    super.setHardwareBufferNeedsUpdate();
  }
}
