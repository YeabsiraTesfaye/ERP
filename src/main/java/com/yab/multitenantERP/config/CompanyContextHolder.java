package com.yab.multitenantERP.config;

public class CompanyContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCompanySchema(String schema) {
        CONTEXT.set(schema);
    }

    public static String getCompanySchema() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}