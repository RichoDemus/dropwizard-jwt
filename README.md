# dropwizard-jwt
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.richodemus.dropwizard-jwt/dropwizard-jwt/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.richodemus.dropwizard-jwt/dropwizard-jwt)  
JSON Web Token support in Dropwizard using the javax security annotations

## Using
### Maven
```
<dependency>
  <groupId>com.richodemus.dropwizard-jwt</groupId>
  <artifactId>dropwizard-jwt</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
```
compile 'com.richodemus.dropwizard-jwt:dropwizard-jwt:1.0.0'
```
### Examples
look at the examples, I'm to lazy to write doc right now :)

## Development

### Building
```
./gradlew
```

### Running dependency vulnerability audit
`./gradlew audit`
    
### Finding outdated dependencies
```
./gradlew dependencyUpdates
```
    
### Signing and uploading
You'll need a gradle.properties with the following contents
```
signing.keyId=24875D73
signing.password=secret
signing.secretKeyRingFile=/Users/me/.gnupg/secring.gpg

ossrhUsername=your-jira-id
ossrhPassword=your-jira-password
```
and then run
```
./gradlew uploadArchives
```
