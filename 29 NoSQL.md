# 29. NoSQL 기술작업

# NoSQL 설명

---

스프링 데이터는 MongoDB, Neo4J, Elasticsearch, Solr, Redis, Gemfire, Couchbase 그리고 Cassandra 등을 포함한 다양한 NoSQL 기술에 접근할 수 있도록 돕는 프로젝트들을 추가할 수 있다. 스프링부트는 Redis, MongoDB, Elasticsearch, Solr 그리고 Gemfire 를 위한 자동설정을 지원한다. 다른 프로젝트들을 사용할 수도 있지만 필요하다면 설정을 변경할 수도 있다. [projects.spring.io/spring-data에](http://projects.spring.io/spring-data%EC%97%90) 나와있는 참조문서들을 살펴보기 바란다.

NoSQL 이란? ( NoSQL은 "Not Only SQL" 이라고도 불린다. ) 우리가 익숙하게 사용하고있는 RDBMS 형태의 관계형 데이터베이스가 아닌 다른 형태의 데이터 저장 기술을 의미한다

# 레디스

---

Redis는 캐시, 메시지 브로커 그리고 풍부한 기능을 가진 key-value 를 저장한다. 스프링부트는 jedis와 Spring Data Redis에서 제공하는 추상화된 기초적인 자동설정을 제공한다. spring-boot-starter-redis을 'Starter POM'을 이용하면 손쉽게 의존성을 추가할 수 있다.

또한 캐싱,세션관리,pub/sub 메시징 처리등에 사용된다.

## embeded-redis 설정은 어떻게?

---

redis를 설치하지 않고 테스트 해보기 위해서 embed-redis 라이브러리를 추가해서 실행해 보자. 

    --- pom.xml
    <!-- https://mvnrepository.com/artifact/it.ozimov/embedded-redis -->
    <dependency>
        <groupId>it.ozimov</groupId>
        <artifactId>embedded-redis</artifactId>
        <version>0.7.1</version>
        <scope>test</scope>
    </dependency>
    
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Profile;
    import redis.embedded.RedisServer;
    
    import javax.annotation.PostConstruct;
    import javax.annotation.PreDestroy;
    
    --- config 
    @Slf4j //lombok
    @Profile("local") // profile이 local일때만 활성화
    @Configuration
    public class EmbeddedRedisConfig {
    
        @Value("${spring.redis.port}")
        private int redisPort;
    
        private RedisServer redisServer;
    
        @PostConstruct
        public void redisServer() throws IOException {
                redisServer = new RedisServer(redisPort);
                redisServer.start();
        }
    
        @PreDestroy
        public void stopRedis() {
            if (redisServer != null) {
                redisServer.stop();
            }
        }
    }

## 테스트 코드 작성하기

---

    --- 도메인 
    @Getter
    @RedisHash("point")
    public class Point implements Serializable {
    
        @Id
        private String id;
        private Long amount;
        private LocalDateTime refreshTime;
    
        @Builder
        public Point(String id, Long amount, LocalDateTime refreshTime) {
            this.id = id;
            this.amount = amount;
            this.refreshTime = refreshTime;
        }
    
        public void refresh(long amount, LocalDateTime refreshTime){
            if(refreshTime.isAfter(this.refreshTime)){ // 저장된 데이터보다 최신 데이터일 경우
                this.amount = amount;
                this.refreshTime = refreshTime;
            }
        }    
    }
    
    --- repository 
    public interface PointRedisRepository extends CrudRepository<Point, String> {
    }
    
    --- unit test 
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class RedisTest1 {
    
        @Autowired
        private PointRedisRepository pointRedisRepository;
    
        @After
        public void tearDown() throws Exception {
            pointRedisRepository.deleteAll();
        }
    
        @Test
        public void 기본_등록_조회기능() {
            //given
            String id = "jojoldu";
            LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
            Point point = Point.builder()
                    .id(id)
                    .amount(1000L)
                    .refreshTime(refreshTime)
                    .build();
    
            //when
            pointRedisRepository.save(point);
    
            //then
            Point savedPoint = pointRedisRepository.findById(id).get();
            assertThat(savedPoint.getAmount()).isEqualTo(1000L);
            assertThat(savedPoint.getRefreshTime()).isEqualTo(refreshTime);
        }
    
    		@Test
    		public void 수정기능() {
    		    //given
    		    String id = "jojoldu";
    		    LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
    		    pointRedisRepository.save(Point.builder()
    		            .id(id)
    		            .amount(1000L)
    		            .refreshTime(refreshTime)
    		            .build());
    		
    		    //when
    		    Point savedPoint = pointRedisRepository.findById(id).get();
    		    savedPoint.refresh(2000L, LocalDateTime.of(2018,6,1,0,0));
    		    pointRedisRepository.save(savedPoint);
    		
    		    //then
    		    Point refreshPoint = pointRedisRepository.findById(id).get();
    		    assertThat(refreshPoint.getAmount()).isEqualTo(2000L);
    		}
    }

## 자동연결

---

spring.redis로 시작하는 프로퍼티들이 있으니 문서를 참고하면 되겠다. 기본적인 host와 port는 localhost와 redis기본 포트인 6379로 설정되어 있다. 만약 spring data repositories 처럼 사용하고 싶다면 기존의 spring data 처럼 사용해도 된다.

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>
    </dependencies>
    
    --- template
    @Component
    public class MyRedis {
    
      private final StringRedisTemplate stringRedisTemplate;
    
      public MyRedis(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
      }
    }
    
    --- application.yml
    spring.redis.*
    
    --- repository
    public interface AccountRepository extends CrudRepository<Account, String> {
      Account findByName(String name);
    }
    

다른 스프링 빈들에 자동설정된 RedisConnectionFactory, StringRedisTemplate 혹은 vanilla RedisTemplate 인스턴스를 주입할 수 있다. 기본적으로 인스턴스들은 localhost:6379를 사용하여 Redis 서버에 연결한다.

자동구성된 형태의 @Bean을 추가할 경우 기본값을 대체할 것이다(RedisTemplate의 경우를 제외하고 그것의 타입이 아니라 'redisTemplate' 이름에 기초하여 제외한다). 만약 commons-pool2이 클래스패스에 있다면 기본적으로 pooled 커넥션 팩토리를 가지게 될 것이다.

## 라이브러리 선택

- jedis
- lettuce

jedis는 thread-safe하지 않기 때문에 jedis-pool을 사용해야한다. 그러나 비용이 증가하기 때문에 lettuce를 많이 사용한다.

## jedis vs lettuce 비교

![29%20NoSQL/Untitled.png](29%20NoSQL/Untitled.png)

## Lettuce Factory 설정

---

RedisConnectionFactory 인터페이스를 확장한 LettuceConnectionFactory 또는 JedisConnectionFactory를 생성해서 빈으로 등록하면 된다.

(* 참고로 springboot 2.0이상부터는 auto-configuration으로 위의 빈(redisConnectionFactory, RedisTemplate, StringTemplate)들이 자동으로 생성되기 때문에 굳이 Configuration을 만들지 않아도 즉시 사용가능하다.)

말 그대로 Redis에 연결하기위한 Connection 설정을 위한 객체를 만든다고 생각하면 된다.

또한 lettuceConnectionFactory.setHost("192.168.0.78")로 지정할 수 있고 마찬가지로 .setPassword("password");를 사용해서 redis 연결에 필요한 설정을 줄 수 있다.

하지만 권장하지 않는 방법이고 application.properties나 .yml 같은 설정파일에서 설정하는 것이 더 좋다.

## Json 설정 vs No Json 설정

---

    --- config : json 직렬화 안할때 설정 
    @Configuration
    public class RedisConfiguration {
        
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
            return lettuceConnectionFactory;
        }
        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            return redisTemplate;
        }
     
    }
    
    --- service 
    @Service
    public class GetSetService {
        
        @Autowired
        RedisTemplate<String, Object> redisTemplate;
        
        public void test() {
            //hashmap같은 key value 구조
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set("jdkSerial", "jdk");
            String result = (String) vop.get("jdkSerial");
            System.out.println(result);//jdk
        }
    }
    
    --- config : json 직렬화 할때 설정 
    @Configuration
    public class RedisConfiguration {
    
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
            return lettuceConnectionFactory;
        }
    
        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MyData.class));
            return redisTemplate;
        }
    
    }
    
    --- application.yml 
    spring.redis.lettuce.pool.max-active=10
    spring.redis.lettuce.pool.max-idle=10
    spring.redis.lettuce.pool.min-idle=2
    spring.redis.port=6379
    spring.redis.host=127.0.0.1

위와 같은 경우 객체를 직렬화를 하여 바이트로 저장하기 때문에 출력시 이상한 문자열이 출력 될 수 있다. json등을 이용하여 해결 가능하다.

위와 같이 setValueSerializer() 메소드를 사용하여 직렬화 를 설정 할 수 있다.

## 여기서 Jackson이란?

---

Jackson은 text/html 형태의 문자가 아닌 객체등의 데이터를 JSON으로 처리해 주는 라이브러리 이다. Jackson 외에 Google에서 만든 GSON과 SimpleJSON 등이 있다. Spring 3.0 이후부터는 내부적으로 Jackson 관련 API 제공을 통하여 자동화 처리가 가능하도록 도와주었다.

    --- 도메인 
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public class MyData implements Serializable {
        private static final long serialVersionUID = -7353484588260422449L;
        private String studentId;
        private String name;
    }
    
    --- 서비스 
    @Service
    public class DataService {
        @Autowired
        RedisTemplate<String, Object> redisTemplate;
    
        public void test() {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            MyData data = new MyData();
            data.setStudentId("1234566");
            data.setName("HongGilDong");
            valueOperations.set("key", data);
    
            MyData data2 = (MyData) valueOperations.get("key");
            System.out.println(data2);
        }
    }

## Redis Publish/Subscribe

---

---

    @Configuration
    public class RedisConfiguration {
    
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
            return lettuceConnectionFactory;
        }
    
        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MyData.class));
            return redisTemplate;
        }
    
        @Bean
        MessageListenerAdapter messageListenerAdapter() {
            return new MessageListenerAdapter(new RedisService());
        }
    
        @Bean
        RedisMessageListenerContainer redisContainer() {
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(redisConnectionFactory());
            container.addMessageListener(messageListenerAdapter(), topic());
            return container;
        }
    
        @Bean
        ChannelTopic topic() {
            return new ChannelTopic("Event");
        }
    }
    
    @Service
    public class RedisService implements MessageListener {
        public static List<String> messageList = new ArrayList<String>();
    
        @Override
        public void onMessage(Message message, byte[] pattern) {
            messageList.add(message.toString());
            System.out.println("Message received: " + message.toString());
        }
    }

Subscribe를 하기 위해서는 서비스를 만들고, 토픽을 추가시켜주어야 한다.

# 몽고DB

---

몽고DB는 전통적인 테이블 기반의 관계형 데이터를 대신하여 JSON과 유사한 구조를 가진 오픈소스 NoSQL 문서 데이터베이스다. 스프링부트는 spring-boot-starter-data-mongodb 'Starter POM'을 추가하면 몽고DB에서 동작하는 다양한 편의를 제공한다.

## 자동연결

---

MongoDbFactory를 이용해서 직접적으로 mongodb와 연결 할 수 있다. 기본적으로 아무 설정 하지 않았을 경우에 host와 port는 mongodb://localhost/test 와 mongo의 기본 포트인 27017를 사용한다.

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
      </dependency>
    </dependencies>
    
    --- factory
    @Component
    public class MyMongo {
    
      private final MongoDbFactory mongoDbFactory;
    
      public MyMongo(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
      }
    
      public void findByname() {
        DB db = this.mongoDbFactory.getDb();
        //
      }
    }
    
    --- 2.X, application.yml
    spring.data.mongodb.host=test
    spring.data.mongodb.port=27017
    
    --- 3.X, application.yml
    spring.data.mongodb.uri=mongodb://localhost:12345,localhost:23456/test
    
    --- template
    @Component
    public class MyMongo {
    
      private final MongoTemplate mongoTemplate;
    
      public MyMongo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
      }
    }
    
    --- repository
    public interface AccountRepository extends MongoRepository<Account, String>{
      Account findByname(String name);
    }
    

스프링 데이터는 MongoDB를 지원하는 레파지토리를 포함하고 있다. 앞서 거론했던 JPA 레파지토리처럼, 메서드 네임을 바탕으로 하여 자동적으로 쿼리를 생성하는 기본 처리방식을 가지고 있다.

사실, 스프링 데이터 JPA와 스프링 데이터 MongoDB는 같은 공통 인프라스트럭처를 공유한다. 그래서 앞에서 다른 JPA 예제를, `Account`는 JPA `@Entity`에서 몽고 데이터 클래스가 되었다, 동작 방식은 같다:

# Neo4j

---

Neo4j는 그래프 데이터 베이스이다. 이것 또한 Spring boot는 디펜더시만으로 몇몇가지의 빈을 구성해준다.

## 자동연결

---

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-neo4j</artifactId>
      </dependency>
    </dependencies>
    
    --- template : 삭제될 예상임 session을 사용해야 함.
    @Component
    public class MyNeo4j {
      private final Neo4jTemplate neo4jTemplate;
    
      public MyNeo4j(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    
      }
    }
    
    --- session 
    @Component
    public class MyNeo4j {
      private final Session session;
    
      public MyNeo4j(Session session) {
        this.session = session;
      }
    
      public Collection<Account> findAccount() {
        return this.session.loadAll(Account.class);
      }
    }
    
    --- repository
    public interface AccountRepository extends GraphRepository<Account> {
    
      Account findByName(String name);
    }
    
    --- application.yml 
    spring.data.neo4j.*

위와 같이 디펜더시를 받는다면 Neo4jSession, Neo4jTemplate 두 클래스가 빈으로 등록 된다.(구현체만..)

위와 같이 별다른 설정 없이도 Neo4jTemplate을 사용할 수 있다. 하지만 이 클래스는 현재 Deprecated 되어있다. 그래서 spring에서는 Neo4jSession(org.neo4j.ogm.session.Session) 을 권장하고 있다.

Neo4j 또한 repositories를 사용할 수 있다. GraphRepository 혹은 Neo4jRepository 인터페이스를 사용하면 된다.

이것 역시 이름 기반의 쿼리를 지원한다.만약 설정 정보를 변경하고 싶다면 application.properties에 spring.data.neo4j. 로 시작하는 프로퍼티의 값을 변경시켜주면 된다.

# Gemfire

---

Spring Data Gemfire는 Pivotal Gemfire 데이터 관리 플랫픔 접근하기 편리한 스프링에 친화적인 도구들을 제공한다. 편리하게 사용하기 위해서는 spring-boot-starter-data-gemfire 'Starter POM' 의존성을 추가해야한다. 현재는 Gemfire를 위한 자동설정을 제공하지는 않지만, @EnableGemfireRepositories 애노테이션만 사용하면 스프링 데이터 레파지토리를 활성화할 수 있다.

## 자동연결

---

    --- pom.xml 
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-gemfire</artifactId>
      </dependency>
    </dependencies>
    
    --- 자동설정 
    //@.. 
    @EnableGemfireRepositories
    public class GemFireApplication {
     // config
    }

# Solr

---

Apache Solr는 검색엔진이다. 스프링부트는 Spring Data Solr에서 제공하는 solr 클라이언트 라이브러리와 추상화 되어 있는 기본적인 자동설정을 제공한다. spring-boot-starter-data-solr 'Starter POM' 의존성만 추가하면 쉽게 사용할 수 있다.

## 자동연결

---

`spring-boot-starter-data-solr`를 디펜더시를 받으면 몇몇가지의 빈들이 자동으로 등록된다. 조금 이상한게 다른 nosql들은 *Template 클래스들을 모두 자동으로 등록 시켜주는데 solr같은 경우에는 SolrTemplate을 자동으로 빈으로 등록시켜주지 않는다. (필자가 못찾는건지)그래서 만약 SolrTemplate을 사용하려면 몇가지 빈으로 등록시켜 줘야 한다.

    --- pom.xml 
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-solr</artifactId>
      </dependency>
    </dependencies>
    
    @Bean
    public SolrClient solrClient(SolrProperties solrProperties) {
      return new HttpSolrClient(solrProperties.getHost());
    }
    
    @Bean
    public SolrTemplate solrTemplate() {
      return new SolrTemplate(solrClient(null));
    }
    
    @Component
    public class PersonTemplate {
    
      private final SolrTemplate solrTemplate;
    
      public PersonTemplate(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
      }
    
    }
    
    spring.data.solr.*
    
    public interface PersonRepository extends SolrCrudRepository<Person, String> {
    
    }

단순하게 설정했으므로 만약 clustering을 한다면 좀 더 설정이 들어가야 될 듯하다.

위와 같이 수동으로 등록한 SolrTemplate을 사용하면 된다. 몇가지 solr 관련 properties도 지원하므로 아래와 같이 spring.data.solr. 시작하는 프로퍼티들을 설정해주면 된다.

만약 spring data repositories 처럼 사용하고 싶다면 이 역시 기존의 spring data 처럼 사용해도 된다.

자동설정된 SolrServer 인스턴스를 다른 스프링 빈에 주입할 수 있다. 서버에 대한 기본적인 연결설정은 localhost:8993/solr을 사용한다.

스프링 데이터는 Apache Solr을 위한 레파지토리 지원을 포함하고 있다. 앞서 거론했던 JPA 레파지토리처럼, 메서드 네임을 바탕으로 하여 자동적으로 쿼리를 생성하는 기본 처리방식을 가지고 있다.

사실, Spring Data JPA와 Spring Data Solr은 같은 공통 인프라스트럭처를 공유하고 있다. 그래서 앞서 다룬 JPA 예제에서 `Person`는 JPA `@Entity`에서 `SolrDocument` 클래스가 되었다. 동작 방식은 같다.

# Elasticsearch

---

[엘라스틱서치 Elastic Search](http://www.elasticsearch.org/)는 오픈소스, 분산, 실시간 검색 및 분석 엔진이다. 스프링 부트는 엘라스틱서치를 위한 기본적인 자동설정과 [Spring Data Elasticsearch](https://github.com/spring-projects/spring-data-elasticsearch)이 제공하는 추상화를 제공한다. `spring-boot-starter-data-elasticsearch` 'Starter POM'을 추가하면 의존성을 손쉽게 추가할 수 있다.

## 자동**연결**

---

자동설정된 `ElasticsearchTemplate` 이나 엘라스틱서치 `Client` 인스턴스를 다른 스프링빈에 주입할 수 있다. 기본적인 인스턴스는 로컬 인메모리 서버에 접속된다(엘라스틱서치 텀즈 안에 `NodeClient` ), 그러나 `spring.data.elasticsearch.clusterNodes` 에 콤마로 구분된 'host:port' 목록을 설정하면 원격지 서버로 변경할 수 있다.

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
      </dependency>
    </dependencies>
    
    --- template 
    @Component
    public class PersonTemplate {
      private final ElasticsearchTemplate elasticsearchTemplate;
    
      public PersonTemplate(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
      }
    }
    
    --- application.yml 
    spring.data.elasticsearch.*
    
    --- repository
    public interface PersonRepository extends ElasticsearchRepository<Person, String> {
    }

스프링 데이터는 엘라스틱서치 지원을 위한 레파지토리를 포함한다. 앞서 살펴봤던 JPA 레파지터리처럼, 기본적인 쿼리 생성박식은 메서드명을 기반으로 하여 자동생성한다.

사실, Spring Data JPA 와 Spring Data Elasticsearch 는 공통 인프라스트럭처를 공유한다, 그래서 JPA에서 예제로 사용했던 `Person`를 `@Entity` 대신 엘라스틱서치 `@Document` 클래스로 대체하면 된다, 같은 방법으로 동작한다.

# Cassandra

---

카산드라는 페이스북에서 만든 분산 데이타 베이스로 아파치 오픈소스이다. 이 역시 nosql의 한 종류이며 대용량 트랜잭션에 대해 고성능 처리가 가능하도록 설계된 데이터 스토어이다. 이 역시 Spring boot에서는 디펜더시만으로 몇몇가지의 빈을 자동설정 해준다.

## 자동연결

---

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-cassandra</artifactId>
      </dependency>
    </dependencies>
    
    --- template
    @Component
    public class PersonTemplate {
    
      private final CassandraTemplate cassandraTemplate;
    
      public PersonTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
      }
    }
    
    --- application.yml 
    spring.data.cassandra.*
    
    --- repository 
    public interface PersonRepository extends CrudRepository<Person, Long> {
    }

위와 같이 spring-boot-starter-data-cassandra를 디펜더시 받으면 이 역시 CassandraTemplate도 자동으로 빈으로 등록시켜준다.

이 역시 적당한 설정의 프로퍼티들을 지원해주고 있다. spring.data.cassandra 로 시작하는 프로퍼티 키값을 사용하여 설정 정보를 변경할 수 있다.

이 또한 레포지토리로 사용할 수 있다. TypedIdCassandraRepository, CassandraRepository도 가능하지만 여기서는 사용하지 않았다. 왜냐하면 더 자세히 알아보지 않았기 때문이다..

# couchbase

---

Couchbase 또한 nosql의 한 종류이다. 상당한 성능을 갖고 있지만 우리나라에서는 그렇게 많이 쓰지는 않는 듯 하다. 이 것 또한 Spring boot에서는 간단하게 사용할 수 있다. 일단 아래와 같이 디펜더시를 받으면 몇몇 가지의 빈들을 자동설정 해준다.

## 자동연결

---

    --- pom.xml
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-couchbase</artifactId>
      </dependency>
    </dependencies>
    
    --- template 
    @Component
    public class PersonTemplate {
    
      private final CouchbaseTemplate couchbaseTemplate;
    
      public PersonTemplate(CouchbaseTemplate couchbaseTemplate) {
        this.couchbaseTemplate = couchbaseTemplate;
      }
    }
    
    --- application.yml 
    spring.couchbase.*
    
    --- repository 
    @ViewIndexed(designDoc = "person")
    public interface PersonRepository extends CouchbaseRepository<Person, String> {
    }

그러면 다들 알겠지만 CouchbaseTemplate이 자동으로 빈으로 등록되어 아무설정없이 사용해도 된다.

또한 다양한 설정 정보를 지원하기 위해 프로퍼티에 설정정보를 변경할 수 있다. spring.couchbase를 이용해서 설정 정보를 변경하면 된다.

카우치베이스 역시 spring data repositories처럼 사용가능하다.

위와 같이 `CouchbaseRepository` 이용해서 spring data repositories처럼 사용하며 된다.

여기서 설명한 모든 spring data repositories는 이름기반의 메서드를 지원하니 적당한 쿼리들은 이름 기반의 메서드를 작성해서 사용하면 좋을 듯 싶다.

오늘도 이렇게 Spring boot에서 지원해주는 spring data nosql에 대해서 알아봤다. 이제 어떤 nosql이 있고 어떤 자동설정이 있는지 살펴봤으니 다음시간에는 간단하게 예제를 살펴보도록 하자. 물론 아주 간단한 예제들이다..