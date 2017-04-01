package org.arquillian.example.service;

import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
public class FluidOuncesConverterTest {

    @EJB
    FluidOuncesConverter converter;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClasses(FluidOuncesConverter.class, FluidOuncesConverterBean.class)
            .addPackages(true, "org.fest");
    }

    @Test
    public void should_convert_fluid_ounces_to_milliliters() throws Exception {
        // given
        double ouncesToConvert = 8d;
        double expectedMilliliters = 236.5882368d;

        // when
        double ouncesInMl = converter.convertToMilliliters(ouncesToConvert);

        // then
        assertThat(ouncesInMl).isEqualTo(expectedMilliliters);
    }
}
