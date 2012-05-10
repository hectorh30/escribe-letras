package org.hectorh30.games.writing;

import java.util.ArrayList;

import org.anddev.andengine.entity.Entity;

public class LetterPath extends Entity {
	private float initialRelX, initialRelY;
	private int stepsNumber = 0; 
	
	public final ArrayList<Float> stepsX, stepsY;
	
	public LetterPath(float initialX, float initialY)
	{
		this.initialRelX = initialX;
		this.initialRelY = initialY;
		
		stepsX = new ArrayList<Float>();
		stepsY = new ArrayList<Float>();
	}
	
	public void addStep(float stepX, float stepY)
	{
		stepsX.add(stepX);
		stepsY.add(stepY);
		stepsNumber++;
	}
	
	public float getInitialRelX() {
		return initialRelX;
	}

	public float getInitialRelY() {
		return initialRelY;
	}
	
	public float getLastStepRelX()
	{
		if(stepsX.size() > 0)
		{
			return stepsX.get(stepsX.size()-1);
		} else {
			return -1f;
		}
	}
	
	public float getLastStepRelY()
	{
		if(stepsX.size() > 0)
		{
			return stepsY.get(stepsY.size()-1);
		} else {
			return -1f;
		}
	}
}
