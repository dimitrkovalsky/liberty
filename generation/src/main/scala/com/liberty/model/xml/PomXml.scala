import com.liberty.common.ProjectConfig

/**
* User: Maxxis
* Date: 26.10.2014
* Time: 23:31
*/
class PomXml {
  def createXml() = {
    val xml = <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>{ProjectConfig.basePackageString}</groupId>
      <artifactId>{ProjectConfig.projectName}</artifactId>
      <packaging>war</packaging>
      <dependencies>
        <!--Rest-->
        <dependency>
          <groupId>org.jboss.resteasy</groupId>
          <artifactId>resteasy-jaxrs</artifactId>
          <version>3.0.4.Final</version>
          <scope>provided</scope>
        </dependency>
        <dependency>
          <groupId>org.jboss.resteasy</groupId>
          <artifactId>jaxrs-api</artifactId>
          <version>3.0.4.Final</version>
          <scope>provided</scope>
        </dependency>
        <dependency>
          <groupId>net.sf.scannotation</groupId>
          <artifactId>scannotation</artifactId>
          <version>1.0.2</version>
          <scope>provided</scope>
        </dependency>

        <!--Rest End-->
        <dependency>
          <groupId>javax</groupId>
          <artifactId>javaee-api</artifactId>
          <version>7.0</version>
          <scope>provided</scope>
        </dependency>

        <dependency>
          <groupId>org.jboss.resteasy</groupId>
          <artifactId>resteasy-jackson-provider</artifactId>
          <version>3.0.4.Final</version>
        </dependency>

        <dependency>
          <groupId>postgresql</groupId>
          <artifactId>postgresql</artifactId>
          <version>9.1-901-1.jdbc4</version>
        </dependency>

        <dependency>
          <groupId>org.eclipse.persistence</groupId>
          <artifactId>org.eclipse.persistence.jpa</artifactId>
          <version>2.5.0</version>
        </dependency>
        <dependency>
          <groupId>org.hibernate</groupId>
          <artifactId>hibernate-validator</artifactId>
          <version>5.1.1.Final</version>
        </dependency>

        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.11</version>
        </dependency>
      </dependencies>
      <build>
        <finalName>standard</finalName>
      </build>
      <properties>
        <maven.compiler.source>1.7</maven.compiler.source>
        <maven.compiler.target>1.7</maven.compiler.target>
        <failOnMissingWebXml>false</failOnMissingWebXml>
      </properties>
    </project>
    xml
  }

}
