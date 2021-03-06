package com.nnl.password.encrypt;

public abstract class AbstractEncryptor {
	public abstract byte[] encrypt(byte[] buffer);
	public abstract byte[] decrypt(byte[] buffer);
}
