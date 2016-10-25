package example;

import com.stampery.Consumer;
import com.stampery.Proof;
import com.stampery.Stampery;

public class Example implements Consumer {

	private Stampery stampery;

	public Example() {
		// Sign up and get your secret token at
		// https://api-dashboard.stampery.com
		stampery = new Stampery("user-secret");

		stampery.subscribe(this);

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
