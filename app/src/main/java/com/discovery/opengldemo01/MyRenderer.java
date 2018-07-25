package com.discovery.opengldemo01;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    //创建定点着色器所需code
    public static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    //创建片原着色器所需code
    public static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    //边
    public static final int COORDS_PER_VERTEX = 3;

    //定义三个顶点坐标数组
    private static float[] triangleCoordes =
            {0.5f, 0.5f, 0f,
                    -0.5f, -0.5f, 0f,
                    0.5f, -0.5f, 0f};

    //定点数
    public static final int vertexCount = triangleCoordes.length / COORDS_PER_VERTEX;


    //顶点之间的偏移量，每个顶点四个字节
    private final int vertexStride = vertexCount * 4;


    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    //Float缓冲区
    private FloatBuffer vertexBuffer;

    //OpenGL ES 程序
    private int mProgram;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d("vrius", "=========onSurfaceCreated=========");

        //设置背景
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        //申请底层空间
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(triangleCoordes.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        vertexBuffer = allocateDirect.asFloatBuffer();
        vertexBuffer.put(triangleCoordes);
        vertexBuffer.position(0);

        //创建定点着色器
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, VERTEX_SHADER_CODE);
        GLES20.glCompileShader(vertexShader);

        //创建片原着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader,FRAGMENT_SHADER_CODE);
        GLES20.glCompileShader(fragmentShader);


        //创建OpenGl ES程序
        mProgram = GLES20.glCreateProgram();

        //将做色器添加到程序中并进行链接
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.d("vrius", "=========onSurfaceChanged=========");
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.d("vrius", "=========onDrawFrame=========");

        //将程序加入到OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        //获取顶点着色器句柄,并启用
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);

        //设置三角形坐标数据
        GLES20.glVertexAttribPointer(vPosition,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer);

        //获取片元着色器句柄，并设置颜色数据
        int vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(vColor,1,color,0);

        //绘制三角型
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
