# Spring Cloud Config Server
[![Build Status](https://travis-ci.org/nmrhtn7898/config-server.svg?branch=master)](https://travis-ci.org/nmrhtn7898/config-server)
[![Coverage Status](https://coveralls.io/repos/github/nmrhtn7898/config-server/badge.svg)](https://coveralls.io/github/nmrhtn7898/config-server)   
1\. 설정 방법 
* application.yml 설정 파일에 아래와 같이 설정 파일을 읽어오는 방법에 따라 설정
* git 리포지토리에서 읽는 경우 uri 및 search-paths 설정(private repository 경우 username, password 필요.
* config server 파일 시스템 또는 클래스패스에서 읽는 경우 search-locations 설정
* pom.xml spring cloud config server 의존성 및 버전 관리 설정  
 
###### bootstrap.yml

```$xslt
encrypt:
  key: nuguribom [프로퍼티 암복호화 키]
spring:
  cloud:
    config:
      server:
        encrypt:
          enabled: false [true 원격 리포지토리의 암호화 프로퍼티 값을 컨피그 서버에서 복호화, false 컨피그 서버에서 복호화 하지 않음]
        git: [git 리포지토리에서 설정 파일을 읽어오는 경우]
          uri: https://github.com/nmrhtn7898/config-repo/
          search-paths: testservice, 디렉토리이름
          username: git 계정 아이디[private repo 경우 필요]
          password: git 게정 비밀번호[private repo 경우 필요]
        native: [컨피그 서버 파일 시스템 또는 클래스패스에서 읽어오는 경우] 
          search-locations: file://Users/testserivce or classpath:config/testservice
```

###### pom.xml

```$xslt
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
2\. 사용 방법
* [Spring Cloud Config Repository](https://github.com/nmrhtn7898/config-repo) 외부 설정 파일 리포지토리 설정 파일을 읽어서 캐시하여 사용.
* 엔드 포인트 url 패턴 */{application-name}/{profile}*, 외부 설정 파일 리포지토리 변경 되는 경우 컨피그 서버에서 변경 된 설정 파일 읽음.
* 사용 예시 /testservice/dev, profile dev 경우 default, dev 설정 파일 모두 사용하고 중복 프로퍼티는 dev 설정 파일에
 존재하는 프로퍼티 값으로 overwrite 한다. 응답 JSON 예시 아래와 같음.
* 암호화 프로퍼티는 {cipher} prefix 적용
```$xslt
{
    "name": "testservice",
    "profiles": [
        "dev"
    ],
    "label": null,
    "version": "23583d91f47e2f7f73ff8c913fd57fdeb20a95d1",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/nmrhtn7898/config-repo//testservice/testservice-dev.yml",
            "source": {
                "name.firstname": "sin",
                "name.lastname": "nari",
                "name.password": "{cipher}3376eee3887c9f11c1593536ce075dd1bab11eba5fbdab9f9348da9e06f7c3fa",
                "name.message": "i am dev"
            }
        },
        {
            "name": "https://github.com/nmrhtn7898/config-repo//testservice/testservice.yml",
            "source": {
                "name.firstname": "sin",
                "name.lastname": "youngjin",
                "name.password": "{cipher}9357c645da1f863891a6dbb93399916413841cc086c49d7821f8b7ccd437a192",
                "name.message": "i am default+++++++++++++++++++++++++++++++++++++++"
            }
        }
    ]
}
```

3\. 실행 방법
* 일반 환경   
1. *mvn spring-boot:run*
* 도커 환경
1. *mvn clean package docker:build*
2. *docker-compose -f docker-compose.yml up -d*
