package com.sun.training.opengl_es;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Square {
	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;

	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = {// in counterclockwise order:
	-0.5f, 0.5f, 0.0f,// top left
			-0.5f, 0.0f, 0.0f,
			-0.5f, -0.5f, 0.0f,// bottom left
			0.0f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,// bottom right
			0.5f, 0.0f, 0.0f,
			0.5f, 0.5f, 0.0f // top right
			, 0.0f, 0.5f, 0.0f
, 0.0f, 0.0f, 0.0f
	};

	private short drawOrder[] = { 1, 2, 8, 3, 4, 8, 5, 6, 8, 7, 0, 8 };// order
																		// to
																		// draw
																		// vertices

	// set color with red, green, blue and alpha(opacity) values
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	private final String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main(){"
			+ " gl_Position = vPosition * uMVPMatrix;" + "}";

	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main(){"
			+ " gl_FragColor = vColor;" + "}";
	private int mProgram;

	private int mPositionHandle;
	private int mColorHandle;

	private int vertexStride = COORDS_PER_VERTEX * 4;// 4 are how many bytes in
														// a float
	private int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
	private int mMVPMatrixHandle;

	public Square() {
		ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
	}

	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// set color
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		// GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
