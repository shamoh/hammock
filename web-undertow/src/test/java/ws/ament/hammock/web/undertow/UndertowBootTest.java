/*
 * Copyright 2015 John D. Ament
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ws.ament.hammock.web.undertow;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import ws.ament.hammock.web.spi.ServletDescriptor;

import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class UndertowBootTest {
    @Rule
    public TestName testName = new TestName();
    @Test
    public void shouldBootWebServer() throws Exception {
        BasicConfigurator.configure();
        try(WeldContainer weldContainer = new Weld(testName.getMethodName()).disableDiscovery()
                .beanClasses(UndertowServletMapper.class, UndertowWebServer.class, DefaultServlet.class, MessageProvider.class)
                .initialize()) {
            UndertowWebServer undertowWebServer = weldContainer.select(UndertowWebServer.class).get();
            undertowWebServer.addServlet(new ServletDescriptor("Default",null,new String[]{"/"},1,null,true,DefaultServlet.class));
            undertowWebServer.start();

            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(DefaultServlet.DATA);
            }

            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(DefaultServlet.DATA);
            }
        }
    }
}
