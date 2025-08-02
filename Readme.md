# Java Wrapper for EVE Online API Endpoints
This project provides a reusable Java wrapper for interacting with EVE Online’s API. As I developed more tools for the EVE ecosystem, I noticed recurring code across multiple projects. To streamline development and follow the DRY (Don't Repeat Yourself) principle, I consolidated that logic into this standalone library, which can be easily included as a Maven dependency.

# How to integrate into your project
First, add jitpack as a repository in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Then add the dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.MrAkaki</groupId>
    <artifactId>EvEEndpoints</artifactId>
    <version>main-SNAPSHOT</version>
</dependency>
```

# Usage

To use the library, you need to call static methods from the `EveEndpoints` class. 
The most important method is `Common::SetUserAgent` as this gives CCP a way to communicate with you if needed(for example related to ban and over usage).

```java
//Call this method once at the start of your application
Common.SetUserAgent("YourAppName/1.0 (your email address)"); 
```

You can then call any of the API endpoints like this:

```java
var characterInformation = Character.GetCharacterInformation(2120599058);
var corporationInformation = Corporation.GetCorporationInformation(98659319);
```

# Available Endpoints
- **Character Endpoints**
  - `getPublicInformation(int characterId)`
  - `getCharacterPortrait(int characterId)`
- **Corportation Endpoints**
  - `getPublicInformation(int corporationId)`
  - `getAllianceHistory(int corporationId)`
- **Alliance Endpoints**
  - `getPublicInformation(int allianceId)`
  - `getAllAlliances()`
  - `getIcons(int allianceId)`
  - `getAllianceCorporations(int allainceId)`