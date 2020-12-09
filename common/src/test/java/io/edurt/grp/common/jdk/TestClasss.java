package io.edurt.grp.common.jdk;

import org.junit.Test;

import java.io.IOException;

public class TestClasss
{
    @Test
    public void testGetClasses()
    {
        try {
            System.out.println(Classs.getClasses("io.edurt.grp.common.jdk"));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
