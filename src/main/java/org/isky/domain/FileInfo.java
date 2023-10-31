package org.isky.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 文件信息对象
 */
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     *
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 材质
     */
    private String material;

    /**
     * 份数
     */
    private Long copies;

    /**
     * 纸张数量
     */
    private Long paper;


    /**
     * 纸张类型
     */
    private String paperType;


    /**
     * 纸张数量
     */
    private Long realPaper;

    /**
     * 是否印刷
     */
    private String printFlag;
    /**
     * 文件日期
     */
    private String fileDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setCopies(Long copies) {
        this.copies = copies;
    }

    public Long getCopies() {
        return copies;
    }

    public void setPaper(Long paper) {
        this.paper = paper;
    }

    public Long getPaper() {
        return paper;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }


    public Long getRealPaper() {
        return realPaper;
    }

    public void setRealPaper(Long realPaper) {
        this.realPaper = realPaper;
    }


    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fileName", getFileName())
                .append("filePath", getFilePath())
                .append("material", getMaterial())
                .append("copies", getCopies())
                .append("paper", getPaper())
                .append("printFlag", getPrintFlag())
                .toString();
    }
}
