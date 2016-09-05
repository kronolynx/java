package com.stampery;

public interface Consumer {
	/**
	 * Error event
	 * @param err
	 */
	void onError(String err);
	/**
	 * Event emitted when the Stampery API has finished loading and is ready to Stamp.
	 */
	void onReady();
	/**
	 * When a proof is ready a hash and the proof are emitted
	 * @param hash that belongs to the requested stamp
	 * @param proof is an Object that contains the version, siblings, merkle root and anchor
	 */
	void onProof(String hash, Proof proof);
}
