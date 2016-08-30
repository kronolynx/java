package com.stampery;

public interface Consumer {
	void onError(String err);
	void onReady();
	void onProof(String hash, String proof);
}
