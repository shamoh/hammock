/*
 * Copyright 2016 Hammock and its contributors
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

package ws.ament.hammock.jpa;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class DataSourceTest {
   @Deployment
   public static JavaArchive createDeployment() {
      return ShrinkWrap.create(JavaArchive.class)
         .addClasses(BeanWithDataSource.class, BuilderBackedBean.class, DataSourceDefinitionBuilder.class)
         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
         .addAsServiceProvider(Extension.class, DataSourceExtension.class);
   }

   @Inject
   @Named("testds")
   private DataSource testds;

   @Inject
   @Named("test2")
   private DataSource test2;

   @Test
   public void shouldInjectDataSource() {
      assertThat(testds).isNotNull();
      assertThat(test2).isNotNull();
   }
}
