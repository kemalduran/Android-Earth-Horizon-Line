package com.example.earthhorizonline;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

public class CameraManager {

    private static CameraPreview mPreview;
    private static Camera mCamera;


    public static void startCamera(Activity activity, FrameLayout preview) {
        if (checkPermissions(activity)) {
            Context context = activity.getApplicationContext();

            // Create an instance of Camera
            mCamera = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(context, mCamera);
            preview.addView(mPreview);

        }
    }

    public static boolean checkPermissions(Activity activity) {
        return ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

            Camera.Parameters params = c.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static void releaseCamera(FrameLayout preview) {
        preview.removeView(mPreview);
        mPreview = null;

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
