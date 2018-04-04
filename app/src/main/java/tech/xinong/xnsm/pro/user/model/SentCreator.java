package tech.xinong.xnsm.pro.user.model;

import java.io.File;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/1/17.
 */

public class SentCreator extends BaseDTO {
    private String mode;
    private String number;
    private String company;
    private List<File> files;


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
