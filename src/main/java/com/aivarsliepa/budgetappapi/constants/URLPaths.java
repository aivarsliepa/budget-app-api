package com.aivarsliepa.budgetappapi.constants;

public final class URLPaths {
    public final class Categories {
        public static final String BASE = "/categories";

        private Categories() {
        }
    }

    public final class Wallets {
        public static final String BASE = "/wallets";

        // -> /wallets/{walletId}/entries
        public static final String ENTRIES = "/entries";

        private Wallets() {
        }
    }

    private URLPaths() {
    }
}
