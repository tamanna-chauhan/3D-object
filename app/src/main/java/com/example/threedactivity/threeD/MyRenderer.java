package com.example.threedactivity.threeD;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{

    private MyCube mCube;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    //create another transformation matrix (a rotation matrix)
    private float[] mRotationMatrix = new float[16];

    /** Store the accumulated rotation. */
    private final float[] mAccumulatedRotation = new float[16];
    /** Store the current rotation. */
    private final float[] mCurrentRotation = new float[16];
    /** Create a temporary matrix. */
    private final float[] mTemporaryMatrix = new float[16];




    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float[] scratch = new float[16];
        // Create a rotation transformation for the square
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;

        // Multiply the current rotation by the accumulated rotation, and then set the accumulated
        // rotation to the result.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

        // Rotate the cube taking the overall rotation into account.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mRotationMatrix, 0, 16);



        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, -2, 2, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw shape
        mCube.draw(scratch);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // initialize a square
        mCube = new MyCube();


        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mAccumulatedRotation, 0);

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
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

    public volatile float mDeltaX;
    public volatile float mDeltaY;
    public volatile  double mTotalDeltaX = 0.0;
    public volatile double mTotalDeltaY = 0.0;

}

