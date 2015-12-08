package com.sun.training.opengl_es;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements Renderer {

	private static final String TAG = MyGLRenderer.class.getSimpleName();

	private Triangle mTriangle;
	private Square mSquare;
	private float mProjectionMatrix[] = new float[16];
	private float mViewMatrix[] = new float[16];
	private float mMVPMatrix[] = new float[16];
	private float mRotationMatrix[] = new float[16];

	private volatile float mAngle;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		Log.d(TAG, "onSurfaceCreated");
		// set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		mTriangle = new Triangle();
		mSquare = new Square();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		Log.d(TAG, "onSurfaceChanged");
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		Log.d(TAG, "onDrawFrame");
		// redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// set the camera position
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		float[] scratch = new float[16];
		// --create a rotation transformation for the triangle
		// long time = SystemClock.uptimeMillis() % 4000L;
		// float angle = 0.090f * ((int) time);
		Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

		// combine the rotation matrix with the projection and camera view
		// Note that the mMVPMatrix factor *must be first* in order for the
		// matrix multiplication product to be correct
		Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

		mSquare.draw(mMVPMatrix);
		mTriangle.draw(scratch);
	}

	public float getAngle() {
		return mAngle;
	}

	public void setAngle(float mAngle) {
		this.mAngle = mAngle;
	}

}
