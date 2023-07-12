package com.nexters.jjanji.global.auth;

public class MemberContext {
    public static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();
    public static Long getContext() {
        return MemberContext.CONTEXT.get();
    }
}
