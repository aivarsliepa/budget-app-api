package com.aivarsliepa.budgetappapi.constants;

public final class URLPaths {
    public static final String API_PREFIX = "/api";

    public final class Auth {
        public static final String BASE = API_PREFIX + "/auth";

        // -> /api/auth/register
        public static final String REGISTER = "/register";

        // -> /api/auth/login
        public static final String LOGIN = "/login";
        
        private Auth() {
        }
    }

    public final class Categories {
        public static final String BASE = API_PREFIX + "/categories";

        private Categories() {
        }
    }

    public final class Wallets {
        public static final String BASE = API_PREFIX + "/wallets";

        // -> /api/wallets/{walletId}/entries
        public static final String ENTRIES = "/entries";

        private Wallets() {
        }
    }

    private URLPaths() {
    }
}
