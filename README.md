# Stampery
Stampery API for Java. Notarize all your data using the blockchain!

## Installation



```java
// coming soon
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
		// Create an object of the Class Stampery
		stampery = new Stampery("2d4cdee7-38b0-4a66-da87-c1ab05b43768");
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
- [java](https://github.com/stampery/java)

# Feedback

Ping us at support@stampery.com and we’ll help you! 😃


## License

Code released under
[the MIT license](https://github.com/stampery/js/blob/master/LICENSE).

Copyright 2016 Stampery
