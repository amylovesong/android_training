package com.sun.training.opengl_es;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLES20Activity extends Activity {

	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}

	class MyGLSurfaceView extends GLSurfaceView {

		public MyGLSurfaceView(Context context) {
			super(context);

			// create an OpenGL ES 2.0 context
			setEGLContextClientVersion(2);

			// set the renderer for drawing on the GLSurfaceView
			setRenderer(new MyGLRenderer());

			// render the view only when there is a change in the drawing data
			// To allow the triangle to rotate automatically, this line is
			// commented out
			// setRenderMode(RENDERMODE_WHEN_DIRTY);
		}

	}

}
