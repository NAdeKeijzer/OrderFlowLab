CREATE TABLE inventory_items (
                                 product_id UUID PRIMARY KEY,
                                 available_quantity INTEGER NOT NULL,
                                 updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);