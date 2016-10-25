# Stampery
Stampery API for Java. Notarize all your data using the blockchain!

## Installation
#### Dependency (Maven Artifact)

Maven artifacts are [released to Maven Central](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22stampery-client%22).

For Maven users:
```xml
<dependency>
    <groupId>com.github.stampery</groupId>
    <artifactId>stampery-client</artifactId>
    <version>1.0</version>
</dependency>
```

For sbt users:
```java
libraryDependencies += "com.github.stampery" % "stampery-client" % "1.0"
```

For gradle users:
```java
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.github.stampery:stampery-client:1.0'
}
```

## Usage
We must create a class that implements the Stampery API Consumer interface

Interface:
```java
public interface Consumer {
	void onError(String err);
	void onReady();
	void onProof(String hash, Proof proof);
}
```
Example implementation:
```java
import com.stampery.Consumer;
import com.stampery.Proof;
import com.stampery.Stampery;

public class Example implements Consumer{

	private Stampery stampery;

	public Example() {
		// Sign up and get your secret token at https://api-dashboard.stampery.com
		stampery = new Stampery("user-secret");
		
		// Subscribe to get the proofs
		stampery.subscribe(this);
		// start stampery
		stampery.start();
	}


	public void onError(String err) {
		System.err.println(err);
	}

	public void onReady() {
		// In this case we are going to add a random number to the string
		// to generate a different hash each time.
		String digest = stampery.hash("Hello, blockchain!" + Math.random());
		// stamp the hash
		stampery.stamp(digest);
	}

	public void onProof(String hash, Proof proof) {
		// each time a proof is received
		System.out.println(hash);
		System.out.println(proof);
	}
}
```

# Official implementations
- [NodeJS](https://github.com/stampery/node)
- [PHP](https://github.com/stampery/php)
- [ruby](https://github.com/stampery/ruby)
- [Python](https://github.com/stampery/python)
- [Elixir](https://github.com/stampery/elixir)
- [Java](https://github.com/stampery/java)
- [Go](https://github.com/stampery/go)

# Feedback

Ping us at support@stampery.com and we’ll help you! 😃


## License

Code released under
[the MIT license](https://github.com/stampery/js/blob/master/LICENSE).

Copyright 2016 Stampery
