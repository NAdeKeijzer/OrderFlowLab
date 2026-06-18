CREATE TABLE outbox_events (
                               id UUID PRIMARY KEY,
                               event_type VARCHAR(255) NOT NULL,
                               aggregate_id UUID NOT NULL,
                               payload TEXT NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               published_at TIMESTAMP WITH TIME ZONE
);