package com.airofbengal.android.openglesbasics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.airofbengal.android.openglesbasics.shapes.Square;
import com.airofbengal.android.openglesbasics.shapes.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Square mSquare;

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] rotationMatrix = new float[16];

    public volatile float mAngle;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mTriangle = new Triangle();
        mSquare = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio,-1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.multiplyMM(vPMatrix,0, projectionMatrix, 0, viewMatrix, 0);
        //mTriangle.draw(vPMatrix);

//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int)time);
        Matrix.setRotateM(rotationMatrix, 0, mAngle, 0, 0, -1f);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        mTriangle.draw(scratch);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getAngle(){
        return mAngle;
    }

    public void setAngle(float angle){
        mAngle = angle;
    }
}
