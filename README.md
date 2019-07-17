# JWT Security Spring Boot Starter
[Spring Boot](https://spring.io/projects/spring-boot) Starter for `JWT Security`.
> Easy JWT security for your Spring Boot applications

[![Build Status](https://travis-ci.com/rbaul/jwt-security-spring-boot.svg?branch=master)](https://travis-ci.com/rbaul/jwt-security-spring-boot)
[![Sonatype Nexus (Snapshots) badge](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.rbaul/jwt-security-spring-boot-starter.svg)](https://oss.sonatype.org/#nexus-search;quick~jwt-security-spring-boot-starter)
[![CodeFactor](https://www.codefactor.io/repository/github/rbaul/jwt-security-spring-boot/badge)](https://www.codefactor.io/repository/github/rbaul/jwt-security-spring-boot)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Build on
>* Java 8
>* Spring Boot 2.1.2

## Setup
###### Maven dependency
```xml
<repositories>
    <repository>
        <id>Sonatype</id>
        <name>Sonatype's repository</name>
        <url>https://oss.sonatype.org/content/groups/public/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.rbaul</groupId>
    <artifactId>jwt-security-spring-boot-starter</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

###### Gradle dependency
```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/groups/public" }
}

compile 'com.github.rbaul:jwt-security-spring-boot-starter:0.0.2-SNAPSHOT'
```

## Properties example
> Privilege must have prefix `ROLE_`
```yaml
security.jwt:
  #  token:
  #    expiration: 600000
  #    secret-key: secret-key-for-encryption
  #    roles-key: roles
  #    header-name: Authorization
  #    password-strength: 7
  #    prefix: Bearer
  roles:
    - name: Viewer
      description: View user
      privileges:
        - ROLE_VIEW_PRIVILEGE
    - name: Editor
      description: Editor user
      privileges:
        - ROLE_CHANGE_PRIVILEGE
        - ROLE_VIEW_PRIVILEGE
  users:
    - username: viewer
      password: viewer
      roles:
        - Viewer
    - username: editor
      password: editor
      roles:
        - Editor
  privileges:
    - name: ROLE_CHANGE_PRIVILEGE
      description: "Change privilege"
    - name: ROLE_VIEW_PRIVILEGE
      description: "View privilege"
#  administrator:
#    username: admin
#    password: admin
#    role-name: Administrator
#    privilege-name: ROLE_ADMIN_PRIVILEGE
```