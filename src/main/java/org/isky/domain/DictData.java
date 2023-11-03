package org.isky.domain;

import java.io.Serializable;

/**
 * 字典数据表 sys_dict_data
 *
 * @author ruoyi
 */
public class DictData implements Serializable {

    public DictData( String dictLabel, String dictValue) {
        this.dictLabel = dictLabel;
        this.dictValue = dictValue;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }


    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

}
