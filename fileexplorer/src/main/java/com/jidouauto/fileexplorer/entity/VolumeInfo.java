package com.jidouauto.fileexplorer.entity;

import java.util.Objects;

public class VolumeInfo {
    public String name;
    public String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolumeInfo that = (VolumeInfo) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path);
    }
}
