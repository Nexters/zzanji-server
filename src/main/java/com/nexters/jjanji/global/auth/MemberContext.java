package com.nexters.jjanji.global.auth;

public class MemberContext {
    public static final ThreadLocal<Long> MEMBER = new ThreadLocal<>();
    public static final ThreadLocal<String> DEVICE = new ThreadLocal<>();

    public static Long getMember() {
        return MemberContext.MEMBER.get();
    }

    public static String getDevice() {
        return MemberContext.DEVICE.get();
    }
}
