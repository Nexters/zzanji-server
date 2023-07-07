package com.nexters.jjanji.common.auth;

public class MemberContext {
    public static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();
    public static Long getContext() {
        return MemberContext.CONTEXT.get();
    }
}
