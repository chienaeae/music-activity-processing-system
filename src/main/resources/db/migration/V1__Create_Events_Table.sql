CREATE TABLE events (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id NVARCHAR(255),
    action NVARCHAR(50) NOT NULL,
    song_id NVARCHAR(255),
    timestamp DATETIMEOFFSET,
    source NVARCHAR(255),
    processed_at DATETIMEOFFSET,
    category NVARCHAR(50) NOT NULL,
    count INT NOT NULL DEFAULT 0
);

-- Create indexes to improve query performance
CREATE INDEX idx_events_category_action ON events(category, action);
CREATE INDEX idx_events_user_id ON events(user_id); 