

CREATE TABLE widgets (
    -- Primary Key and Identifiers
                         widget_id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Automatically generates a UUID
                         name VARCHAR(255) NOT NULL,
                         type VARCHAR(50) NOT NULL, -- Widget category (e.g., 'Chart', 'Sensor', 'Video')

    -- Network/Protocol Information
                         protocol VARCHAR(10) NOT NULL, -- e.g., 'https'
                         host VARCHAR(255) NOT NULL,    -- e.g., 'www.youtube.com'
                         url_path TEXT NOT NULL,        -- The specific path/query (e.g., 'watch?v=...')
                         port INT,                      -- Optional port number
                         ip_address INET,               -- Use INET type for IP addresses (can be NULL)

    -- Metadata and Roles
                         image_url TEXT,
                         role_id VARCHAR(50) NOT NULL,
                         role_type VARCHAR(50) NOT NULL, -- e.g., 'ADMIN', 'USER'
                         status VARCHAR(50) NOT NULL,   -- e.g., 'ACTIVE', 'INACTIVE'

    -- Timestamps
                         date_added DATE NOT NULL,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE accounts (
    -- Core Identifiers and Authentication
                          id BIGSERIAL PRIMARY KEY,                    -- Auto-incrementing primary key
                          username VARCHAR(50) UNIQUE NOT NULL,        -- Must be unique and non-null
                          mail VARCHAR(255) UNIQUE NOT NULL,           -- Email, must be unique (used for login/recovery)
                          password_hash TEXT NOT NULL,                 -- Stores the secure password hash (NEVER store plaintext passwords!)

    -- User Metadata
                          full_name VARCHAR(255),                      -- Optional
                          role VARCHAR(50) NOT NULL,                   -- e.g., 'ADMIN', 'USER', 'GUEST'

    -- Timestamps
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          last_login TIMESTAMP WITH TIME ZONE          -- Can be NULL initially
);

-- OPTIONAL: Add an index for faster lookups by email (common login method)
CREATE UNIQUE INDEX idx_accounts_mail ON accounts (mail);



