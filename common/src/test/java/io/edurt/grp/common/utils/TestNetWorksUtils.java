package io.edurt.grp.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestNetWorksUtils {

    @Test
    public void testGetHostName() {
        Assert.assertNotEquals(NetWorksUtils.getHostName(Boolean.FALSE), null);
        Assert.assertEquals(NetWorksUtils.getHostName(), "localhost");
    }

    @Test
    public void testGetAddress() {
        Assert.assertNotEquals(NetWorksUtils.getAddress(), null);
        Assert.assertNotEquals(NetWorksUtils.getAddress(Boolean.FALSE), null);
    }

}
