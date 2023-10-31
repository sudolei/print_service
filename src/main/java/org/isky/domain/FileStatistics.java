package org.isky.domain;


/**
 * 功能描述
 *
 * @author: sunlei
 * @date: 2023年07月18日 14:31
 */
public class FileStatistics {
    /**
     * 文件日期
     */
    private String fileDate;

    /**
     * 材质
     */
    private String material;
    /**
     * 用纸数量
     */
    private Long sumPaper;

    /**
     * 用纸类型
     */
    private String paperType;

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Long getSumPaper() {
        return sumPaper;
    }

    public void setSumPaper(Long sumPaper) {
        this.sumPaper = sumPaper;
    }
}
