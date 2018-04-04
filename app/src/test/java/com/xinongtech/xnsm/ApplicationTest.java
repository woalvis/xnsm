package com.xinongtech.xnsm;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by xiao on 2017/1/18.
 */

public class ApplicationTest extends InstrumentationTestCase {

    @SmallTest
    public void test_case(){
        final int expected =5;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
