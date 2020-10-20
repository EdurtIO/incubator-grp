package io.edurt.grp.spi.service;

import java.time.LocalTime;

public class ServicePrintImpl implements ServicePrint {

    @Override
    public void println(String string) {
        System.out.println(String.format("%s print time %s", string, LocalTime.now()));
    }

}
