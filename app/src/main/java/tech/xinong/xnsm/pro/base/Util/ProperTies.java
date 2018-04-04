package tech.xinong.xnsm.pro.base.Util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by xiao on 2017/10/27.
 */

public class ProperTies {
    private static final ProperTies ourInstance = new ProperTies();

    public static ProperTies getInstance() {
        return ourInstance;
    }

    private ProperTies() {

    }

    public static Properties getProperties(Context c){
        Properties urlProps;
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            //注意这地方的参数appConfig在eclipse中应该是appConfig.properties才对,但在studio中不用写后缀
            //InputStream in = c.getAssets().open("appConfig.properties");
            InputStream in = c.getAssets().open("appConfig");
            //方法二：通过class获取setting.properties的FileInputStream
            //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            props.load(bufferedReader);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        urlProps = props;
        return urlProps;
    }
}
