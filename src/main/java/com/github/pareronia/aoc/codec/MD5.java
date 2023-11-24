package com.github.pareronia.aoc.codec;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {

    public static byte[] md5(final String data) {
        return DigestUtils.md5(data);
    }

    public static String md5Hex(final String data) {
        return DigestUtils.md5Hex(data);
    }

    public static byte[] md5(final byte[] data) {
        return DigestUtils.md5(data);
    }
}
