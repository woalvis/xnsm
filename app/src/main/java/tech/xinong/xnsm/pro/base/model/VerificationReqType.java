package tech.xinong.xnsm.pro.base.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiao on 2017/11/21.
 */

public enum VerificationReqType {
    PERSONAL("个人"),
    ENTERPRISE("企业"),
    OTHER("其他");
    private String displayName;

    VerificationReqType(String s) {
        this.displayName = s;
    }

    public  static String getDispalyName(String verifiedTags){
        if (!TextUtils.isEmpty(verifiedTags))
            return null;
        String[] tags = verifiedTags.split(",");
        List<String> result = new ArrayList<>();
        for (String tag:tags){
            VerificationReqType type = VerificationReqType.valueOf(tag);
            if (type != null)
                result.add(type.getDisplayName());
        }
        return  "";
    }

    public String getDisplayName() {
        return displayName;
    }
}
