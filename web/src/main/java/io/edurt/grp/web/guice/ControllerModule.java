package io.edurt.grp.web.guice;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import io.edurt.grp.web.annotation.RestController;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ControllerModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerModule.class);

    private String packageName;
    private final Set<Class<? extends Annotation>> bindingAnnotations;

    public ControllerModule(String packageName)
    {
        this(packageName, RestController.class);
    }

    public ControllerModule(String packageName, final Class<? extends Annotation>... bindingAnnotations)
    {
        this.packageName = packageName;
        this.bindingAnnotations = Sets.newHashSet(bindingAnnotations);
    }

    @Override
    public void configure()
    {
        LOGGER.info("初始化Web模块，开始加载Controller服务，扫描包 {}", this.packageName);
        Reflections packageReflections = new Reflections(packageName);
        try {
            bindingAnnotations
                    .stream()
                    .map(packageReflections::getTypesAnnotatedWith)
                    .flatMap(Set::stream)
                    .forEach(this::bind);
        }
        catch (Exception e) {
            LOGGER.error("初始化Web模块失败，请查看详细日志", e);
        }
    }
}
