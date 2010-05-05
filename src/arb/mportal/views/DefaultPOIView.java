package arb.mportal.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import arb.mportal.MainActivity;
import arb.mportal.R;


public class DefaultPOIView extends View implements View.OnTouchListener { 

	
	private class TouchStateClosed implements ITouchState { 
		private DefaultPOIView v = null;
		public TouchStateClosed(DefaultPOIView v) {
			this.v = v; 
		} 
		public void draw(Canvas c) {    
			c.drawBitmap(bc, 0,0,p);
		}
		public boolean onTouch(View v, MotionEvent event) {
			this.v.stateTransitionTo(DefaultPOIView.STATE_OPENED); 
			return true;
		}
	}
	
	
	private class TouchStateOpened implements ITouchState { 
		private DefaultPOIView v = null;		
		public TouchStateOpened(DefaultPOIView v) {
			this.v = v;  
		} 
		public void draw(Canvas c) {       
			c.drawBitmap(bo, 0,0,p);  
		}
		public boolean onTouch(View v, MotionEvent event) {
			this.v.stateTransitionTo(DefaultPOIView.STATE_CLOSED);  
			return true; 
		}
	}

	
	
	protected ITouchState currentState = null;
	protected boolean touchDown = false;
	protected static ITouchState STATE_CLOSED = null;
	protected static ITouchState STATE_OPENED = null; 
	private static Paint p = new Paint();
	private static Bitmap bc = null;
	private static Bitmap bo = null;	
	
	 
	public DefaultPOIView(Context c) {
		super(c); 
		
		bo = BitmapFactory.decodeResource(getResources(), R.drawable.arb_background_on);
		bc = BitmapFactory.decodeResource(getResources(), R.drawable.arb_background);
		
		STATE_CLOSED = new TouchStateClosed(this);
		STATE_OPENED = new TouchStateOpened(this);
		setOnTouchListener(this);
		stateTransitionTo(STATE_CLOSED);  
		 
		setDrawingCacheEnabled(true);
		
		
	}
	
	
	protected void stateTransitionTo(ITouchState newState) {
		currentState = newState; 
	}
	
	
	public boolean onTouch(View v, MotionEvent e) {
		if(touchDown == false) { 
			touchDown = true; 
			MainActivity.t.setText(String.valueOf(System.currentTimeMillis())); 
			return currentState.onTouch(v, e);			
		} else if(e.getAction() == MotionEvent.ACTION_UP) { 
			touchDown = false;
		}
		return true;

	}
	
	
	@Override  
	public void onDraw(Canvas c) { 
		super.onDraw(c);
		currentState.draw(c);
	}
	
	
	@Override
	public void draw(Canvas c) {
		super.draw(c);  
		/*Bitmap cache = getDrawingCache();
		if(cache != null) 
			c.drawBitmap(cache, 0, 0, p);*/   
	}
}