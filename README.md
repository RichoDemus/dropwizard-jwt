# dropwizard-jwt
JSON Web Token support in Dropwizard using the javax security annotations

## Using
### Maven
```
<dependency>
  <groupId>com.github.RichoDemus</groupId>
  <artifactId>dropwizard-jwt</artifactId>
  <version>prerelease</version>
</dependency>

<repositories>
  <repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
  </repository>
</repositories>
```
    
### Examples
look at the examples, I'm to lazy to write doc right now :)

## Development

### Building
```
./gradlew
```
    
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
