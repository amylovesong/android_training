package com.sun.training.opengl_es;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20Activity extends Activity {

	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}

	class MyGLSurfaceView extends GLSurfaceView {

		private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
		private MyGLRenderer mRenderer;
		private float mPreviousX;
		private float mPreviousY;

		public MyGLSurfaceView(Context context) {
			super(context);

			// create an OpenGL ES 2.0 context
			setEGLContextClientVersion(2);

			// set the renderer for drawing on the GLSurfaceView
			mRenderer = new MyGLRenderer();
			setRenderer(mRenderer);

			// render the view only when there is a change in the drawing data
			// --To allow the triangle to rotate automatically, this line is
			// commented out
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}

		@Override
		public boolean onTouchEvent(MotionEvent e) {
			float x = e.getX();
			float y = e.getY();

			switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;

				// reverse direction of rotation above the mid-line
				if (y > getHeight() / 2) {
					dy = dy * -1;
				}

				// reverse direction of rotation to left of the mid-line
				if (x < getWidth() / 2) {
					dx = dx * -1;
				}
				
				mRenderer.setAngle(mRenderer.getAngle() + (dx + dy)
						* TOUCH_SCALE_FACTOR);
				requestRender();
			}

			mPreviousX = x;
			mPreviousY = y;
			return true;
		}

	}

}
