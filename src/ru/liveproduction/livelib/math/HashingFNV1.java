/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/
package ru.liveproduction.livelib.math;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public final class HashingFNV1 {
    private static final BigInteger primeX32 = new BigInteger("16777619");
    private static final BigInteger primeX64 = new BigInteger("1099511628211");
    private static final BigInteger primeX128 = new BigInteger("309485009821345068724781371");
    private static final BigInteger primeX256 = new BigInteger("374144419156711147060143317175368453031918731002211");
    private static final BigInteger primeX512 = new BigInteger("35835915874844867368919076489095108449946327955754392558399825615420669938882575126094039892345713852759");
    private static final BigInteger primeX1024 = new BigInteger("5016456510113118655434598811035278955030765345404790744303017523831112055108147451509157692220295382716162651878526895249385292291816524375083746691371804094271873160484737966720260389217684476157468082573");

    private static final BigInteger offsetX32 = new BigInteger("2166136261");
    private static final BigInteger offsetX64 = new BigInteger("14695981039346656037");
    private static final BigInteger offsetX128 = new BigInteger("144066263297769815596495629667062367629");
    private static final BigInteger offsetX256 = new BigInteger("100029257958052580907070968620625704837092796014241193945225284501741471925557");
    private static final BigInteger offsetX512 = new BigInteger("9659303129496669498009435400716310466090418745672637896108374329434462657994582932197716438449813051892206539805784495328239340083876191928701583869517785");
    private static final BigInteger offsetX1024 = new BigInteger("14197795064947621068722070641403218320880622795441933960878474914617582723252296732303717722150864096521202355549365628174669108571814760471015076148029755969804077320157692458563003215304957150157403644460363550505412711285966361610267868082893823963790439336411086884584107735010676915");

    private static final BigInteger modX32 = new BigInteger("2").pow(32);
    private static final BigInteger modX64 = new BigInteger("2").pow(64);
    private static final BigInteger modX128 = new BigInteger("2").pow(128);
    private static final BigInteger modX256 = new BigInteger("2").pow(256);
    private static final BigInteger modX512 = new BigInteger("2").pow(512);
    private static final BigInteger modX1024 = new BigInteger("2").pow(1024);


    private Bits bit;
    private static HashingFNV1 INSTANCEx32 = new HashingFNV1(Bits.x32);
    private static HashingFNV1 INSTANCEx64 = new HashingFNV1(Bits.x64);
    private static HashingFNV1 INSTANCEx128 = new HashingFNV1(Bits.x128);
    private static HashingFNV1 INSTANCEx256 = new HashingFNV1(Bits.x256);
    private static HashingFNV1 INSTANCEx512 = new HashingFNV1(Bits.x512);
    private static HashingFNV1 INSTANCEx1024 = new HashingFNV1(Bits.x1024);


    private HashingFNV1(Bits bit) {
        this.bit = bit;
    }

    public enum Bits{
        x32, //max length output string 10
        x64, //max length output string 20
        x128, // max length output string 39
        x256, //max length output string 78
        x512, //max length output string 155
        x1024 //max length output string 309
    }

    public static HashingFNV1 getInstance(String bits) {
        if (bits.equals("x32") || bits.equals("32")) return INSTANCEx32;
        if (bits.equals("x64") || bits.equals("64")) return INSTANCEx64;
        if (bits.equals("x128") || bits.equals("128")) return INSTANCEx128;
        if (bits.equals("x256") || bits.equals("256")) return INSTANCEx256;
        if (bits.equals("x512") || bits.equals("512")) return INSTANCEx512;
        if (bits.equals("x1024") || bits.equals("1024")) return INSTANCEx1024;
        return null;
    }

    public static HashingFNV1 getInstance(int bits) {
        switch (bits) {
            case 32: return INSTANCEx32;
            case 64: return INSTANCEx64;
            case 128: return INSTANCEx128;
            case 256: return INSTANCEx256;
            case 512: return INSTANCEx512;
            case 1024: return INSTANCEx1024;
            default: return null;
        }
    }

    public static HashingFNV1 getInstance(Bits bit) {
        switch (bit) {
            case x32: return INSTANCEx32;
            case x64: return INSTANCEx64;
            case x128: return INSTANCEx128;
            case x256: return INSTANCEx256;
            case x512: return INSTANCEx512;
            case x1024: return INSTANCEx1024;
            default: return null;
        }
    }

    public BigInteger hash(byte[] bytes) {
        BigInteger offset = offsetX32;
        BigInteger prime = primeX32;
        BigInteger mod = modX32;
        switch (this.bit) {
            case x64: offset = offsetX64; prime = primeX64; mod = modX64; break;
            case x128: offset = offsetX128; prime = primeX128; mod = modX128; break;
            case x256: offset = offsetX256; prime = primeX256; mod = modX256; break;
            case x512: offset = offsetX512; prime = primeX512; mod = modX512; break;
            case x1024: offset = offsetX1024; prime = primeX1024; mod = modX1024; break;
        }
        BigInteger result = offset;

        for (int i = 0; i < bytes.length; i++) {
            result = result.xor(BigInteger.valueOf(bytes[i])).multiply(prime).mod(mod);
        }
        return result;
    }

    public BigInteger hashString(String str) {
        try {
            return hash(str.getBytes("UTF-8"));
        }catch (UnsupportedEncodingException e) {
            return hash(str.getBytes());
        }
    }


}
