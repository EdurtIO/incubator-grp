package io.edurt.grp.server.module;

import io.edurt.grp.spi.service.ServicePrint;
import io.edurt.grp.spi.service.ServicePrintImpl;
import io.edurt.grp.spi.GrpModule;

public class GrpServiceModule extends GrpModule {

    @Override
    protected void configure() {
        bind(ServicePrint.class).to(ServicePrintImpl.class);
    }

}
