-- Add additional metadata fields to the events table
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('events') AND name = 'device_type')
BEGIN
    ALTER TABLE events ADD device_type NVARCHAR(50);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('events') AND name = 'os_version')
BEGIN
    ALTER TABLE events ADD os_version NVARCHAR(50);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('events') AND name = 'app_version')
BEGIN
    ALTER TABLE events ADD app_version NVARCHAR(50);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('events') AND name = 'ip_address')
BEGIN
    ALTER TABLE events ADD ip_address NVARCHAR(50);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('events') AND name = 'location')
BEGIN
    ALTER TABLE events ADD location NVARCHAR(255);
END

-- Create additional indexes if they don't exist
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_events_timestamp' AND object_id = OBJECT_ID('events'))
BEGIN
    CREATE INDEX idx_events_timestamp ON events(timestamp);
END

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_events_location' AND object_id = OBJECT_ID('events'))
BEGIN
    CREATE INDEX idx_events_location ON events(location); 
END 