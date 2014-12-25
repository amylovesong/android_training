package com.sun.training.opengl_es;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Triangle {
	private FloatBuffer vertexBuffer;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = {// in counterclockwise order:
	0.0f, 0.622008459f, 0.0f, // top
			-0.5f, -0.311004243f, 0.0f, // bottom left
			0.5f, -0.311004243f, 0.0f // bottom right
	};

	// set color with red, green, blue and alpha(opacity) values
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	private final String vertexShaderCode = "attribute vec4 vPosition;"
			+ "void main(){" + " gl_Position = vPosition;" + "}";
	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main(){"
			+ " gl_FragColor = vColor;" + "}";
	private int mProgram;

	private int mPositionHandle;
	private int mColorHandle;

	private int vertexStride = COORDS_PER_VERTEX * 4;// 4 are how many bytes in
														// a float
	private int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

	public Triangle() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(triangleCoords);
		vertexBuffer.position(0);

		// compile the shader code, add them to a OpenGL ES program object and
		// then link the program
		int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);// create OpenGL ES program executables
	}

	public void draw() {
		// add program to OpenGL ES environment
		GLES20.glUseProgram(mProgram);

		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// prepare the triangle coordinates data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

		// disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
