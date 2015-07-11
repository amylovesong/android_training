package com.sun.training.capture_photo;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class ControllCameraActivity extends Activity {
	private static final String TAG = ControllCameraActivity.class
			.getSimpleName();

	private Camera mCamera;
	private Preview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPreview = new Preview(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (safeCameraOpen(0)) {
					mPreview.setCamera(mCamera);
				}
			}
		}).start();

	}

	@Override
	protected void onPause() {
		super.onPause();

		mPreview.setCamera(null);
	}

	private boolean safeCameraOpen(int id) {
		boolean qOpened = false;
		try {
			releaseCameraAndPreview();
			mCamera = Camera.open(id);
			// mCamera = Camera.open();
			qOpened = (mCamera != null);
		} catch (Exception e) {
			Log.e(TAG, "failed to open Camera");
			e.printStackTrace();
		}

		Log.d(TAG, "safeCameraOpen qOpened=" + qOpened);
		return qOpened;
	}

	private void releaseCameraAndPreview() {
		mPreview.setCamera(null);
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	// The preview requires an implementation of the SurfaceHolder.Callback
	// interface, which is used to pass image data from the camera hardware to
	// the application.
	class Preview extends ViewGroup implements SurfaceHolder.Callback {

		SurfaceView mSurfaceView;
		SurfaceHolder mHolder;
		Camera mCamera;
		List<Size> mSupportedPreviewSizes;
		Size mPreviewSize;

		public Preview(Context context) {
			super(context);

			mSurfaceView = new SurfaceView(context);
			addView(mSurfaceView);

			mHolder = mSurfaceView.getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			Log.d(TAG, "mSurfaceView " + mSurfaceView);
			Log.d(TAG, "mHolder=" + mHolder);
			if (mHolder != null) {
				Log.d(TAG, "mHolder.getSurface()=" + mHolder.getSurface());
			}
		}

		// Set and start the preview
		public void setCamera(Camera camera) {
			Log.d(TAG, "setCamera mCamera=" + mCamera + " camera=" + camera);
			if (mCamera == camera) {
				return;
			}

			stopPreviewAndFreeCamera();

			mCamera = camera;
			if (mCamera != null) {
				List<Size> localSizes = mCamera.getParameters()
						.getSupportedPreviewSizes();
				mSupportedPreviewSizes = localSizes;
				mPreviewSize=mSupportedPreviewSizes.get(0);
				Log.d(TAG, "mPreviewSize width=" + mPreviewSize.width
						+ " height=" + mPreviewSize.height);
				requestLayout();

				Log.d(TAG, "setPreviewDisplay "
						+ (mHolder.getSurface() == null));
				// FIXME
				// "12-15 17:10:27.119: D/Camera(25053): app passed NULL surface"
				try {
					mCamera.setPreviewDisplay(mHolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Important: Call startPreview() to start updating the preview
				// surface. Preview must be started before you can take a
				// picture.
				mCamera.startPreview();
			}

		}

		private void stopPreviewAndFreeCamera() {
			if (mCamera != null) {
				mCamera.stopPreview();

				mCamera.release();
				mCamera = null;
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "surfaceChanged holder=" + holder);
		}

		// Modify camera settings
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d(TAG, "surfaceChanged holder=" + holder + " format=" + format
					+ " width=" + width + " height=" + height);
			Camera.Parameters parameters=mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();
			mCamera.setParameters(parameters);

			mCamera.startPreview();
		}

		// stop the preview
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(TAG, "surfaceChanged holder=" + holder);
			if (mCamera != null) {
				mCamera.stopPreview();
			}
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub

		}

	}
}
