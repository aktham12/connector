package com.atypon.io;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ZipManager {
    private ZipManager() {
    }

    public static ZipManager getInstance() {
        return ZipManagerHolder.INSTANCE;
    }
    public void unZip(Path source, Path destination) throws IOException{
        new ZipFile(source.toFile()).extractAll(destination.toString());
    }
    public void zip(String zipName, String filePath) throws ZipException {
        new ZipFile(zipName).addFolder(new File(filePath));
    }
    private static class ZipManagerHolder {
        private static final ZipManager INSTANCE = new ZipManager();
    }



}

