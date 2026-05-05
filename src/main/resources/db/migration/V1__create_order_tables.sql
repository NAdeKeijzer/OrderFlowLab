CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_id UUID NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_lines (
                             id UUID PRIMARY KEY,
                             order_id UUID NOT NULL,
                             product_id UUID NOT NULL,
                             quantity INTEGER NOT NULL,

                             CONSTRAINT fk_order_lines_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders (id)
);