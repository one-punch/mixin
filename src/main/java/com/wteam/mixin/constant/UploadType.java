package com.wteam.mixin.constant;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * 上传文件的类型.
 *
 * @version 1.0
 * @author benko
 * @time 2016-4-7 18:00:24
 */
public enum UploadType {
    /**
     * excel文件.
     */
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"),
    /**
     * word文件.
     */
    WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"),
    /**
     * 图片(.jpg .jpeg,.png)
     */
    IMAGE("image/jpeg","image/png","image/x-png"),
    /**
     * MP4文件.
     */
    MP4("video/mpeg4", "video/mp4"),
    /**
     * PPT文件.
     */
    PPT("application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    /**
     * 是指任意类型的文件.
     */
    OTHERS;
    /** 此上传类型的MINE类型列表. */
    private List<String> contenTypes;

    /**
     * @param contenType
     *            此上传类型的MINE类型数组.
     */
    UploadType(final String... contenType) {
        this.contenTypes = Arrays.asList(contenType);
    }

    /**
     * 根据contenType判断上传文件类型是否符合要求.
     *
     * @param contenType
     *            上传文件MINE类型.
     * @return boolean
     */
    public boolean check(final String contenType) {
        // 如果是
        if (this.equals(OTHERS)) {
            return true;
        }

        return this.contenTypes.contains(contenType);
    }

    /**
     * 检验type字符串是否有对应的枚举类型;若有,返回枚举,若无返回null.
     *
     * @param type
     *            上传类型字符串.
     * @return UploadType
     */
    public static UploadType checkType(final String type) {
        try {
            return UploadType.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 根据文件的类型生成相对路径,例如:"/png/a.png".
     *
     * @param fileName
     *            文件名.
     * @return String
     */
    public String getRelativeURL(final String fileName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String[] array = fileName.split("\\.");
        return "/" + this.name().toLowerCase() + "/" + uuid + "." + array[array.length - 1];
    }

}
