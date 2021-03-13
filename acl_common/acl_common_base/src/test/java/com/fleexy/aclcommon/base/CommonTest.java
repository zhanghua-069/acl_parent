package com.fleexy.aclcommon.base;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CommonTest {

    //111111 - 96e79218965eb72c92a549dd5a330112
    @Test
    public void testMD5() {
        String md5 = SecureUtil.md5("111111");
        log.info(md5);
    }
}
