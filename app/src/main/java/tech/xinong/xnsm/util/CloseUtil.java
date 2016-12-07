package tech.xinong.xnsm.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xiao on 2016/12/7.
 */

public final class CloseUtil {

    public static void close(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
