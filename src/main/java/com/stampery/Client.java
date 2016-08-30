package com.stampery;

public interface Client {
	void onError(String err);
	void onReady();
	void onProof(String hash, String proof);
}
