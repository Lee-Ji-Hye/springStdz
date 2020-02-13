# 28. SQL 데이터베이스 작업

[차례및 범례](https://www.notion.so/b0e041e5e3df4d41b37e35d2f3eb235e)

# 태그

---

#데이터베이스, #SQL, #jdbcTemplate, #Repository, #객체관계매핑, #하이버네이트 

#jpa, #spring-data-jpa

# 질문

---

데이터베이스 연동은 어떻게 해야 하는가?

create, insert, update, delete, select Query를 만들어 DB에 전송하는 방법은?

Query 문자열의 데이터 값을 클래스 오브젝트로 받는 방법은?

DB 백업및 형상관리는 어떻게 해야 하는다?

테이블을 만들때는 꼭 create query를 날려야 하나? 

객체만 선언해도 테이블을 생성하는 방법은 없을까?

엔티티는 무엇인가?

인메모리란?

인메모리 방식 DB란?

인메모리 방식 DB는 왜 쓰는가?

인메모리 방식 DB는 어떻게 써야 하는가?

완성 Query에 대한 로그는 어떻게 봐야 하나?

Query 로그 출력시 Formatting 방법은?

Database Connection Pool 이란?

Connection Pool을 왜 써야 하는가?

Connection Pool에 대한 베스트프랙티스는 있는가?

Tomcat Pool과 DB Connection Pool의 연관관계는 있는가?

DB 커넥션풀 방법은?

실제 데이터베이스 들어간 값을 확인하는 방법은 없는가?

데이터베이스 툴들은 어떤것들이 있는가?

트랜잭션 처리는?

# Connection Pool

---

커넥션 풀이란 db와 연결하는 커넥션을 미리 생성해두고 풀에 저장해두었다 필요할때 꺼내쓰고, 사용후에는 다시 풀에 반환하는 기법을 말한다.

(멀티쓰레드의 쓰레드풀과 유사하다.)

커넥션을 미리 생성해두기 때문에 커넥션을 사용자가 db를 사용할때마다 매번 생성을 하는것보다 더 빠른 속도를 보장한다.

또한 커넥션의 최대 생성 갯수도 제어해주기 때문에 많은 사용자가 몰려도 과부화를 방지할 수 있다.

![28%20SQL/Untitled.png](28%20SQL/Untitled.png)

![28%20SQL/Untitled%201.png](28%20SQL/Untitled%201.png)

커넥션 풀의 오픈소스에는 대표적으로 DBCP와 C3P0이 있는데 여기선 DBCP를 사용하였다. DBCP는 아파치 사이트에서 받을수 있는데, 그 외에 DBCP에서 사용한 Pool인 Commons Pool이 필요하고, 부가적으로 로그 기록에 사용되는 Commons Logging도 필요하다.

각각의 API는 아래 링크에서 받을 수 있다.

Commons DBCP2 : [http://commons.apache.org/proper/commons-dbcp/](http://commons.apache.org/proper/commons-dbcp/)

Commons Pool2 : [http://commons.apache.org/proper/commons-pool/](http://commons.apache.org/proper/commons-pool/)

Commons Logging : [http://commons.apache.org/proper/commons-logging/](http://commons.apache.org/proper/commons-logging/)

### 소스

---

    # DBCP 소스 
    package jdbc;
    
    import java.sql.DriverManager;
    import java.util.Properties;
    
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServlet;
    
    import org.apache.commons.dbcp2.ConnectionFactory;
    import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
    import org.apache.commons.dbcp2.PoolableConnection;
    import org.apache.commons.dbcp2.PoolableConnectionFactory;
    import org.apache.commons.dbcp2.PoolingDriver;
    import org.apache.commons.pool2.impl.GenericObjectPool;
    import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
    
    public class DBCPInit extends HttpServlet {
    
        @Override
        public void init() throws ServletException {
                loadJDBCDriver();
                initConnectionPool();
        }
        private void loadJDBCDriver() {
            try {
                //커넥션 풀에서 사용할 jdbc 드라이버를 로딩
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("fail to load JDBC Driver", ex);
            }
        }
        private void initConnectionPool() {
            try {
                String jdbcUrl = "jdbc:mysql://localhost:3306/chap14?" + "useUnicode=true&characterEncoding=utf8";
                String username = "phs1116";
                String pw = "1234";
                
                //커넥션팩토리 생성. 커넥션 팩토리는 새로운 커넥션을 생성할때 사용한다.
                ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, username, pw);
                
                //DBCP가 커넥션 풀에 커넥션을 보관할때 사용하는 PoolableConnectionFactory 생성
                //실제로 내부적으로 커넥션을 담고있고 커넥션을 관리하는데 기능을 제공한다. ex)커넥션을 close하면 종료하지 않고 커넥션 풀에 반환
                PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
                //커넥션이 유효한지 확인할때 사용하는 쿼리를 설정한다.
                poolableConnFactory.setValidationQuery("select 1");
                
                
                //커넥션 풀의 설정 정보를 생성한다.
                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                //유휴 커넥션 검사 주기
                poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 1L);
                //풀에 있는 커넥션이 유효한지 검사 유무 설정
                poolConfig.setTestWhileIdle(true);
                //커넥션 최소갯수 설정
                poolConfig.setMinIdle(4);
                //커넥션 최대 갯수 설정
                poolConfig.setMaxTotal(50);
    
                //커넥션 풀 생성. 인자로는 위에서 생성한  PoolabeConnectionFactory와 GenericObjectPoolConfig를 사용한다.
                GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnFactory,
                        poolConfig);
                
                //PoolabeConnectionFactory에도 커넥션 풀을 연결
                poolableConnFactory.setPool(connectionPool);
                
                //커넥션 풀을 제공하는 jdbc 드라이버를 등록.
                Class.forName("org.apache.commons.dbcp2.PoolingDriver");
                
     
                PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
                //위에서 커넥션 풀 드라이버에 생성한 커넥션 풀을 등룍한다. 이름은 cp이다.
                driver.registerPool("cp", connectionPool);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    # web.xml에 서블릿 실행 설정 
    <servlet>
        <servlet-name>DBCPInit</servlet-name>
        <servlet-class>jdbc.DBCPInit</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    # 커넥션 사용 
    String jdbcDriver = "jdbc:apache:commons:dbcp:cp";
    ...
    conn = DriverManager.getConnection(jdbcDriver);

# 스프링부트에서는 DBCP

---

스프링부트에서는 기본적으로 HikariCP라는 DBCP를 기본적으로 제공합니다.

[https://github.com/brettwooldridge/HikariCP#frequently-used](https://github.com/brettwooldridge/HikariCP#frequently-used)

## 스프링부트 DBCP 설정

---

- DBCP 설정은 애플리케이션 성능에 중요한 영향을 미치므로 신중히 고려해야하는 부분입니다.
- 커넥션 풀의 커넥션 개수를 많이 늘린다고 해서 제대로된 성능이 나오는 것은 아닙니다. 왜냐하면 커넥션은 CPU 코어의 개수만큼 스레드로 동작하기 때문입니다.
- 스프링에서 DBCP를 설정하는 방법은 다음과 같습니다. spring.datasource.hikari.maximum-pool-size=4 는 커넥션 객체의 최대 수를 4개로 설정하겠다는 의미입니다.

## 의존성 추가

---

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

## application.properties

---

    # application.properties
    spring.datasource.hikari.maximum-pool-size=4
    spring.datasource.tomcat.max-active=50
    spring.datasource.tomcat.max-idle=50
    spring.datasource.tomcat.min-idle=10
    spring.datasource.tomcat.max-wait=-1
    spring.datasource.tomcat.initial-size=10
    spring.datasource.tomcat.test-on-borrow=true
    spring.datasource.tomcat.test-while-idle=true
    spring.datasource.tomcat.validation-query=select 1 
    spring.datasource.tomcat.time-between-eviction-runs-millis=3000

# 아키텍처 소개

---

스프링 프레임워크는 SQL 데이터베이스에서 동작하는 넓은 지원을 제공한다. 

[[JDBC] JDBC, JPA/Hibernate, Mybatis의 차이 - Heee's Development Blog](https://gmlwjd9405.github.io/2018/12/25/difference-jdbc-jpa-mybatis.html)

## JDBC 아키텍처

---

**Java DataBase Connectivity의 약자로서 자바 프로그램 내에서 DB와 관련된 작업을 처리할 수 있도록 도와주는 일을 한다.**

**Java에서 데이터베이스를 사용할 때에는 JDBC API를 이용하여 프로그래밍한다. 자바는 DBMS 종류에 상관없이 한나의 JDBC API를 사용하여 데이터베이스 작업을 처리할 수 잇기 때문에 알아두면 어떤 DBMS든 작업을 처리할 수 잇게 된다.**

![28%20SQL/Untitled%202.png](28%20SQL/Untitled%202.png)

## jpa 아키텍처

---

자바 퍼시스턴스 API또는 자바 지속성 API(Java Persistence API, JPA) 는 자바 플랫폼 SE와 자바 플랫폼 EE를 사용하는 응용프로그램에서 관계형 데이터베이스의 관리를 표현하는 자바 API이다. 

![28%20SQL/Untitled%203.png](28%20SQL/Untitled%203.png)

### 구성요소

---

- `javax.persistance` 패키지로 정의 된 API 그 자체
- [자바 퍼시스턴스 쿼리 언어](https://ko.wikipedia.org/w/index.php?title=%EC%9E%90%EB%B0%94_%ED%8D%BC%EC%8B%9C%EC%8A%A4%ED%84%B4%EC%8A%A4_%EC%BF%BC%EB%A6%AC_%EC%96%B8%EC%96%B4&action=edit&redlink=1) (JPQL)
- 객체/관계 메타데이터

### 엔티티

---

퍼시스턴스 엔티티는 관계형 데이터베이스의 테이블로 지속되는 경량 자바 클래스이다. 이러한 엔티티는 테이블에서 개개의 행에 해당한다. 엔티티는 일반적으로 다른 엔티티들과 관계가 있으며 이러한 관계는 객체/관계형 메타 데이터를 통해 표현된다. 객체/관계형 메타데이터는 어노테이션을 사용하여 엔티티 클래스 파일에 직접 명시하거나 응용프로그램과 함께 배포되는 별도의 XML 설명자 파일에서 지정할 수 있다.

### JPQL

---

자바 퍼시스턴스 쿼리 언어(Java Persistence Query Language, JPQL)는 관계형 데이터베이스에 저장된 엔티티에 대한 쿼리들을 작성한다. 쿼리들은 구문에서 SQL 쿼리와 유사하지만, 데이터베이스 테이블에 직접적으로 처리하지 않고 엔티티 개체에 대하여 처리된다.

## 하이버네이트 아키텍처

---

**하이버네이트 ORM**(Hibernate ORM)은 [자바](https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4)) 언어를 위한 [객체 관계 매핑](https://ko.wikipedia.org/wiki/%EA%B0%9D%EC%B2%B4_%EA%B4%80%EA%B3%84_%EB%A7%A4%ED%95%91) [프레임워크](https://ko.wikipedia.org/wiki/%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4_%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC)이다. [객체 지향](https://ko.wikipedia.org/wiki/%EA%B0%9D%EC%B2%B4_%EC%A7%80%ED%96%A5_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D) 도메인 모델을 [관계형 데이터베이스](https://ko.wikipedia.org/wiki/%EA%B4%80%EA%B3%84%ED%98%95_%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4)로 매핑하기 위한 [프레임워크](https://ko.wikipedia.org/wiki/%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4_%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC)를 제공한다.

하이버네이트는 [GNU LGPL](https://ko.wikipedia.org/wiki/GNU_LGPL) 2.1로 배포되는 [자유 소프트웨어](https://ko.wikipedia.org/wiki/%EC%9E%90%EC%9C%A0_%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4)이다.

![28%20SQL/Untitled%204.png](28%20SQL/Untitled%204.png)

## mybatis 아키텍처

---

**마이바티스**(MyBatis)는 [자바](https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4)) [퍼시스턴스 프레임워크](https://ko.wikipedia.org/wiki/%ED%8D%BC%EC%8B%9C%EC%8A%A4%ED%84%B4%EC%8A%A4_%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC)의 하나로 [XML](https://ko.wikipedia.org/wiki/XML) 서술자나 애너테이션(annotation)을 사용하여 [저장 프로시저](https://ko.wikipedia.org/wiki/%EC%A0%80%EC%9E%A5_%ED%94%84%EB%A1%9C%EC%8B%9C%EC%A0%80)나 [SQL](https://ko.wikipedia.org/wiki/SQL) 문으로 객체들을 연결시킨다.

마이바티스는 [아파치 라이선스](https://ko.wikipedia.org/wiki/%EC%95%84%ED%8C%8C%EC%B9%98_%EB%9D%BC%EC%9D%B4%EC%84%A0%EC%8A%A4) 2.0으로 배포되는 [자유 소프트웨어](https://ko.wikipedia.org/wiki/%EC%9E%90%EC%9C%A0_%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4)이다.

마이바티스는 [IBATIS](https://ko.wikipedia.org/wiki/IBATIS) 3.0의 포크이며 [IBATIS](https://ko.wikipedia.org/wiki/IBATIS)의 원 개발자들이 포함된 팀에 의해 유지보수되고 있다.

![28%20SQL/Untitled%205.png](28%20SQL/Untitled%205.png)

### 사용법

---

    package org.mybatis.example;
    
    public interface BlogMapper {
        @Select("select * from Blog where id = #{id}")
        Blog selectBlog(int id);
    }
    
    BlogMapper mapper = session.getMapper(BlogMapper.class);
    Blog blog = mapper.selectBlog(101);
    
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="org.mybatis.example.BlogMapper">
        <select id="selectBlog" parameterType="int" resultType="Blog">
            select * from Blog where id = #{id}
        </select>
    </mapper>
    
    Blog blog = session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);

# 설정

---

javax.sql.DataSource 인터페이스는 데이터베이스 연결을 위한 표준 메서드를 제공한다.

기본적으로 데이터소스는 데이터베이스 연결을 생성하는 자격들을 얻는데 URL을 사용한다.

## 내장형 데이터베이스 지원: 인-메모리 내장형 데이터베이스

h2, hsql, derby 

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.hsqldb</groupId>
        <artifactId>hsqldb</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.200</version>
        <scope>test</scope>
    </dependency>

spring-boot-starter-jpa 안에 spring-jdbc가 포함되어 있다.

## 외부 데이터베이스 연결

데이터베이스 연결에는 Datasource의 풀링 사용을 자동으로 설정할 수 있다.

- 성능과 동시성을 위한 톰캣 풀링 Datasource 설정은, 언제든지 선택할 수 있음.
- HikariCP라면 사용가능하다.
- Commons DBCP도 사용가능하지만, 출시 시에 사용하는 것은 권장하지 않음.
- 마지막으로 Commons DBCP2도 사용가능함.

spring-boot-starter-jdbc, spring-boot-starter-data-jpa starter-poms를 사용한다면 자동적으로 tomcat-jdbc에 관한 의존성이 자동으로 추가된다. 

추가적인 커넥션풀은 수동으로 설정해야 가능하다. 만약 Datasource 빈을 정의한다면, 자동설정은 동작하지 않는다. 

## 외부설정

    spring.datasource.url=jdbc:mysql://localhost/test
    spring.datasource.username=dbuser
    spring.datasource.password=dbpass
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver

jdbcTemplate 사용: NamedParameterJdbcTemplate 클래스들은 자동설정 되어 있음.

    @Component
    public class MyBean {
    
        private final JdbcTemplate jdbcTemplate;
    
        @Autowired
        public MyBean(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }
    
        // ...
    
    }

# JPA 그리고 Spring Data

자바 영속 API는 객체와 데이터베이스의 관계를 연결하는 표준 기술이다. 

## 주요 의존성

- 하이버네이트 - JPA 구현체 중에서 많이 사용되고 있음.
- Spring Data JPA - JPA 기반의 저장소들을 쉽게 사용할 수있도록 만들어 줌.
- Spring ORMs - 스프링 프레임워크에서 핵심 ORM을 지원함.

## 엔티티 클래스

스프링부트는 Entity Scanning을 사용한다. 

    package com.example.myapp.domain;
    
    import java.io.Serializable;
    import javax.persistence.*;
    
    @Entity
    public class City implements Serializable {
    
        @Id
        @GeneratedValue
        private Long id;
    
        @Column(nullable = false)
        private String name;
    
        @Column(nullable = false)
        private String state;
    
        // ... additional members, often include @OneToMany mappings
    
        protected City() {
            // no-args constructor required by JPA spec
            // this one is protected since it shouldn't be used directly
        }
    
        public City(String name, String state) {
            this.name = name;
            this.country = country;
        }
    
        public String getName() {
            return this.name;
        }
    
        public String getState() {
            return this.state;
        }
    
        // ... etc
    
    }

@EntityScan 어노테이션을 사용하면 엔티티 스캔위치를 변경할 수 있음. 

## Spring Data JPA 레파지토리

Spring Data JPA 레파지토리는 데이터에 대한 접근을 정의할 수 있는 인터페이스들이다.

JPA 쿼리는 메서드의 이름으로부터 자동으로 쿼리를 생성한다. 

예) CityRepository 인터페이스에 findAllByState(String state) 메서드가 정의되어 있다면 state를 주고 모든 도시를 검색할 것이다. 

보다 복잡한 쿼리는 Spring Data의 Query 어노테이션을 메서드에 어노테이드하여 사용할 수 있다. 

스프링 데이터 레포지토리는 Repository혹은 CrudRepository 를 확장한다. 

## 아래는 전형적인 스프링 데이터 레포지토리에 예제:

    package com.example.myapp.domain;
    
    import org.springframework.data.domain.*;
    import org.springframework.data.repository.*;
    
    public interface CityRepository extends Repository<City, Long> {
    
        Page<City> findAll(Pageable pageable);
    
        City findByNameAndCountryAllIgnoringCase(String name, String country);
    
    }

## JPA 데이터베이스 생성 및 삭제

기본적으로 JPA 데이터베이스는 내장된 데이터베이스(H2, HSQL 혹은 Derby)를 사용할 경우 자동적으로 생성된다. 

spring.jpa.* 프로퍼티즈를 사용한 JPA 설정을 통해 보다 명확하게 설정 가능하다.

    spring.jpa.hibernate.ddl-auto=create-drop #테이블 생성하고 제거

spring.jpa.properties.* 하이버네이트 설정 가능

    spring.jpa.properties.hibernate.globally_quoted_identifiers=true

ApplicationContext가 시작할 때까지 기본적을 DDL 실행(혹은 유효성 검사)은 지연된다. 

## 문제? h2-console이 안떠요!

---

h2-console을 보고 싶다면?

콘솔 서버를 수동으로 부팅

안되는 이유?

서블릿 기반응용 프로그램을 개발할 때만 이것이 가능하다고 함.

    @Component
    @Profile("test") // <-- up to you
    public class H2 {
    
        private org.h2.tools.Server webServer;
    
        private org.h2.tools.Server server;
    
        @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
        public void start() throws java.sql.SQLException {
            this.webServer = org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
            this.server = org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
        }
    
        @EventListener(org.springframework.context.event.ContextClosedEvent.class)
        public void stop() {
            this.webServer.stop();
            this.server.stop();
        }
    
    }

# 실습: Spring Boot 2, Webflux, Reactor, H2

---

참고: [https://code.massoudafrashteh.com/spring-boot-webflux-with-RDBMS/](https://code.massoudafrashteh.com/spring-boot-webflux-with-RDBMS/)

## maven 설정

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    
    <properties>
        <h2.version>1.4.197</h2.version>
        <lombok.version>1.16.20</lombok.version>
        <swagger.version>2.8.0</swagger.version>
        <reactor.version>3.1.6.RELEASE</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>${h2.version}</version>
            <scope>runtime</scope>
        </dependency>
        <!-- swagger -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${swagger.version}</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${swagger.version}</version>
        </dependency>
        <!-- unit testing -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <version>${reactor.version}</version>
            <scope>test</scope>
        </dependency>

## application.yml 설정

    spring:
      datasource:
        url: jdbc:h2:mem:customerdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        username: sa
        password:
        driver-class-name: org.h2.Driver
        platform: h2
    
      # enable H2 web console and set url for web console
      # http://localhost:8080/console
      h2:
        console:
          enabled: true
          path: /console

## CustomerController.java

    @RestController
    @RequestMapping("/customers")
    public class CustomerController {
    
        @Autowired
        private CustomerService customerService;
    
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<Customer> save(@RequestBody Customer customer) {
            return customerService.save(customer);
        }
        //...
    }

## CustomerServiceImpl.java

    @Service
    @Transactional
    public class CustomerServiceImpl implements CustomerService {
    
        @Autowired
        private CustomerRepository customerRepository;
    
        @Override
        public Mono<Customer> save(Customer customer) {
            return Mono.just(customerRepository.save(customer));
        }
        //...
    }

## CustomerRepository.java

    @Repository
    public interface CustomerRepository extends JpaRepository<Customer, Long> {}

## Boot start

    @EnableWebFlux
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }

## testCode

    @RunWith(SpringRunner.class)
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @SpringBootTest(classes=Application.class,
            webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
    public class CustomerIntegrationTest {
    
        @Autowired
        private WebTestClient webTestClient;
    
        private Customer newCustomer = new Customer();
    
        public void test2_add_customer_done() {
            WebClient webClient = WebClient.create("http://localhost:" + port);
            Customer customer = webClient.post().uri("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(newCustomer))
                    .retrieve()
                    .bodyToMono(Customer.class)
                    .block();
    
            id = customer.getId();
            assertThat(customer.getName(), is(newCustomer.getName()));
        }
    
        @Test
        public void test3_find_customer_done() {
            webTestClient.get().uri("/customers/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name")
                    .isEqualTo(newCustomer.getName());
        }

## 소스다운로드

git clone spring-boot-reactive-restful-rdbms