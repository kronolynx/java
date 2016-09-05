package com.stampery;

import org.msgpack.value.ArrayValue;

public class Anchor {
	private int chain ;
	private String tx;
	
	public Anchor(ArrayValue anchor) {
		chain = anchor.get(0).asIntegerValue().asInt();
		tx = anchor.get(1).toString();
	}
	/**
	 * Chain codes:
	 * 1 Bitcoin livenet
	 * 2 Ethererum livenet
	 * @return Chain code
	 */
	public int getChain() {
		return chain;
	}

	/**
	 * @return Transaction ID
	 */
	public String getTx() {
		return tx;
	}

	@Override
	public String toString() {
		return "Anchor:\n  chain:" + chain + "\n  tx: " + tx;
	}
}
