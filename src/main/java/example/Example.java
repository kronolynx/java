package example;

import com.stampery.Consumer;
import com.stampery.Proof;
import com.stampery.Stampery;

public class Example implements Consumer{
	
	private Stampery stampery;

	public Example() {
		// Create an object of the Class Stampery
		this.stampery = new Stampery("2d4cdee7-38b0-4a66-da87-c1ab05b43768");
		// Subscribe to get the proofs
		this.stampery.subscribe(this);
		// start stampery
		this.stampery.start();
	}

	
	public void onError(String err) {
		System.err.println(err);
	}

	public void onReady() {
		// Hash a string with a random number at the end to get a different hash each time
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
