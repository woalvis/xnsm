package tech.xinong.xnsm.pro.user.model;

import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;

/**
 * Created by xiao on 2018/1/17.
 */

public class LogisticsModel extends BaseDTO {

    private String mode;
    private String number;
    private String company;
    private List<ListingDocDTO> docs;
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

    public List<ListingDocDTO> getDocs() {
        return docs;
    }

    public void setDocs(List<ListingDocDTO> docs) {
        this.docs = docs;
    }
}
