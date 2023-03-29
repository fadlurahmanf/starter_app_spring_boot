package com.fadlurahmanf.starter.general.constant;

public class EntityConstant {

    public static class Identity{
        public static final String entity = "identity";

        // row
        public static final String id = "id";
        public static final String email = "email";
        public static final String password = "password";
        public static final String pin = "pin";
        public static final String status = "status";
        public static final String balance = "balance";
        public static final String fcmToken = "fcm_token";
        public static final String createdAt = "created_at";
    }

    public static class VerificationEmail {
        public static final String entity = "verification_email";

        // row
        public static final String email = "email";
        public static final String emailType = "email_type";
        public static final String emailToken = "email_token";
        public static final String isVerified = "is_verified";
        public static final String isExpired = "is_expired";
        public static final String expiredAt = "expired_at";
        public static final String createdAt = "created_at";
    }

    public static class HistoryTransfer {
        public static final String entity = "history_transfer";

        // row
        public static final String id = "id";
        public static final String transactionId = "transaction_id";
        public static final String fromUserId = "from_user_id";
        public static final String toUserId = "to_user_id";
        public static final String balance = "balance";
        public static final String createdAt = "created_at";
    }

    public static class PinVerification {
        public static final String entity = "pin_verification";

        // row
        public static final String userId = "user_id";
        public static final String pinToken = "pin_token";
        public static final String isUsed = "is_used";
        public static final String createdAt = "created_at";
    }

    public static class Product {
        public static final String entity = "product";

        // row
        public static final String id = "id";
        public static final String totalProduct = "total_product";
    }
}
