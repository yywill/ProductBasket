CREATE TABLE t_customers (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             user_name VARCHAR(255) NOT NULL,
                             client_display_name VARCHAR(255),
                             api_key VARCHAR(255),
                             PRIMARY KEY (id)
);

INSERT INTO t_customers (user_name, client_display_name, api_key) VALUES ('yywill', 'William Yang', 'API-KEY-123456');
INSERT INTO t_customers (user_name, client_display_name, api_key) VALUES ('yy', 'Yang Yang', 'API-KEY-789012');