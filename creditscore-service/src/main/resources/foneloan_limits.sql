CREATE TABLE foneloan_limit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    customer_number VARCHAR(50) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    mobile_number VARCHAR(50) NOT NULL,
    one_month_recommended_limit DOUBLE NOT NULL,
    emi_month INT NOT NULL,
    emi_max_amount DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Prevent duplicate tenure rows for same customer
    UNIQUE (account_number, emi_month)
);

INSERT INTO foneloan_limit
(code,customer_number, account_number,mobile_number, one_month_recommended_limit, emi_month, emi_max_amount)
VALUES
    ('abc','1001', '5678','1234567890', 5000.00, 1, 5000.00),
    ('abc','1001', '5678','1234567890', 5000.00, 3, 15000.00),
    ('abc','1001', '5678','1234567890', 5000.00, 6, 30000.0),
    ('abc','1001', '5678','1234567890', 5000.00, 12, 60000.0),
    ('bmw','9808590388','7140327592','9808590388',6000.00,3,60000.0);
