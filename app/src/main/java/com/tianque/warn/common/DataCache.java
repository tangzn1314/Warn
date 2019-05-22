package com.tianque.warn.common;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @author ctrun on 2015/6/8.
 */
@SuppressWarnings("all")
public class DataCache<T> {

    public final static String FOLDER_GLOBAL = "FOLDER_GLOBAL";

    public void save(Context ctx, ArrayList<T> data, String name) {
        save(ctx, data, name, "");
    }

    private void save(Context ctx, Object data, String name, String folder) {
        if (ctx == null) {
            return;
        }

        File file;
        if (!folder.isEmpty()) {
            File fileDir = new File(ctx.getFilesDir(), folder);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdir();
            }
            file = new File(fileDir, name);
        } else {
            file = new File(ctx.getFilesDir(), name);
        }

        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(data);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGlobal(Context ctx, Object data, String name) {
        save(ctx, data, name, FOLDER_GLOBAL);
    }

    public void delete(Context ctx, String name) {
        File file = new File(ctx.getFilesDir(), name);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteGlobal(Context ctx, String name) {
        File globalFolder = new File(ctx.getFilesDir(), FOLDER_GLOBAL);
        if (!globalFolder.exists()) {
            return;
        }

        deleteFile(globalFolder, name);
    }

    private void deleteFile(File folder, String name) {
        File file = new File(folder, name);
        if (file.exists()) {
            file.delete();
        }
    }

    public ArrayList<T> load(Context ctx, String name) {
        return load(ctx, name, "");
    }

    private ArrayList<T> load(Context ctx, String name, String folder) {
        ArrayList<T> data = null;

        File file;
        if (!folder.isEmpty()) {
            File fileDir = new File(ctx.getFilesDir(), folder);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdir();
            }
            file = new File(fileDir, name);
        } else {
            file = new File(ctx.getFilesDir(), name);
        }

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                data = (ArrayList<T>) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new ArrayList<T>();
        }

        return data;
    }

    public ArrayList<T> loadGlobal(Context ctx, String name) {
        return load(ctx, name, FOLDER_GLOBAL);
    }

    public T loadGlobalObject(Context ctx, String name) {
        String folder = FOLDER_GLOBAL;
        T data = null;

        File file;
        if (!folder.isEmpty()) {
            File fileDir = new File(ctx.getFilesDir(), folder);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdir();
            }
            file = new File(fileDir, name);
        } else {
            file = new File(ctx.getFilesDir(), name);
        }

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                data = (T) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
