CREATE TABLE bank_money (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            account_number VARCHAR(50) NOT NULL,
                            type VARCHAR(10) NOT NULL, -- DEBIT or CREDIT
                            total_amount DECIMAL(15,2) NOT NULL,
                            transfer_amount  DECIMAL(15,2) NOT NULL,
                            particular_remarks VARCHAR(255),
                            value_date DATE NOT NULL,
                            total_balance DECIMAL(20,2) NOT NULL -- store total bank money balance
);

INSERT INTO bank_money
(account_number, type, total_amount, transfer_amount, particular_remarks, value_date, total_balance)
VALUES
    ('ACC10001', 'DEBIT', 20000000.00, 0, 'EMI deduction', '2025-10-12', 20000000.00);