package com.almagems.cubetraz.graphics;

import static android.opengl.GLES10.*;
import static android.opengl.GLES11Ext.*;


public final class FBO {

    // FBO vars
    public int textureId;

    // Buffers
    private int m_ColorBuffer;
    private int m_DepthBuffer;
    private int m_FrameBuffer;

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public FBO(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void bind() {
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_FrameBuffer);
    }

    private void createTexture(int w, int h) {
        if (textureId != 0) {
            int[] arr = {textureId};
            glDeleteTextures(arr.length, arr, 0);
            textureId = 0;
        }

        width = w;
        height = h;

        int[] temp = new int[1];
        glGenTextures(1, temp, 0);
        glBindTexture(GL_TEXTURE_2D, temp[0]);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        textureId = temp[0];
    }

    private void checkStatus() {
        int status = glCheckFramebufferStatusOES(GL_FRAMEBUFFER_OES);

        switch (status)
        {
            case GL_FRAMEBUFFER_COMPLETE_OES:
                System.out.println("FBO is OK!");
                break;

            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES:
                System.out.println("ERROR: FBO incomplete attachment");
                break;

            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES:
                System.out.println("ERROR: FBO incomplete missing attachment");
                break;

            case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES:
                System.out.println("ERROR: FBO incomplete dimensions");
                break;

            case GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES:
                System.out.println("ERROR: FBO incomplete formats");
                break;

            case GL_FRAMEBUFFER_UNSUPPORTED_OES:
                System.out.println("ERROR: FBO unsupported");
                break;
        }
    }

    public void createWithColorBuffer() {
        int[] colorBuffer = new int[1];
        glGenRenderbuffersOES(1, colorBuffer, 0);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, colorBuffer[0]);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_RGBA8_OES, width, height);

        int[] frameBuffer = new int[1];
        glGenFramebuffersOES(1, frameBuffer, 0);
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, frameBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_RENDERBUFFER_OES, colorBuffer[0]);

        // create and attach the texture to the FBO
        this.createTexture(width, height);
        glFramebufferTexture2DOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_TEXTURE_2D, textureId, 0);

        this.checkStatus();

        m_ColorBuffer = colorBuffer[0];
        m_FrameBuffer = frameBuffer[0];
    }

    public void createWithColorAndDebthBuffer() {
        // color buffer
        int[] colorBuffer = new int[1];
        glGenRenderbuffersOES(1, colorBuffer, 0);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, colorBuffer[0]);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_RGBA8_OES, width, height);

        // Create the dept buffer
        int[] depthBuffer = new int[1];
        glGenRenderbuffersOES(1, depthBuffer, 0);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, depthBuffer[0]);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_DEPTH_COMPONENT16_OES, width, height);

        int[] frameBuffer = new int[1];
        glGenFramebuffersOES(1, frameBuffer, 0);
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, frameBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_RENDERBUFFER_OES, colorBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_DEPTH_ATTACHMENT_OES, GL_RENDERBUFFER_OES, depthBuffer[0]);

        // create and attach the texture to the FBO
        this.createTexture(width, height);
        glFramebufferTexture2DOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_TEXTURE_2D, textureId, 0);

        this.checkStatus();

        m_ColorBuffer = colorBuffer[0];
        m_DepthBuffer = depthBuffer[0];
        m_FrameBuffer = frameBuffer[0];
    }

    public void createWithColorAndDepthStencilBuffer() {
        // color buffer
        int[] colorBuffer = new int[1];
        glGenRenderbuffersOES(1, colorBuffer, 0);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, colorBuffer[0]);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_RGBA8_OES, width, height);

        // create packed depth & stencil buffer with same size as the color buffer
        int[] depthStencilBuffer = new int[1];
        glGenRenderbuffersOES(1, depthStencilBuffer, 0);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, depthStencilBuffer[0]);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_DEPTH24_STENCIL8_OES, width, height);

        int[] frameBuffer = new int[1];
        glGenFramebuffersOES(1, frameBuffer, 0);
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, frameBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_RENDERBUFFER_OES, colorBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_DEPTH_ATTACHMENT_OES, GL_RENDERBUFFER_OES, depthStencilBuffer[0]);
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_STENCIL_ATTACHMENT_OES, GL_RENDERBUFFER_OES, depthStencilBuffer[0]);

        // create and attach the texture to the FBO
        this.createTexture(width, height);
        glFramebufferTexture2DOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_TEXTURE_2D, textureId, 0);

        this.checkStatus();

        m_ColorBuffer = colorBuffer[0];
        m_DepthBuffer = depthStencilBuffer[0];
        m_FrameBuffer = frameBuffer[0];
    }

    public void release() {
        int[] arr = new int[1];

        if (m_FrameBuffer != 0) {
            arr[0] = m_FrameBuffer;
            glDeleteFramebuffersOES(1, arr, 0);
        }

        if (m_ColorBuffer != 0) {
            arr[0] = m_ColorBuffer;
            glDeleteRenderbuffersOES(1, arr, 0);
        }

        if (m_DepthBuffer != 0) {
            arr[0] = m_DepthBuffer;
            glDeleteRenderbuffersOES(1, arr, 0);
        }
    }
}
