package example;

public class Example implements Client{
	
	private Stampery stampery;

	public Example() {
		this.stampery = new Stampery("2d4cdee7-38b0-4a66-da87-c1ab05b43768");
		this.stampery.subscribe(this);
		this.stampery.start();
	}

	public void onError(String err) {
		System.err.println(err);
	}

	public void onReady() {
		String digest = stampery.hash("Hello, blockchain!");
		stampery.stamp(digest);	
	}

	public void onProof(String hash, String proof) {
		System.out.println(hash);
		System.out.println(proof);
	}

}
