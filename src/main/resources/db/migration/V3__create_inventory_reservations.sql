CREATE TABLE inventory_reservations (
                                        id UUID PRIMARY KEY,
                                        order_id UUID NOT NULL,
                                        product_id UUID NOT NULL,
                                        quantity INTEGER NOT NULL,
                                        reserved_at TIMESTAMP WITH TIME ZONE NOT NULL
);