-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('STANDARD_USER', 'ADMIN') NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    street VARCHAR(255),
    city VARCHAR(100),
    province VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    profile_picture_url VARCHAR(500),
    created_at DATETIME NOT NULL
);

-- Trips table
CREATE TABLE IF NOT EXISTS trips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    purpose ENUM('BUSINESS', 'LEISURE', 'MEDICAL', 'EDUCATION') NOT NULL,
    accompanied_members INT,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_trips_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tracked flights table
CREATE TABLE IF NOT EXISTS tracked_flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    flight_number VARCHAR(255) NOT NULL,
    airline_name VARCHAR(255),
    origin_iata VARCHAR(3),
    destination_iata VARCHAR(3),
    seat_number VARCHAR(10),
    seat_type ENUM('WINDOW', 'MIDDLE', 'AISLE'),
    cabin_class ENUM('ECONOMY', 'BUSINESS'),
    reference_number VARCHAR(20),
    scheduled_departure DATETIME,
    scheduled_arrival DATETIME,
    actual_departure DATETIME,
    actual_arrival DATETIME,
    departure_delay_minutes INT,
    arrival_delay_minutes INT,
    status ENUM('ON_TIME', 'DELAYED', 'CANCELLED', 'LANDED') NOT NULL,
    last_synced_at DATETIME,
    CONSTRAINT fk_tracked_flights_trip FOREIGN KEY (trip_id) REFERENCES trips(id)
);

-- Flight status history table
CREATE TABLE IF NOT EXISTS flight_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracked_flight_id BIGINT NOT NULL,
    status ENUM('ON_TIME', 'DELAYED', 'CANCELLED', 'LANDED') NOT NULL,
    recorded_at DATETIME NOT NULL,
    CONSTRAINT fk_status_history_flight FOREIGN KEY (tracked_flight_id) REFERENCES tracked_flights(id)
);