package com.example.ratatouille.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsActions {

    public static void askPermission(Activity context, int permission) {
        switch (permission){
            case PermissionIds.REQUEST_READ_CONTACTS:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS)) {
                        // Show an expanation to the user *asynchronously   
                        Toast.makeText(context, "Se necesita el permiso para poder mostrar los contactos!", Toast.LENGTH_LONG).show();
                    }
                    // Request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS}, PermissionIds.REQUEST_READ_CONTACTS);
                }
                Log.i("Permiso ","Permiso ya confirmado");
                break;
            case PermissionIds.REQUEST_CAMERA:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {
                        // Show an expanation to the user *asynchronously   
                        //Toast.makeText(context, "Se necesita el permiso para poder acceder a la camara!", Toast.LENGTH_LONG).show();
                    }
                    // Request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, PermissionIds.REQUEST_CAMERA);
                }
                break;
            case PermissionIds.REQUEST_LOCATION:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an expanation to the user *asynchronously   
                        //Toast.makeText(context, "Se necesita el permiso para poder acceder a la localización!", Toast.LENGTH_LONG).show();
                    }
                    // Request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionIds.REQUEST_LOCATION);
                }
                break;
            case PermissionIds.REQUEST_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an expanation to the user *asynchronously   
                        //Toast.makeText(context, "Se necesita el permiso para poder acceder a la localización!", Toast.LENGTH_LONG).show();
                    }
                    // Request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionIds.REQUEST_READ_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    public static boolean checkPermission(Activity context, int permission){
        switch (permission){
            case PermissionIds.REQUEST_READ_CONTACTS:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            case PermissionIds.REQUEST_CAMERA:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            case PermissionIds.REQUEST_LOCATION:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            case PermissionIds.REQUEST_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
        }
        return false;
    }

}
