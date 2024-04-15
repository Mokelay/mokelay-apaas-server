package com.mokelay.core.bean.constant;

/**
 * 文件类型
 */
public enum FileType {
    XLSX("xlsx"),
    YAML("yaml");

    private String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Get FileType from type
     *
     * @param type
     * @return
     */
    public static FileType getFileType(String type) {
        FileType ft = null;
        for (FileType _ft : FileType.values()) {
            if (_ft.getType().equalsIgnoreCase(type)) {
                ft = _ft;
                break;
            }
        }

        return ft;
    }
}
